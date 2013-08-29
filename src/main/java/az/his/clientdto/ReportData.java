package az.his.clientdto;

import az.his.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportData {
    public enum Mode {
        DAY, WEEK, MONTH
    }

    private Map<String, String> seriesNames;

    private Map<String, String> seriesFields;
    private Map<Date, Map<String, Double>> data;
    private int fieldCount = 0;
    private Mode mode;

    public ReportData(Date from, Date to, Mode mode) {
        this.mode = mode;
        seriesNames = new HashMap<String, String>();
        seriesFields = new HashMap<String, String>();
        data = new HashMap<Date, Map<String, Double>>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        moveDateToPerStart(cal);

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(to);
        moveDateToPerStart(calTo);

        while (!cal.after(calTo)) {
            data.put(new Date(cal.getTimeInMillis()), new HashMap<String, Double>());
            switch (mode) {
                case DAY:
                    cal.add(Calendar.DATE, 1);
                    break;
                case WEEK:
                    cal.add(Calendar.DATE, 7);
                    break;
                case MONTH:
                    cal.add(Calendar.MONTH, 1);
                    break;
            }
        }
    }

    public void addSeries(String id, String name) {
        String fieldName = "val" + fieldCount;
        fieldCount++;

        seriesNames.put(id, name);
        seriesFields.put(id, fieldName);
    }

    public void addData(Date date, String seriesId, Double value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        moveDateToPerStart(cal);

        Map<String, Double> day = data.get(new Date(cal.getTimeInMillis()));
        if (day == null) return;

        day.put(seriesId, value);
    }

    public String getJson() throws ServletException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        try {
            JSONArray jData = new JSONArray();

            for (Date date : data.keySet()) {
                JSONObject jDay = new JSONObject();

                jDay.put("date", df.format(date));
                for (String seriesId : seriesFields.keySet()) {
                    Double val = data.get(date).get(seriesId);
                    if (val == null) val = 0d;
                    jDay.put(seriesFields.get(seriesId), val);
                }

                jData.put(jDay);
            }


            JSONObject ret = new JSONObject();
            ret.put("items", jData);

            JSONArray jSeries = new JSONArray();

            for (String seriesId : seriesFields.keySet()) {
                JSONObject jSerie = new JSONObject();
                jSerie.put("field", seriesFields.get(seriesId));
                jSerie.put("name", seriesNames.get(seriesId));
                jSeries.put(jSerie);
            }

            ret.put("series", jSeries);

            return ret.toString();
        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }

    private void moveDateToPerStart(Calendar cal){
        switch (mode) {
            case DAY:
                DateUtil.moveToMidnight(cal);
                break;
            case WEEK:
                DateUtil.moveToMonday(cal);
                break;
            case MONTH:
                DateUtil.moveToFirstOfMonth(cal);
                break;
        }
    }
}
