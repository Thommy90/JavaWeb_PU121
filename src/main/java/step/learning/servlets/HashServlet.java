package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class HashServlet extends HttpServlet {
    private final HashService hashService;
    @Inject
    public HashServlet(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String result = (String) session.getAttribute("hash-result");
        if (result != null) {
            req.setAttribute("result", result);
            session.removeAttribute("hash-result");
        }

        req.setAttribute("pageName", "hash");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String inputeText = req.getParameter("input_text");
        String inputeTextArea = req.getParameter("textarea2");
        if(inputeText ==null){
            resp.setStatus(406);
            resp.getWriter().print("Required 'input_text' parameter");
            return;
        }
        String result = String.format(
                "hash ('%s') = %s",
                inputeText,
                hashService.hash(inputeText)
        );
        if ("download".equals(req.getParameter("mode"))){
            String str = inputeTextArea.substring(inputeTextArea.lastIndexOf(" = "));
            resp.setHeader("Content-Type", "application/octet-stream");
            resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.txt\"",
                    str.substring(3, 11)));
            resp.getWriter().print(inputeTextArea);
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute("hash-result", result);

       resp.sendRedirect( req.getRequestURI());
    }
}

/*
Работа с формами, передача данных
Параметры с запроса берутся методом req.getParameter(name)
название параметра как в поле формы (<input name="input_text"...)
если нужен полный перелик параметров , то наявные методы
      req.getParameterNames() - имена параметров
      req.getParameterMap() - имена и значения, собирает в массив
параметры с одинаковыми именами

1. Get
методом Get нельзя передавать тело запроса, значит, все параметры ограничены query-типом
(url-параметры)
"+" - простота, возможность сделать посылку с данными
"-" - просмотр данных что передаются, ограничения на большие обьемы данных

2. Post
этим методом можно передавать как URL - параметры , так и тело запроса
req.getParameter(name) проводить поиск в обоих местах, при доступности двух имен - берется первое
"+" - возможность тела (больших обьемов данных), прихованнисть данных (от случайного просмотра)
"-" - особенность при обновлении страницы, построенной методом POST , браузер выдает сообщение
и пробует повторно передать данные
 */