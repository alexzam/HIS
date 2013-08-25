package az.his.controllers;

import az.his.AuthUtil;
import az.his.DBUtil;
import az.his.DateUtil;
import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import az.his.persist.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void categoryData(@RequestParam(value = "type", required = false) String type, HttpServletResponse resp)
            throws ServletException, IOException {
        if (type == null) type = "a";

        List<TransactionCategory> cats;
        JSONObject ret = new JSONObject();
        JSONArray items = new JSONArray();
        if (type.equals("e")) {
            cats = TransactionCategory.getByType(appContext, TransactionCategory.CatType.EXP);
        } else if (type.equals("i")) {
            cats = TransactionCategory.getByType(appContext, TransactionCategory.CatType.INC);
        } else {
            cats = dbUtil.findAll(TransactionCategory.class);
        }

        try {
            ret.put("identifier", "id");
            ret.put("label", "name");

            JSONObject item;

            for (TransactionCategory cat : cats) {
                item = new JSONObject();

                item.put("id", cat.getId());
                item.put("name", cat.getName());
                String oType;
                switch (cat.getType()) {
                    case EXP:
                        oType = "e";
                        break;
                    case INC:
                        oType = "i";
                        break;
                    case NONE:
                        oType = "n";
                        break;
                    default:
                        oType = "null";
                }
                item.put("type", oType);

                items.put(item);
            }
            ret.put("items", items);
        } catch (JSONException e) {
            throw new ServletException(e);
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().append(ret.toString());
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

        if (trType.equals("i")) {
            catId = TransactionCategory.CAT_DONATE;
            common = false;
        } else if (trType.equals("r")) {
            catId = TransactionCategory.CAT_REFUND;
            amount = -amount;
            common = false;
        } else {
            if (catId == null) throw new ServletException("Category ID should be filled (param 'cat')");
            amount = -amount;
            common = true;
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

        Account account = Account.getCommon(appContext);

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
    public String updateTransaction(@RequestBody String body) throws JSONException {
        JSONObject req = new JSONObject(body);

        Transaction trans = dbUtil.get(Transaction.class, req.getInt("id"));

        trans.setActor(dbUtil.get(User.class, req.getInt("actor_id")));
        trans.setAmount(req.getDouble("amount"));
        trans.setCategory(dbUtil.get(TransactionCategory.class, req.getInt("category_id")));
        trans.setComment(req.getString("comment"));
        trans.setTimestmp(new Date(req.getLong("timestamp")));

        dbUtil.update(trans);

        return "{ok:1}";
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public void getStatistic(HttpServletResponse resp) throws JSONException, IOException {
        JSONObject ret = new JSONObject();

        Account acc = Account.getCommon(appContext);
        long totalExp = acc.getTotalExp(appContext);
        long eachExp = totalExp / 2;
        User user = User.getCurrentUser(appContext);
        long persExp = user.getPersonalExpense(appContext, acc);
        long persDonation = user.getPersonalDonation(appContext, acc);

        ret.put("amount", acc.getAmountPrintable());
        ret.put("totalExp", DBUtil.formatCurrency(totalExp));
        ret.put("eachExp", DBUtil.formatCurrency(eachExp));
        ret.put("persExp", DBUtil.formatCurrency(persExp));
        ret.put("persDonation", DBUtil.formatCurrency(persDonation));
        ret.put("persSpent", DBUtil.formatCurrency(persDonation + persExp));
        ret.put("persBalance", DBUtil.formatCurrency(persDonation + persExp - eachExp));

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().append(ret.toString());
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public void getTransactions(
            HttpServletResponse resp,
            @RequestParam(value = "from", required = false) Long rawFrom,
            @RequestParam(value = "to", required = false) Long rawTo,
            @RequestParam(value = "cat", required = false) Integer[] cat
    ) throws JSONException, IOException {
        JSONObject ret = new JSONObject();

        List<Transaction> transactions = getFilteredTransactions(rawFrom, rawTo, cat);
        JSONArray items = new JSONArray();

        ret.put("identifier", "id");

        for (Transaction transaction : transactions) {
            items.put(transaction.getJson());
        }

        ret.put("items", items);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().append(ret.toString());
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
