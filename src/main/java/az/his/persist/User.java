package az.his.persist;

import az.his.AuthUtil;
import az.his.DBUtil;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.util.List;

/**
 * User of the system. We know two of them.
 */
@Entity(name = "user")
public class User {
    private int id;
    private String Name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Transient
    public static List<User> getAll(ApplicationContext context) {
        DBUtil dbUtil = DBUtil.getInstance(context);
        return dbUtil.findAll(User.class);
    }

    @Transient
    public long getPersonalExpense(ApplicationContext context, Account acc) {
        Session session = DBUtil.getCurrentSession(context);
        Long out = (Long) session
                .createQuery("select sum(amount) from transaction where account = ? and common = true and actor = ?")
                .setEntity(0, acc)
                .setEntity(1, this)
                .uniqueResult();
        if (out == null) out = 0l;
        else out = -out;
        return out;
    }

    @Transient
    public long getPersonalDonation(ApplicationContext context, Account acc) {
        Session session = DBUtil.getCurrentSession(context);
        Long out = (Long) session
                .createQuery("select sum(amount) from transaction where account = ? and common = false and actor = ?")
                .setEntity(0, acc)
                .setEntity(1, this)
                .uniqueResult();
        if (out == null) out = 0l;
        return out;
    }

    @Transient
    public static User getCurrentUser(ApplicationContext context){
        return DBUtil.getInstance(context).get(User.class, AuthUtil.getUid());
    }

    public static User getById(ApplicationContext context, int uid) {
        return DBUtil.getInstance(context).get(User.class, uid);
    }
}
