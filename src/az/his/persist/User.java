package az.his.persist;

import az.his.DBUtil;
import org.hibernate.Session;

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
    public static List<User> getAll() {
        return DBUtil.findAll(User.class);
    }

    @Transient
    public long getPersonalExpense(Account acc) {
        Session session = DBUtil.getSession();
        Long out = -(Long) session
                .createQuery("select sum(amount) from transaction where account = ? and common = true and actor = ?")
                .setEntity(0, acc)
                .setEntity(1, this)
                .uniqueResult();
        if (out == null) out = 0l;
        return out;
    }

    @Transient
    public long getPersonalDonation(Account acc) {
        Session session = DBUtil.getSession();
        Long out = (Long) session
                .createQuery("select sum(amount) from transaction where account = ? and common = false and actor = ?")
                .setEntity(0, acc)
                .setEntity(1, this)
                .uniqueResult();
        if (out == null) out = 0l;
        return out;
    }
}
