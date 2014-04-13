package az.his.persist;

import az.his.DBUtil;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.util.List;

/**
 * Transaction category
 */
@Entity(name = "transcategory")
public class TransactionCategory {
    public static final int CAT_DONATE = 1;
    public static final int CAT_REFUND = 2;

    public enum CatType {
        EXP, INC, TRANSFER, CORRECTION
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
    public static List<TransactionCategory> getByType(ApplicationContext context, CatType type) {
        return DBUtil.getCurrentSession(context).createQuery("from az.his.persist.TransactionCategory where type = :type")
                .setParameter("type", type)
                .list();
    }

    @Transient
    public static List<TransactionCategory> getAll(ApplicationContext context) {
        return DBUtil.getInstance(context).findAll(TransactionCategory.class);
    }
}
