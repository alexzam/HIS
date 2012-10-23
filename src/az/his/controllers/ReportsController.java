package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.TransactionCategory;
import org.hibernate.Query;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @RequestMapping("/data/expenses")
    @Transactional(readOnly = true)
    public void dataExpenses(HttpServletResponse resp) throws JSONException, IOException {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Timestamp from = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, 1);
        Timestamp to = new Timestamp(cal.getTimeInMillis());

        Query query = DBUtil.getCurrentSession().createQuery(
                "select date(t.timestmp) as date, -sum(t.amount) as val " +
                        "from transaction t " +
                        "where " +
                        "t.timestmp >= :from " +
                        "and t.timestmp <= :to " +
                        "and t.category.id != " + TransactionCategory.CAT_DONATE +
                        "and t.category.id != " + TransactionCategory.CAT_REFUND +
                        "group by date(t.timestmp) " +
                        "order by date(t.timestmp)");
        query.setTimestamp("from", from);
        query.setTimestamp("to", to);
        List results = query.list();

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        JSONArray items = new JSONArray();
        for (Object result : results) {
            Object[] arr = (Object[]) result;
            double amount = (((Long) arr[1]).doubleValue() / 100);

            JSONObject item = new JSONObject();
            item.put("date", df.format(arr[0]));
            item.put("value", amount);
            item.put("cat", 0);
            items.put(item);
        }

        JSONObject ret = new JSONObject();
        ret.put("items", items);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().append(ret.toString());
    }
}
