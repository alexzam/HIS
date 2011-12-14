package az.his.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class AccountController {

    @RequestMapping(method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }
}
