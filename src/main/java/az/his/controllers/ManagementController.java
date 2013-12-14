package az.his.controllers;

import az.his.DBUtil;
import az.his.clientdto.AccountDto;
import az.his.clientdto.GridResponse;
import az.his.persist.Account;
import az.his.persist.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ModelAndView managePage() {
        return new ModelAndView("manage");
    }

    @RequestMapping(value = "/accounts", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @ResponseBody
    public GridResponse<AccountDto> getAccounts() {
        List<Account> accounts = Account.getForUser();

        GridResponse<AccountDto> response = new GridResponse<>();

        for (Account account : accounts) {
            response.addItem(new AccountDto(account));
        }

        return response;
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void saveAccount(@RequestBody AccountDto accData){
        Account acc = accData.toPersist();
        if(!acc.isPublic()) acc.setOwner(User.getCurrentUser());

        dbUtil.saveOrUpdate(acc);
    }
}
