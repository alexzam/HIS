package az.his;

import az.his.filters.AuthFilter;
import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import az.his.persist.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * For fetching account data in JSON
 */
public class AccountDataServlet extends HttpServlet {
    /**
     * If act == "put", add transaction.
     *
     * @param request  Standart
     * @param response Standart
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        String act = request.getParameter("act");
        if ("put".equals(act)) addTransaction(request);
        else if ("del".equals(act)) delTransactions(request);
    }

    private void delTransactions(HttpServletRequest request) throws ServletException {
        String[] ids = checkParam(request, "ids").split(",");
        DBManager dbman = DBUtil.getDBManFromReq(request);

        for (String id : ids) {
            try {
                int iid = Integer.parseInt(id);
                dbman.delete(Transaction.class, iid);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    /**
     * Adds a transaction. Type (i, r, p, a), cat, catname, actor, amount, comment, date.
     *
     * @param request Standart
     * @throws ServletException Standart
     */
    private void addTransaction(HttpServletRequest request) throws ServletException {
        int actId = Integer.parseInt(checkParam(request, "actor"));
        int amount = Math.round(Float.parseFloat(checkParam(request, "amount")) * 100);
        String trType = checkParam(request, "type");
        boolean common;
        DBManager dbman = DBUtil.getDBManFromReq(request);

        int catId;
        if (trType.equals("i")) {
            catId = TransactionCategory.CAT_DONATE;
            common = false;
        } else if (trType.equals("r")) {
            catId = TransactionCategory.CAT_REFUND;
            amount = -amount;
            common = false;
        } else {
            catId = Integer.parseInt(checkParam(request, "cat"));
            amount = -amount;
            common = true;
        }

        TransactionCategory cat;
        if (catId == 0) {
            // New category
            cat = new TransactionCategory();
            cat.setName(checkParam(request, "catname"));
            cat.setType(TransactionCategory.CatType.EXP);
            cat = dbman.merge(cat);
        } else {
            cat = dbman.get(TransactionCategory.class, catId);
        }

        String comment = request.getParameter("comment");
        Date time = new Date(Long.parseLong(checkParam(request, "date")));
        User actor = dbman.get(User.class, actId);

        Account account = Account.getCommon(dbman);

        Transaction trans = new Transaction();
        trans.setActor(actor);
        trans.setAccount(account);
        trans.setTimestmp(time);
        trans.setAmount(amount);
        trans.setCategory(cat);
        trans.setComment(comment);
        trans.setCommon(common);
        dbman.persist(trans);

        if (trType.equals("a")) {
            // Expense form account itself means we should make immediate refund
            trans = new Transaction();
            trans.setActor(actor);
            trans.setAccount(account);
            trans.setTimestmp(time);
            trans.setAmount(amount);
            trans.setCategory(dbman.get(TransactionCategory.class, TransactionCategory.CAT_REFUND));
            trans.setCommon(false);
            dbman.persist(trans);
        }
    }

    private String checkParam(HttpServletRequest request, String name) throws ServletException {
        String ret = request.getParameter(name);
        if (ret == null) throw new ServletException("No parameter " + name);
        return ret;
    }

    /**
     * act == getamount: return account stats <br/>
     * else: return transactions list
     *
     * @param request  Standart
     * @param response Standart
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject ret;

        String act = request.getParameter("act");

        try {
            if ("getamount".equals(act)) {
                ret = getStatistic(request);
            } else {
                ret = getTransactions(request);
            }
        } catch (JSONException e) {
            throw new ServletException(e);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().append(ret.toString());
    }

    private JSONObject getStatistic(HttpServletRequest req) throws JSONException {
        JSONObject ret = new JSONObject();
        DBManager dbman = DBUtil.getDBManFromReq(req);
        Account acc = Account.getCommon(dbman);
        long totalExp = acc.getTotalExp(dbman);
        long eachExp = totalExp / 2;
        User user = dbman.get(User.class, AuthFilter.getUid(req.getSession()));
        long persExp = user.getPersonalExpense(dbman, acc);
        long persDonation = user.getPersonalDonation(dbman, acc);

        ret.put("amount", acc.getAmountPrintable());
        ret.put("totalExp", DBUtil.formatCurrency(totalExp));
        ret.put("eachExp", DBUtil.formatCurrency(eachExp));
        ret.put("persExp", DBUtil.formatCurrency(persExp));
        ret.put("persDonation", DBUtil.formatCurrency(persDonation));
        ret.put("persSpent", DBUtil.formatCurrency(persDonation + persExp));
        ret.put("persBalance", DBUtil.formatCurrency(persDonation + persExp - eachExp));

        return ret;
    }

    private JSONObject getTransactions(HttpServletRequest request) throws JSONException {
        JSONObject ret = new JSONObject();
        DBManager dbman = DBUtil.getDBManFromReq(request);

        Calendar calFrom = new GregorianCalendar();
        calFrom.set(Calendar.DAY_OF_MONTH, 1);
        Date fromDate = calFrom.getTime();

        calFrom.add(Calendar.MONTH, 1);
        Date toDate = calFrom.getTime();

        int cat = 0;

        String param = request.getParameter("from");
        if (param != null && !param.equals("")) {
            fromDate = new Date(Long.parseLong(param));
        }

        param = request.getParameter("to");
        if (param != null && !param.equals("")) {
            // Plus day to include "to" date to filter
            toDate = new Date(Long.parseLong(param) + 24 * 3600 * 1000);
        }

        param = request.getParameter("cat");
        if (param != null && !param.equals("")) {
            cat = Integer.parseInt(param);
        }

        List<Transaction> transactions = Transaction.getFiltered(dbman, fromDate, toDate, cat);
        JSONArray items = new JSONArray();

        ret.put("identifier", "id");

        for (Transaction transaction : transactions) {
            items.put(transaction.getJson());
        }

        ret.put("items", items);

        return ret;
    }
}
