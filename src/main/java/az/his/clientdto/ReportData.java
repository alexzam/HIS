package az.his.clientdto;

import az.his.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public List<SeriesDto> getSeries(){
        ArrayList<SeriesDto> series = new ArrayList<>(seriesFields.size());
        for (String seriesId : seriesFields.keySet()) {
            series.add(new SeriesDto(seriesFields.get(seriesId), seriesNames.get(seriesId)));
        }
        return series;
    }

    public List<Map<String, Object>> getItems(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<Map<String, Object>> items = new ArrayList<>(data.keySet().size());

        for (Date date : data.keySet()) {
            Map<String, Object> day = new HashMap<>();
            day.put("date", df.format(date));

            for (String seriesId : seriesFields.keySet()) {
                Double val = data.get(date).get(seriesId);
                if (val == null) val = 0d;
                day.put(seriesFields.get(seriesId), val);
            }

            items.add(day);
        }

        return items;
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

    private class SeriesDto {
        private String field;
        private String name;

        public SeriesDto(String field, String name) {
            this.field = field;
            this.name = name;
        }

        public String getField() {
            return field;
        }

        public String getName() {
            return name;
        }
    }
}
