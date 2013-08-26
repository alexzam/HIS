package az.his;

import az.his.persist.DBListener;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Hibernate set up
 */
public class DBUtil extends HibernateDaoSupport implements ApplicationContextAware {
    private static ApplicationContext appContext;

    public static DBUtil getInstance(ApplicationContext context) {
        return context.getBean(DBUtil.class);
    }

    public static DBUtil getInstance() {
        if (appContext == null) return null;
        return appContext.getBean(DBUtil.class);
    }

    public static Session getCurrentSession(ApplicationContext context){
        return getInstance(context).getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    public static Session getCurrentSession() {
        DBUtil instance = getInstance();
        if(instance == null) return null;
        return getInstance().getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    private static java.text.NumberFormat intFormatter = new DecimalFormat("0");
    private static java.text.NumberFormat int2Formatter = new DecimalFormat("0.00");

    public static String formatCurrency(long sum) {
        if (sum % 100 != 0) {
            return int2Formatter.format((double) sum / 100);
        } else return intFormatter.format((double) sum / 100);
    }

    public <E> List<E> findAll(Class<E> clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }

    public <E> E get(Class<E> clazz, Serializable id) {
        return getHibernateTemplate().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> findByProperty(Class<E> clazz, String name, String val) {
        Criteria query = getSession().createCriteria(clazz);
        query.add(Restrictions.eq(name, val));
        return query.list();
    }

    public <E> E merge(E obj) {
        return getHibernateTemplate().merge(obj);
    }

    public void persist(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        if (obj instanceof DBListener) {
            ((DBListener) obj).beforeInsert(template);
            getSession().flush();
        }
        template.persist(obj);
    }

    public <E> void delete(Class<E> clazz, int iid) {
        E obj = get(clazz, iid);
        HibernateTemplate template = getHibernateTemplate();
        if (obj instanceof DBListener) {
            ((DBListener) obj).beforeDelete(template);
        }
        template.delete(obj);
    }

    public void update(Object obj) {
        getHibernateTemplate().update(obj);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
