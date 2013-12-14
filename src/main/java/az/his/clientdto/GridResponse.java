package az.his.clientdto;

import java.util.ArrayList;
import java.util.List;

public class GridResponse<T> {
    private List<T> items = new ArrayList<>();

    public String getIdentifier(){return "id";}

    public List<T> getItems(){
        return items;
    }

    public void addItem(T item){
        items.add(item);
    }
}
