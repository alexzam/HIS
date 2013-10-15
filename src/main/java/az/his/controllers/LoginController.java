package az.his.controllers;

import az.his.DBUtil;
import az.his.persist.User;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    ApplicationContext appContext;

    @Autowired
    @Qualifier("authMan")
    private AuthenticationManager authManager;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String loginPage(Model model) {
        List<User> users = User.getAll(appContext);
        model.addAttribute("users", users);
        return "login";
    }

    @RequestMapping(params = "mode=in")
    @Transactional(readOnly = true)
    public String loginHandler(@RequestParam("uid") int uid, HttpSession session) {
        String name = String.valueOf(uid);
        String pass = "u" + name;
        UsernamePasswordAuthenticationToken userpass = new UsernamePasswordAuthenticationToken(name, pass);
        Authentication auth = authManager.authenticate(userpass);

        SecurityContextHolder.getContext().setAuthentication(auth);

        Query query = DBUtil.getInstance().getSession().createQuery("from az.his.persist.User where id = ?");
        query.setInteger(0, uid);
        User user = (User) query.uniqueResult();
        session.setAttribute("user", user);

        return "redirect:account";
    }
}
