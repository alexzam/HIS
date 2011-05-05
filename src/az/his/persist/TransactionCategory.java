package az.his.persist;

import az.his.DBUtil;

import javax.persistence.*;
import java.util.List;

/**
 * Transaction category
 */
@Entity(name = "transcategory")
public class TransactionCategory {
    public enum CatType {
        EXP, INC
    }

    private int id;
    private String name;
    private CatType type;

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

    public CatType getType() {
        return type;
    }

    public void setType(CatType type) {
        this.type = type;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static List<TransactionCategory> getByType(CatType type) {
        return DBUtil.getSession().createQuery("from az.his.persist.TransactionCategory where type = :type")
                .setParameter("type", type)
                .list();
    }
}
