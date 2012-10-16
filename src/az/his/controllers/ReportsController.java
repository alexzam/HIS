package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.TransactionCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportsController {
    @Resource(name = "dbUtil")
    private DBUtil dbUtil;

    @RequestMapping(method = RequestMethod.GET)
    public String reportsPage() {
        return "reports";
    }

    @RequestMapping("/data/spendbycat")
    @Transactional(readOnly = true)
    public void dataSpendByCategory(HttpServletResponse resp) throws JSONException, IOException {
        List results = DBUtil.getCurrentSession().createQuery(
                "select tc.name as name, -sum(t.amount) as val " +
                        "from transaction t, transcategory tc " +
                        "where tc = t.category " +
                        "and tc.id != " + TransactionCategory.CAT_DONATE +
                        "and tc.id != " + TransactionCategory.CAT_REFUND +
                        "group by tc.name " +
                        "order by sum(t.amount) desc").list();

        JSONArray items = new JSONArray();
        for (Object result : results) {
            Object[] arr = (Object[]) result;
            String name = (String) arr[0];
            double amount = (((Long) arr[1]).doubleValue() / 100);

            JSONObject item = new JSONObject();
            item.put("name", name);
            item.put("value", amount);
            items.put(item);
        }

        JSONObject ret = new JSONObject();
        ret.put("items", items);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().append(ret.toString());
    }
}
