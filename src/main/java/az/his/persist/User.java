package az.his.persist;

import az.his.AuthUtil;
import az.his.DBUtil;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User of the system. We know two of them.
 */
@Entity(name = "user")
public class User {
    private int id;
    private String Name;
    private Map<String, SysParameter> sysParameters;
    private Map<String, SysParameter> generalSysParameters;
    private List<Account> ownedAccounts;

    public User() {
        List<SysParameter> parameters = SysParameter.getAllGeneral();
        generalSysParameters = new HashMap<String, SysParameter>(parameters.size());

        for (SysParameter parameter : parameters) {
            generalSysParameters.put(parameter.getName(), parameter);
        }
    }

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

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @MapKey(name = "name")
    public Map<String, SysParameter> getSysParameters() {
        return sysParameters;
    }

    public void setSysParameters(Map<String, SysParameter> sysParameters) {
        this.sysParameters = sysParameters;
    }

    @OneToMany(mappedBy = "owner")
    public List<Account> getOwnedAccounts() {
        return ownedAccounts;
    }

    public void setOwnedAccounts(List<Account> ownedAccounts) {
        this.ownedAccounts = ownedAccounts;
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
    public static User getCurrentUser() {
        return DBUtil.getInstance().get(User.class, AuthUtil.getUid());
    }

    @Transient
    public static User getById(ApplicationContext context, int uid) {
        return DBUtil.getInstance(context).get(User.class, uid);
    }

    @Transient
    @SuppressWarnings("unchecked")
    public SysParameter getSysParameter(String name) {
        SysParameter temp = sysParameters.get(name);
        if (temp != null) return temp;
        return generalSysParameters.get(name);
    }

    public void setSysParameter(String name, String val) {
        Session session = DBUtil.getCurrentSession();
        SysParameter temp = sysParameters.get(name);
        if (temp != null) {
            temp.setVal(val);
        } else {
            temp = new SysParameter();
            temp.setName(name);
            temp.setVal(val);
            temp.setOwner(this);
            sysParameters.put(name, temp);
            session.save(temp);
        }

        session.merge(this);
    }
}
