package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.db.dao.UserDao;
import step.learning.db.dao.WebTokenDao;
import step.learning.db.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Singleton
public class InstallServlet extends HttpServlet {
    @Inject
    private UserDao userDao ;
    @Inject
    private WebTokenDao webTokenDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            webTokenDao.install();
            userDao.install() ;
           // resp.getWriter().print( "Install OK" ) ;
            User user = new User();
            user.setFirstName("Kostya");
            user.setLastName("Tom");
            user.setEmail("test@gmail.com");
            user.setPhone("0937777777");
           // user.setBirthdate(new Date(1990 , 06, 29));
            user.setLogin("Tom");
            user.setPasswordDk("123");
            user.setCulture("En");
            user.setGender("male");
            userDao.add(user);
            resp.getWriter().print( "Insert OK" ) ;
        }
        catch( RuntimeException ex ) {
            resp.getWriter().print( "Install Error. Look at logs" ) ;
        }
    }
}
