package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.hash.HashService;
import step.learning.services.hash.KupinaHashService;

public class ServiceConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind( HashService.class).to(KupinaHashService.class) ;
    }
}
