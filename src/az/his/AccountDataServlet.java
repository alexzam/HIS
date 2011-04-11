package az.his;

import az.his.ejb.ContentManager;
import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
@WebServlet(name = "AccountDataServlet", value = "/account-data")
public class AccountDataServlet extends HttpServlet {
    @EJB(name = "java:module/ContMan")
    private ContentManager cm;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void addTransaction(HttpServletRequest request) throws ServletException {
        int actId = Integer.parseInt(checkParam(request, "actor"));
        float amount = Float.parseFloat(checkParam(request, "amount"));

        int catId = Integer.parseInt(checkParam(request, "cat"));
        TransactionCategory cat;
        if (catId == 0) {
            // New category
            cat = new TransactionCategory();
            cat.setName(checkParam(request, "catname"));
            cat.setType((amount > 0) ? TransactionCategory.CatType.INC : TransactionCategory.CatType.EXP);
            cat = cm.merge(cat);
        } else {
            cat = cm.getTransCategory(catId);
        }

        String comment = request.getParameter("comment");
        Date time = new Date(Long.parseLong(checkParam(request, "date")));

        Account account = cm.getAccountById(Account.ACC_COMMON);

        Transaction trans = new Transaction();
        trans.setActor(cm.getUserById(actId));
        trans.setAccount(account);
        trans.setTimestmp(time);
        trans.setAmount(amount);
        trans.setCategory(cat);
        trans.setComment(comment);
        account.setValue(account.getValue() + amount);
        cm.merge(account);
        cm.persist(trans);
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
                ret.put("amount", cm.getAccountAmountPrintable(Account.ACC_COMMON));
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

                List<Transaction> transactions = cm.getTransactionsFiltered(fromDate, toDate, cat);
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        addTransaction(req);
    }


}
