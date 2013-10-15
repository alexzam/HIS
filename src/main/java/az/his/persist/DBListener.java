package az.his.persist;

import org.hibernate.Session;

public interface DBListener {
    public void beforeDelete(Session session);
    public void beforeInsert(Session session);
}
