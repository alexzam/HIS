package az.his.jaxb;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CategoryList")
@XmlSeeAlso({JaxCategoryList.Category.class})
public class JaxCategoryList {
    @XmlAnyElement
    public List<Category> cats = new ArrayList<>();

    public void addCategory(int id, String name){
        Category category = new Category(id, name);
        cats.add(category);
    }

    @XmlRootElement
    public static class Category {
        @XmlAttribute
        public int id;

        @XmlAttribute
        public String name;

        public Category() {
        }

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
