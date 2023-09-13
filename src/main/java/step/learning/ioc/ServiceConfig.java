package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import step.learning.services.db.DbProvider;
import step.learning.services.db.PlanetDbProvider;
import step.learning.services.hash.HashService;
import step.learning.services.hash.KupinaHashService;
import step.learning.services.kdf.HashKdfService;
import step.learning.services.kdf.KdfService;

public class ServiceConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind( HashService.class).to(KupinaHashService.class) ;
        bind (DbProvider.class).to(PlanetDbProvider.class);

        bind( KdfService.class ).to( HashKdfService.class ) ;

        bind( String.class )
                .annotatedWith( Names.named( "DbPrefix" ) )
                .toInstance( "pu121_" ) ;
    }
}
