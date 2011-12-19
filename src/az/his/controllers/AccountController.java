package az.his.controllers;

import az.his.persist.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class AccountController {

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String loginPage(){
        User.getAll();
        return "login";
    }
}
