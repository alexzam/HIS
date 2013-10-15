package az.his;

import az.his.persist.DBListener;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Hibernate set up
 */
public class DBUtil implements ApplicationContextAware {
    private static ApplicationContext appContext;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("UnusedDeclaration")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static DBUtil getInstance(ApplicationContext context) {
        return context.getBean(DBUtil.class);
    }

    public static DBUtil getInstance() {
        if (appContext == null) return null;
        return appContext.getBean(DBUtil.class);
    }

    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public static Session getCurrentSession(ApplicationContext context){
        return getInstance(context).getSession();
    }

    public static Session getCurrentSession() {
        DBUtil instance = getInstance();
        if(instance == null) return null;
        return getInstance().getSession();
    }

    private static java.text.NumberFormat intFormatter = new DecimalFormat("0");
    private static java.text.NumberFormat int2Formatter = new DecimalFormat("0.00");

    public static String formatCurrency(long sum) {
        if (sum % 100 != 0) {
            return int2Formatter.format((double) sum / 100);
        } else return intFormatter.format((double) sum / 100);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> findAll(Class<E> clazz) {
        return getSession().createQuery("from "+clazz.getName()).list();
    }

    @SuppressWarnings("unchecked")
    public <E> E get(Class<E> clazz, Serializable id) {
        return (E) getSession().load(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> findByProperty(Class<E> clazz, String name, String val) {
        Criteria query = getSession().createCriteria(clazz);
        query.add(Restrictions.eq(name, val));
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public <E> E merge(E obj) {
        return (E) getSession().merge(obj);
    }

    public void persist(Object obj) {
        Session session = getSession();
        if (obj instanceof DBListener) {
            ((DBListener) obj).beforeInsert(session);
        }
        session.persist(obj);
    }

    public <E> void delete(Class<E> clazz, int iid) {
        E obj = get(clazz, iid);
        Session session = getSession();
        if (obj instanceof DBListener) {
            ((DBListener) obj).beforeDelete(session);
        }
        session.delete(obj);
    }

    public void update(Object obj) {
        getSession().update(obj);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
