package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContextEvent;

public class IocListener extends GuiceServletContextListener {
//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        super.contextInitialized(servletContextEvent);
//    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new FilterConfig(),
                new ServletConfig(),
                new LoggerConfig(),
                new ServiceConfig()
        );
    }
}
/*
Инверсия управления в веб-проектах
(добавление Guice к веб-проектам)

1. Зависимости (пакеты): pom.xml
-  https://mvnrepository.com/artifact/com.google.inject/guice
    (проверенно на версии 6 0 0)
- https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet
    (берем 6 0 0 для совмещения с javax)
2. Настройки web.xml
- убираем с web.xml все настроки сервлетов и фильтров, (они будут
перенесены в конфигурацию Guice)
- добавляем фильтр Guice та наша прослушка событий создания контексту (старт веб работы)
(полная аналогия точки main) -- класс с этого файла
3. Создаем конфиг-классы (один или несколько), зазначаемо фильтры и сервлеты.
!! для всех классов фильртов и сервлетов следует зазначить @Singleton
и прибрать @WebServlet/Filter
 */