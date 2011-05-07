package az.his;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        String act = request.getParameter("act");
        if ("put".equals(act)) addTransaction(request);
    }

    private void addTransaction(HttpServletRequest request) throws ServletException {
        int actId = Integer.parseInt(checkParam(request, "actor"));
        float amount = Float.parseFloat(checkParam(request, "amount"));
        String trType = checkParam(request, "type");
        boolean common;

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
            cat = DBUtil.merge(cat);
        } else {
            cat = DBUtil.get(TransactionCategory.class, catId);
        }

        String comment = request.getParameter("comment");
        Date time = new Date(Long.parseLong(checkParam(request, "date")));
        User actor = DBUtil.get(User.class, actId);

        Account account = Account.getCommon();

        Transaction trans = new Transaction();
        trans.setActor(actor);
        trans.setAccount(account);
        trans.setTimestmp(time);
        trans.setAmount(amount);
        trans.setCategory(cat);
        trans.setComment(comment);
        trans.setCommon(common);
        if (!common) {
            account.setValue(account.getValue() + amount);
            DBUtil.merge(account);
        }
        DBUtil.persist(trans);

        if (trType.equals("a")) {
            // Expence form account itself means we should make immediate refund
            trans = new Transaction();
            trans.setActor(actor);
            trans.setAccount(account);
            trans.setTimestmp(time);
            trans.setAmount(amount);
            trans.setCategory(DBUtil.get(TransactionCategory.class, TransactionCategory.CAT_REFUND));
            trans.setCommon(false);

            account.setValue(account.getValue() + amount);
            DBUtil.merge(account);

            DBUtil.persist(trans);
        }
    }

    private String checkParam(HttpServletRequest request, String name) throws ServletException {
        String ret = request.getParameter(name);
        if (ret == null) throw new ServletException("No parameter " + name);
        return ret;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject ret = new JSONObject();

        String act = request.getParameter("act");

        try {
            if ("getamount".equals(act)) {
                ret.put("amount", Account.getCommon().getAmountPrintable());
            } else {
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

                List<Transaction> transactions = Transaction.getFiltered(fromDate, toDate, cat);
                JSONArray items = new JSONArray();

                ret.put("identifier", "id");

                for (Transaction transaction : transactions) {
                    items.put(transaction.getJson());
                }

                ret.put("items", items);
            }
        } catch (JSONException e) {
            throw new ServletException(e);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().append(ret.toString());
    }
}
