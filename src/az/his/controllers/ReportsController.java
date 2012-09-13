package az.his.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/reports")
public class ReportsController {
    @RequestMapping(method = RequestMethod.GET)
    public void reportsPage(HttpServletResponse resp){
        try {
            resp.getOutputStream().println("<h1>Under construction</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
