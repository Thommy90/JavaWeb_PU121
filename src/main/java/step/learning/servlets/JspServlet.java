package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class JspServlet extends HttpServlet {
    private final Logger logger;
    private final HashService hashService;

    @Inject
    public JspServlet(Logger logger, HashService hashService) {
        this.logger = logger;
        this.hashService = hashService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "INFO JSP " + hashService.hash("123"));
       // logger.log(Level.WARNING, "WARNING JSP");

        req.setAttribute("pageName", "jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }
}
