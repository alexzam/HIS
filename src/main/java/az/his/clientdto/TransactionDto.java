package az.his.clientdto;

import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionDto {
    private int id;
    private double amount;
    private String timestamp;
    private int actor_id;
    private String actor_name;
    private String category_name;
    private int category_id;
    private String comment;
    private String type;

    public TransactionDto(Transaction transaction) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        TransactionCategory.CatType catType = transaction.getCategory().getType();

        id = transaction.getId();
        amount = ((double) transaction.getAmount()) / 100;
        timestamp = df.format(transaction.getTimestmp());
        actor_id = transaction.getActor().getId();
        actor_name = transaction.getActor().getName();
        category_name =
                (catType == TransactionCategory.CatType.TRANSFER) ?
                        transaction.getPair().getAccount().getName() :
                        transaction.getCategory().getName();

        category_id = transaction.getCategory().getId();
        comment = transaction.getComment();

        switch (catType) {
            case CORRECTION:
                type = "C";
                break;
            case TRANSFER:
                type = (amount > 0) ? "TI" : "TO";
                break;
            case EXP:
                type = "E";
                break;
            case INC:
                type = "I";
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getActor_id() {
        return actor_id;
    }

    public void setActor_id(int actor_id) {
        this.actor_id = actor_id;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
