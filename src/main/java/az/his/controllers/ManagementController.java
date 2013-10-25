package az.his.controllers;

import az.his.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manage")
public class ManagementController {
    @Autowired
    public DBUtil dbUtil;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public ModelAndView managePage(){
        return new ModelAndView("manage");
    }
}
