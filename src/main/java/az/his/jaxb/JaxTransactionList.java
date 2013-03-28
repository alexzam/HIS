package az.his.jaxb;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "TransactionList")
@XmlSeeAlso({JaxTransactionList.Transaction.class})
public class JaxTransactionList {
    @XmlAttribute(required = true)
    public int uid;

    @XmlAnyElement(lax = true)
    public List<Transaction> transactions = new ArrayList<Transaction>();

    @XmlRootElement(name = "tr")
    public static class Transaction {
        @XmlAttribute(required = true)
        public int amount;

        @XmlAttribute(required = true)
        public int cat;

        @XmlAttribute(required = true)
        public Date date;
    }
}
