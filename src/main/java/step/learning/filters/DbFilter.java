package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbProvider;

import javax.servlet.*;
import java.io.IOException;

/*
Инициализирует первое подключение, контролирует доступность базы данных
 */
@Singleton
public class DbFilter implements Filter {
    private FilterConfig filterConfig;
    private final DbProvider dbProvider;

    @Inject
    public DbFilter(DbProvider dbProvider) {
        this.dbProvider = dbProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            dbProvider.getConnection();
            chain
                    .doFilter(request, response);
        }
        catch (RuntimeException ignored){
            request
                    .getRequestDispatcher("WEB-INF/no-db.jsp")
                    .forward(request, response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
