package az.his.persist;

import az.his.DBUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "sysParameters")
public class SysParameter {
    private int id;
    private String name;
    private String val;
    private User owner;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @ManyToOne
    @JoinColumn(name = "owner")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static List<SysParameter> getAllGeneral() {
        Session currentSession = DBUtil.getCurrentSession();
        if (currentSession == null) return Collections.emptyList();
        return currentSession.createQuery("from SysParameter where owner is null").list();
    }
}
