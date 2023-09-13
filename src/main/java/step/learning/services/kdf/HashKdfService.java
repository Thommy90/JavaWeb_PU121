package step.learning.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.hash.HashService;

/**
 * Hash based KDF
 */
@Singleton
public class HashKdfService implements KdfService {
    private final HashService hashService ;
    @Inject
    public HashKdfService(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    public String getDerivedKey( String password, String salt ) {
        return hashService.hash(
                String.format( "%s-%s", password, salt )
        ) ;
    }
}
