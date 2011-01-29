//import org.hibernate.HibernateException;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.AnnotationConfiguration;
//import org.hibernate.metadata.ClassMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: AlexZam
 * Date: 24.01.11
 * Time: 2:34
 * To change this template use File | Settings | File Templates.
 */
public class Main {
//    private static final SessionFactory ourSessionFactory;
//
//    static {
//        try {
//            ourSessionFactory = new AnnotationConfiguration().
//                    configure("hibernate.cfg.xml").
//                    buildSessionFactory();
//        } catch (Throwable ex) {
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    public static Session getSession() throws HibernateException {
//        return ourSessionFactory.openSession();
//    }
//
//    public static void main(final String[] args) throws Exception {
//        final Session session = getSession();
//        try {
//            System.out.println("querying all the managed entities...");
//            final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
//            for (Object key : metadataMap.keySet()) {
//                final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
//                final String entityName = classMetadata.getEntityName();
//                final Query query = session.createQuery("from " + entityName);
//                System.out.println("executing: " + query.getQueryString());
//                for (Object o : query.list()) {
//                    System.out.println("  " + o);
//                }
//            }
//        } finally {
//            session.close();
//        }
//    }
}
