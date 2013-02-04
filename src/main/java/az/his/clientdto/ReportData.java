package az.his.clientdto;

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
    private Map<String, String> seriesNames;
    private Map<String, String> seriesFields;
    private Map<Date, Map<String, Double>> data;

    private int fieldCount = 0;

    public ReportData(Date from, Date to) {
        seriesNames = new HashMap<String, String>();
        seriesFields = new HashMap<String, String>();
        data = new HashMap<Date, Map<String, Double>>();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(from.getTime());
        while (cal.getTimeInMillis() <= to.getTime()) {
            data.put(new Date(cal.getTimeInMillis()), new HashMap<String, Double>());
            cal.add(Calendar.DATE, 1);
        }
    }

    public void addSeries(String id, String name) {
        String fieldName = "val" + fieldCount;
        fieldCount++;

        seriesNames.put(id, name);
        seriesFields.put(id, fieldName);
    }

    public void addData(Date date, String seriesId, Double value) {
        Map<String, Double> day = data.get(date);
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
                    if(val == null) val = 0d;
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
}
