package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Singleton

public class CharsetFilter implements Filter {
    private  FilterConfig _filterConfig;
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig;
    }

    public void doFilter(                                       // Метод работы фильтра
                         ServletRequest servletRequest,         // ! не НТТР - параметры
                         ServletResponse servletResponse,       // но реально это НТТР-данные
                         FilterChain filterChain                //  посылание на цепь
    ) throws IOException, ServletException {
        // за нуждой роботы с request/response их желательно перетворить до НТТР-
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        /* кодирование request/response можно установить до первого акта
        * чтение/запись к ним. Фильтр - идеальное место для этого */

        // способ передать данные про работу фильтра - атрибуты запроса
        servletRequest.setAttribute("charset", StandardCharsets.UTF_8.name());

        //  не забыть передать работу до цепи
        filterChain.doFilter(servletRequest, servletResponse);
        // код спизаписанные после цепи будет выполнятся на внешнем ходу
    }

    public void destroy() {
        _filterConfig = null;
    }
}

/*
Фильтры (сервлетные фильтры)
Классы для сетевых задач, роль которых - создание концепции Middleware -
создание цепей обьектов - выполнителей с возможностью вставки новых
обьектов в эту цепь
Цепь(в обработке веб-запроса) имеет прямой и обратный ход, фильтры
имеют возможность работать во всех направлениях
Фильтры, как правило, добавляют до многих маршрутов (часто - до всех),
тогда как сервлеты привязаны к одному или группового маршрута (/user/*)
Задача фильтров - общие действия, как то восстановление кодирование символов,
опдключение к БД, проверка авторизации, и тд
Особенность фильтров еще и в том, что они не разделяются методами запроса

Описание фильтра не включает его в процесс, нужна регистрация
- или в web.xml
- или анотацией
про +/- смотрим в сервлетах, но для фильтров порядок имеет часто значение,
по этому недостаток анотаций с НЕгарантированием порядку есть критичным
*/

/*
ServletContext - область окружения, в котором работает сервлет - основа
визначення адреса, как URL , так и файловых.

http://localhost:8080/JavaWeb_PU121/jsp?text=Hello
getContextPath() -- /JavaWeb_PU121
getRequestURI()  -- /JavaWeb_PU121/jsp
getServletPath() -- /jsp
getPathInfo()    -- null
getProtocol()    -- HTTP/1.1
getScheme()      -- http
getRemoteHost()  -- 0:0:0:0:0:0:0:1
getQueryString() -- text=Hello

Щодо файлових шляхів:
(new File("./")).getAbsolutePath() -- C:\Servers\apache-tomcat-8.5.93\bin\.
   ==> наші коди виконуються через веб-сервер (tomcat), відповідно
       поточна папка "./" є робочою папкою ехе-файла сервера
getServletContext().getRealPath("./") -- ...\repos\JavaWeb-PU121\target\JavaWeb-PU121\
   ==> показує на папку деплою (target) - де розміщені зкомпільовані класи сервлету
аналоги
   -   _filterConfig.getServletContext().getRealPath("./")
   -   request.getServletContext().getRealPath("./")
 */