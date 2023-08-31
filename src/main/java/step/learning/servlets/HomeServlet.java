package step.learning.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {       // Название класса любое
    @Override
    protected void doGet(                            // название метода только так
                                                     HttpServletRequest request,              // реквест - обьект что дается веб-сервером
                                                     HttpServletResponse response)            // респонс - то, что будет отправлено как ответ
            throws ServletException, IOException {
        request.getRequestDispatcher(                // делаем внутренний редирект - передаем работу
                        "WEB-INF/index.jsp")                         // до другого обрабщика  - index.jsp
                .forward(request, response);
    }
}


/*
Servlets - специальные классы Java для сетевых задач
Работа с сервлетами требует установки servlet-api
Для веб-разработки чаще берется предок HttpServlet
HttpServlet - можно считать аналогом контроллера в версии API

После создания сервлет нужно зарегистрировать (Есть два способа (без IoC)
- через настройку сервера web.xml
- с помощью анотаций (servlet-api 3 и выше)

через web.xml
+ централизованные декларации - все в одном месте
+ гарантированный порядок деклараций
+ совмещен с более старыми технологиями
- более длинные инструкции
  <servlet>
    <servlet-name>Home</servlet-name>
    <servlet-class>step.learning.servlets.HomeServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Home</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
 */