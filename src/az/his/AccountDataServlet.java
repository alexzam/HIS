package az.his;

import az.his.ejb.ContentManager;
import az.his.persist.Transaction;
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
import java.util.List;

/**
 * For fetching account data in JSON
 */
@WebServlet(name = "AccountDataServlet", value = "/account-data")
public class AccountDataServlet extends HttpServlet {
    @EJB(name = "java:module/ContMan")
    private ContentManager cm;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject ret = new JSONObject();
        JSONArray items = new JSONArray();
        List<Transaction> transactions = cm.getAllTransactions();

        try {
            ret.put("identifier", "id");

            for (Transaction transaction : transactions) {
                items.put(transaction.getJson());
            }

            ret.put("items", items);
        } catch (JSONException e) {
            throw new ServletException(e);
        }

        response.getWriter().append(ret.toString());
    }
}
