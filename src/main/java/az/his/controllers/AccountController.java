package az.his.controllers;

import az.his.AuthUtil;
import az.his.DBUtil;
import az.his.DateUtil;
import az.his.clientdto.CategoryDto;
import az.his.clientdto.StoreResponse;
import az.his.clientdto.TransactionDto;
import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import az.his.persist.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    public static final String CSV_DIVIDER = ";";

    @Autowired
    ApplicationContext appContext;

    @Autowired
    public DBUtil dbUtil;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String accountPage(Model model) throws ServletException {
        List<User> usersNotMe = User.getAll(appContext);

        User userMe = null;
        for (User user : usersNotMe) {
            if (user.getId() == AuthUtil.getUid()) {
                userMe = user;
                usersNotMe.remove(user);
                break;
            }
        }
        if (userMe == null) throw new ServletException("Where is users table?!");
        model.addAttribute("users", usersNotMe);
        model.addAttribute("me", userMe);

        return "account";
    }

    @RequestMapping(value = "/catdata")
    @Transactional(readOnly = true)
    @ResponseBody
    public StoreResponse<CategoryDto> categoryData(@RequestParam(value = "type", required = false) String type,
                                                  HttpServletResponse resp)
            throws ServletException, IOException {

        if (type == null) type = "a";

        List<TransactionCategory> cats;
        switch (type) {
            case "e":
                cats = TransactionCategory.getByType(appContext, TransactionCategory.CatType.EXP);
                break;
            case "i":
                cats = TransactionCategory.getByType(appContext, TransactionCategory.CatType.INC);
                break;
            default:
                cats = dbUtil.findAll(TransactionCategory.class);
                break;
        }

        StoreResponse<CategoryDto> response = new StoreResponse<>();

        for (TransactionCategory cat : cats) {
            response.addItem(new CategoryDto(cat));
        }
        return response;
    }

    /**
     * Adds a transaction or two (see parameters description).
     *
     * @param actId     Actor of transaction.
     * @param rawAmount Amount in basic currency (copecks).
     * @param trType    Transaction type. "i" for donation, "r" for refund, "p" for expence and "a" for expence from
     *                  account. The last value will mean that two transactions will be created.
     * @param catId     Category ID. For "i" and "r" transactions may be null.
     * @param catName   Category name. Should be not null if transaction is not of "i" or "r" type and category ID is 0.
     *                  That means new category.
     * @param rawDate   Unix date of transaction.
     * @param comment   Comment to transaction.
     * @return Empty JSON as nothing needs to be returned
     * @throws ServletException Standart
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST, params = "act=put")
    @Transactional
    @ResponseBody
    public String addTransaction(
            @RequestParam("actor") int actId,
            @RequestParam("amount") float rawAmount,
            @RequestParam("type") String trType,
            @RequestParam(value = "cat", required = false) Integer catId,
            @RequestParam(value = "catname", required = false) String catName,
            @RequestParam("date") long rawDate,
            @RequestParam("comment") String comment
    ) throws ServletException {

        int amount = Math.round(rawAmount * 100);
        boolean common;

        switch (trType) {
            case "i":
                catId = TransactionCategory.CAT_DONATE;
                common = false;
                break;
            case "r":
                catId = TransactionCategory.CAT_REFUND;
                amount = -amount;
                common = false;
                break;
            default:
                if (catId == null) throw new ServletException("Category ID should be filled (param 'cat')");
                amount = -amount;
                common = true;
                break;
        }

        TransactionCategory cat;
        if (catId == 0) {
            // New category if needed
            if (catName == null) throw new ServletException("Category name should be filled (param 'catname')");

            List<TransactionCategory> cats = dbUtil.findByProperty(TransactionCategory.class, "name", catName);
            if (cats.size() == 0) {
                // New category
                cat = new TransactionCategory();
                cat.setName(catName);
                cat.setType(TransactionCategory.CatType.EXP);
                cat = dbUtil.merge(cat);
            } else {
                // Reuse category
                cat = cats.get(0);
            }
        } else {
            cat = dbUtil.get(TransactionCategory.class, catId);
        }

        Date time = new Date(rawDate);
        User actor = dbUtil.get(User.class, actId);

        Account account = Account.getCommon();

        Transaction trans = new Transaction();
        trans.setActor(actor);
        trans.setAccount(account);
        trans.setTimestmp(time);
        trans.setAmount(amount);
        trans.setCategory(cat);
        trans.setComment(comment);
        trans.setCommon(common);
        dbUtil.persist(trans);

        if (trType.equals("a")) {
            // Expense form account itself means we should make immediate refund
            trans = new Transaction();
            trans.setActor(actor);
            trans.setAccount(account);
            trans.setTimestmp(time);
            trans.setAmount(amount);
            trans.setCategory(dbUtil.get(TransactionCategory.class, TransactionCategory.CAT_REFUND));
            trans.setCommon(false);
            dbUtil.persist(trans);
        }

        return "{}";
    }

    /**
     * Delete some transactions. If category becomes empty, delete it too.
     *
     * @param rawIds Transaction ID comma-separated list.
     * @return Empty string
     * @throws ServletException Standart
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST, params = "act=del")
    @ResponseBody
    @Transactional
    public String delTransactions(
            @RequestParam("ids") String rawIds
    ) throws ServletException {
        String[] ids = rawIds.split(",");

        for (String id : ids) {
            try {
                int iid = Integer.parseInt(id);
                dbUtil.delete(Transaction.class, iid);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        return "";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public String updateTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction trans = dbUtil.get(Transaction.class, transactionDto.getId());

        trans.setActor(dbUtil.get(User.class, transactionDto.getActor_id()));
        trans.setAmount(transactionDto.getAmount());
        trans.setCategory(dbUtil.get(TransactionCategory.class, transactionDto.getCategory_id()));
        trans.setComment(transactionDto.getComment());
        trans.setTimestmp(new Date(transactionDto.getTimestamp()));

        dbUtil.update(trans);

        return "{ok:1}";
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @ResponseBody
    public Map<String, String> getStatistic(HttpServletResponse resp) throws IOException {
        Account acc = Account.getCommon();
        long totalExp = acc.getTotalExp();
        long eachExp = totalExp / 2;
        User user = User.getCurrentUser();
        long persExp = user.getPersonalExpense(appContext, acc);
        long persDonation = user.getPersonalDonation(appContext, acc);

        HashMap<String, String> ret = new HashMap<>(7);
        ret.put("amount", acc.getAmountPrintable());
        ret.put("totalExp", DBUtil.formatCurrency(totalExp));
        ret.put("eachExp", DBUtil.formatCurrency(eachExp));
        ret.put("persExp", DBUtil.formatCurrency(persExp));
        ret.put("persDonation", DBUtil.formatCurrency(persDonation));
        ret.put("persSpent", DBUtil.formatCurrency(persDonation + persExp));
        ret.put("persBalance", DBUtil.formatCurrency(persDonation + persExp - eachExp));

        return ret;
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @ResponseBody
    public StoreResponse<TransactionDto> getTransactions(
            HttpServletResponse resp,
            @RequestParam(value = "from", required = false) Long rawFrom,
            @RequestParam(value = "to", required = false) Long rawTo,
            @RequestParam(value = "cat", required = false) Integer[] cat
    ) throws IOException {

        List<Transaction> transactions = getFilteredTransactions(rawFrom, rawTo, cat);

        StoreResponse<TransactionDto> ret = new StoreResponse<>();

        for (Transaction transaction : transactions) {
            ret.addItem(new TransactionDto(transaction));
        }

        return ret;
    }

    private List<Transaction> getFilteredTransactions(Long rawFrom, Long rawTo, Integer[] cat) {
        Calendar calFrom = DateUtil.convertInDateParam(rawFrom);
        if (calFrom == null) calFrom = DateUtil.createCalDate1();
        Date fromDate = calFrom.getTime();

        Calendar calTo = DateUtil.convertInDateParam(rawTo);
        if (calTo == null) {
            calTo = DateUtil.createCalDate1();
            calTo.add(Calendar.MONTH, 1);
        }
        calTo.add(Calendar.DAY_OF_MONTH, 1);
        Date toDate = calTo.getTime();

        if (cat == null) cat = new Integer[]{};

        return Transaction.getFiltered(appContext, fromDate, toDate, cat);
    }

    @RequestMapping(value = "/csv")
    @Transactional(readOnly = true)
    public void getTransactionsCSV(
            HttpServletResponse resp,
            @RequestParam(value = "from", required = false) Long rawFrom,
            @RequestParam(value = "to", required = false) Long rawTo,
            @RequestParam(value = "cat", required = false) Integer[] cat
    ) throws IOException {
        List<Transaction> transactions = getFilteredTransactions(rawFrom, rawTo, cat);

        StringBuilder ret = new StringBuilder();

        ret.append("User;Date;Category;Amount;Comment\n");
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
        formatter.applyPattern("dd.MM.yyyy");

        for (Transaction tr : transactions){
            ret.append(tr.getActor().getName()).append(CSV_DIVIDER);
            ret.append(formatter.format(tr.getTimestmp())).append(CSV_DIVIDER);
            ret.append(tr.getCategory().getName()).append(CSV_DIVIDER);
            ret.append(tr.getAmount()).append(CSV_DIVIDER);
            ret.append(tr.getComment()).append("\n");
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/csv");
        resp.addHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");
        resp.getWriter().append(ret.toString());
    }
}
