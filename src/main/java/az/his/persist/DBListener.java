package az.his.persist;

import org.springframework.orm.hibernate3.HibernateTemplate;

public interface DBListener {
    public void beforeDelete(HibernateTemplate template);
    public void beforeInsert(HibernateTemplate template);
}
