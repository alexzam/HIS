package az.his;

import az.his.ejb.ContentManager;
import az.his.persist.TestEntity;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: AlexZam
 * Date: 28.01.11
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/serv"})
public class TestServlet extends HttpServlet {
    @EJB(name = "java:module/ContMan")
    private ContentManager cm;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TestEntity ent = new TestEntity();
        ent.name = "Haba!";

        cm.persist(ent);

//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("main");
//        EntityManager em = emf.createEntityManager();
//
//
//
//        em.close();
//        emf.close();
        response.getWriter().append("Servlet Works!");
    }
}
