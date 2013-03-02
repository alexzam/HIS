package az.his.controllers;

import az.his.jaxb.JaxCategoryList;
import az.his.jaxb.JaxUserList;
import az.his.persist.TransactionCategory;
import az.his.persist.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {
    @RequestMapping("/users")
    @ResponseBody
    public JaxUserList getUsers() {
        List<User> users = User.getAll();

        JaxUserList jaxUserList = new JaxUserList();
        for (User dbUser : users) {
            JaxUserList.User user = new JaxUserList.User();
            user.id = dbUser.getId();
            user.name = dbUser.getName();
            jaxUserList.users.add(user);
        }

        return jaxUserList;
    }

    @RequestMapping("/cats")
    @ResponseBody
    public JaxCategoryList getCategories(@RequestParam("uid") int uid) {
        List<TransactionCategory> cats = TransactionCategory.getAll();
        JaxCategoryList catList = new JaxCategoryList();

        for (TransactionCategory cat : cats) {
            catList.addCategory(cat.getId(), cat.getName());
        }

        return catList;
    }
}
