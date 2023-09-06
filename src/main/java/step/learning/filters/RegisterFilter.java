package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Singleton

public class RegisterFilter implements Filter {
    private FileWriter writer;
    private  FilterConfig _filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this._filterConfig = filterConfig;
        try {
            writer = new FileWriter("journal.txt", true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = sdf.format(new Date());
    String method = request.getMethod();
    String url = request.getRequestURL().toString();
    writer.write("Date: " + date + " Method: " + method + " url:" + url + '\n');
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        _filterConfig = null;
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}