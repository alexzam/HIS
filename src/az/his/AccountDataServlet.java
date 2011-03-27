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
import java.util.Date;
import java.util.List;

/**
 * For fetching account data in JSON
 */
@WebServlet(name = "AccountDataServlet", value = "/account-data")
public class AccountDataServlet extends HttpServlet {
    @EJB(name = "java:module/ContMan")
    private ContentManager cm;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }
        String action = request.getParameter("act");

        if (action == null) throw new ServletException("No action");
        if (action.equals("addtr")) addTransaction(request);
        else throw new ServletException("Unnown action");
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
        JSONArray items = new JSONArray();
        List<Transaction> transactions = cm.getAllTransactions();

        String act = request.getParameter("act");

        try {
            if ("getamount".equals(act)) {
                ret.put("amount", cm.getAccountAmountPrintable(Account.ACC_COMMON));
            } else {
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
