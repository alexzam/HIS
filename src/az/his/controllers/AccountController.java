package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/login")
public class AccountController {

    @Resource(name = "dbUtil")
    private DBUtil dbUtil;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String loginPage(Model model){
        List<User> users = User.getAll(dbUtil.getDbManager());
        model.addAttribute("users", users);
        return "login";
    }
}
