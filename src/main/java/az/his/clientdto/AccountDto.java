package az.his.clientdto;

import az.his.persist.Account;

@SuppressWarnings("UnusedDeclaration")
public class AccountDto {
    private String name;
    private int id;
    private long val;
    private String privacy;

    public AccountDto(){
        privacy = "C";
    }

    public AccountDto(Account account) {
        id = account.getId();
        name = account.getName();
        val = account.getValue();

        if(account.isPublic()) privacy = "C";
        else if(account.isHidden()) privacy = "H";
        else privacy = "P";
    }

    public Account toPersist() {
        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setValue(val);
        account.setPublic(privacy.equals("C"));
        account.setHidden(privacy.equals("H"));

        return account;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public long getVal() {
        return val;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVal(long val) {
        this.val = val;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
