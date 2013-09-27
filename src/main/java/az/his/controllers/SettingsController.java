package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    public DBUtil dbUtil;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public ModelAndView settingsPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("colorScheme", user.getSysParameter("ui.colorScheme").getVal());

        return new ModelAndView("settings", params);
    }

    @RequestMapping("setColor")
    @Transactional
    public ModelAndView setColor(@RequestParam("new") String newVal, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> params = new HashMap<String, Object>();

        user.setSysParameter("ui.colorScheme", newVal);
        params.put("colorScheme", user.getSysParameter("ui.colorScheme").getVal());

        return new ModelAndView("settings", params);
    }
}
