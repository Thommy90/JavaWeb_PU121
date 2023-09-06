package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.filters.RegisterFilter;

/*
Конфигурация фильтров для Guice
 */
public class FilterConfig extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(RegisterFilter.class);
        filter("/*").through(CharsetFilter.class);
    }
}
