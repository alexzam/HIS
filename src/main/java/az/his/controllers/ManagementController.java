package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.Account;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @RequestMapping(value = "/accounts", produces = "application/json; charset=UTF-8")
    @Transactional(readOnly = true)
    @ResponseBody
    public String getAccounts() throws JSONException {
        List<Account> accounts = Account.getForUser();

        JSONObject ret = new JSONObject();
        ret.put("identifier", "id");

        JSONArray items = new JSONArray();

        for (Account account : accounts) {
            JSONObject jAcc = new JSONObject();
            jAcc.put("id", account.getId());
            jAcc.put("name", account.getName());
            jAcc.put("val", account.getValue());
            jAcc.put("public", account.isPublic());

            items.put(jAcc);
        }

        ret.put("items", items);

        return ret.toString();
    }
}
