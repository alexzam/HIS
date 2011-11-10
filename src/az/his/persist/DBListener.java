package az.his.persist;

import az.his.DBManager;

public interface DBListener {
    public void beforeDelete(DBManager dbman);
    public void beforeInsert(DBManager dbman);
}
