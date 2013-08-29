package az.his.controllers;

import az.his.DBUtil;
import az.his.DateUtil;
import az.his.clientdto.ReportData;
import az.his.persist.TransactionCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportsController {
    @Autowired
    private ApplicationContext appContext;

    @RequestMapping(method = RequestMethod.GET)
    public String reportsPage() {
        return "reports";
    }

    @RequestMapping("/data/spendbycat")
    @Transactional(readOnly = true)
    public void dataSpendByCategory(HttpServletResponse resp) throws JSONException, IOException {
        List results = DBUtil.getCurrentSession(appContext).createQuery(
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
    @ResponseBody
    public String dataExpenses(HttpServletResponse resp,
                               @RequestParam(value = "from", required = false) Long rawFrom,
                               @RequestParam(value = "to", required = false) Long rawTo,
                               @RequestParam(value = "cat", required = false) Integer[] cat,
                               @RequestParam("group") String groupMode)
            throws JSONException, IOException, ServletException {

        Calendar cal = DateUtil.convertInDateParam(rawFrom);
        if (cal == null) cal = DateUtil.createCalDate1();
        Timestamp from = new Timestamp(cal.getTimeInMillis());

        cal = DateUtil.convertInDateParam(rawTo);
        if (cal == null) {
            cal = DateUtil.createCalDate1();
            cal.add(Calendar.MONTH, 1);
        }
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Timestamp to = new Timestamp(cal.getTimeInMillis());

        if (cat == null) cat = new Integer[]{};

        // "Total" series
        List results = DBUtil.getCurrentSession().createQuery(
                "select date(t.timestmp) as date, -sum(t.amount) as val " +
                        "from transaction t " +
                        "where " +
                        "t.timestmp >= :from " +
                        "and t.timestmp <= :to " +
                        "and t.category.id != " + TransactionCategory.CAT_DONATE +
                        "and t.category.id != " + TransactionCategory.CAT_REFUND +
                        "group by date(t.timestmp) " +
                        "order by date(t.timestmp)")
                .setTimestamp("from", from)
                .setTimestamp("to", to)
                .list();

        ReportData.Mode mode;
        if (groupMode.equals("D")) mode = ReportData.Mode.DAY;
        else if (groupMode.equals("M")) mode = ReportData.Mode.MONTH;
        else mode = ReportData.Mode.WEEK;

        ReportData ret = new ReportData(from, to, mode);
        addSeries(results, ret, "tot", "Total");

        // Series by category
        for (Integer catId : cat) {
            results = DBUtil.getCurrentSession().createQuery(
                    "select date(t.timestmp) as date, -sum(t.amount) as val " +
                            "from transaction t " +
                            "where " +
                            "t.timestmp >= :from " +
                            "and t.timestmp <= :to " +
                            "and t.category.id = :cat " +
                            "group by date(t.timestmp) " +
                            "order by date(t.timestmp)")
                    .setTimestamp("from", from)
                    .setTimestamp("to", to)
                    .setInteger("cat", catId)
                    .list();

            String catName = DBUtil.getInstance(appContext).get(TransactionCategory.class, catId).getName();

            addSeries(results, ret, catId.toString(), catName);
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        return ret.getJson();
    }

    /**
     * Add series with data.
     *
     * @param results    Data
     * @param ret        Returned object
     * @param seriesId   Series ID
     * @param seriesName Series name
     */
    private void addSeries(List results, ReportData ret, String seriesId, String seriesName) {
        ret.addSeries(seriesId, seriesName);
        for (Object result : results) {
            Object[] arr = (Object[]) result;
            double amount = (((Long) arr[1]).doubleValue() / 100);

            ret.addData((Date) arr[0], seriesId, amount);
        }
    }
}
