package az.his.persist;

public interface DBListener {
    public void beforeDelete();
    public void beforeInsert();
}
