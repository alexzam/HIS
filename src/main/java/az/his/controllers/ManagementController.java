package az.his.controllers;

import az.his.DBUtil;
import az.his.clientdto.AccountDto;
import az.his.clientdto.GridResponse;
import az.his.persist.Account;
import org.json.JSONException;
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
    public ModelAndView managePage() {
        return new ModelAndView("manage");
    }

    @RequestMapping(value = "/accounts", produces = "application/json; charset=UTF-8")
    @Transactional(readOnly = true)
    @ResponseBody
    public GridResponse<AccountDto> getAccounts() throws JSONException {
        List<Account> accounts = Account.getForUser();

        GridResponse<AccountDto> response = new GridResponse<>();

        for (Account account : accounts) {
            response.addItem(new AccountDto(account));
        }

        return response;
    }

}
