package az.his.jaxb;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "UserList")
@XmlSeeAlso({JaxUserList.User.class})
public class JaxUserList {
    @XmlAnyElement
    public List<User> users = new ArrayList<User>();

    @XmlRootElement
    public static class User{
        @XmlAttribute
        public int id;

        @XmlAttribute
        public String name;
    }
}
