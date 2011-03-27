package az.his;

import az.his.ejb.ContentManager;
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
import java.util.List;

/**
 * For fetching transaction category data in JSON
 */
@WebServlet(name = "TrCategoryDataServlet", value = "/trcategory-data")
public class TrCategoryDataServlet extends HttpServlet {
    @EJB(name = "java:module/ContMan")
    private ContentManager cm;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");

        if (type == null) throw new ServletException("No type param");

        List<TransactionCategory> cats;
        JSONObject ret = new JSONObject();
        JSONArray items = new JSONArray();
        if (type.equals("e")) {
            cats = cm.getTransactionCategories(TransactionCategory.CatType.EXP);
        } else if (type.equals("i")) {
            cats = cm.getTransactionCategories(TransactionCategory.CatType.INC);
        } else {
            throw new ServletException("Incorrect type");
        }

        try {
            ret.put("identifier", "id");
            ret.put("label", "name");

            for (TransactionCategory cat : cats) {
                JSONObject item = new JSONObject();

                item.put("id", cat.getId());
                item.put("name", cat.getName());

                items.put(item);
            }
            ret.put("items", items);
        } catch (JSONException e) {
            throw new ServletException(e);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().append(ret.toString());
    }
}