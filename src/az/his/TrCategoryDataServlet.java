package az.his;

import az.his.persist.TransactionCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * For fetching transaction category data in JSON
 */
public class TrCategoryDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");

        if (type == null) type = "a";

        List<TransactionCategory> cats;
        JSONObject ret = new JSONObject();
        JSONArray items = new JSONArray();
        if (type.equals("e")) {
            cats = TransactionCategory.getByType(TransactionCategory.CatType.EXP);
        } else if (type.equals("i")) {
            cats = TransactionCategory.getByType(TransactionCategory.CatType.INC);
        } else {
            cats = DBUtil.findAll(TransactionCategory.class);
        }

        try {
            ret.put("identifier", "id");
            ret.put("label", "name");

            JSONObject item = new JSONObject();

            item.put("id", "0");
            item.put("name", "(Все)");
            item.put("type", "null");

            items.put(item);

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

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().append(ret.toString());
    }
}
