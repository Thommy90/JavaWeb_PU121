package step.learning.db.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.db.dto.User;
import step.learning.services.db.DbProvider;
import step.learning.services.kdf.KdfService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao {
    private final DbProvider dbProvider ;
    private final KdfService kdfService ;
    private final Logger logger ;
    private final String dbPrefix ;
    @Inject
    public UserDao( DbProvider dbProvider, KdfService kdfService, Logger logger,
                    @Named("DbPrefix") String dbPrefix ) {
        this.dbProvider = dbProvider;
        this.kdfService = kdfService;
        this.logger = logger;
        this.dbPrefix = dbPrefix;
    }

    /**
     * CREATE TABLE and INSERT first user
     */
    public void add(User user)
    {
        user.setId(UUID.randomUUID()) ;
        user.setSalt(user.getId().toString().substring(0, 8));
        user.setPasswordDk(kdfService.getDerivedKey( user.getPasswordDk(), user.getSalt()));


        String insertSQL = String.format(
                "INSERT INTO %1$sUsers (id, firstName, lastName, email, phone, `login`, " +
                        "salt, passwordDk, culture, gender) " +
                        "VALUES( '%2$s', '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s', '%9$s', '%10$s'" +
                        ", '%11$s' )",
                dbPrefix, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(),
                user.getLogin(), user.getSalt(), user.getPasswordDk(), user.getCulture(), user.getGender()
        ) ;

        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            statement.executeUpdate( insertSQL ) ;
        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + insertSQL
            ) ;
            throw new RuntimeException(ex);
        }
    }
    public void install() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + dbPrefix +"Users (" +
                "id               CHAR(36)     PRIMARY KEY," +
                "firstName        VARCHAR(50)  NULL," +
                "lastName         VARCHAR(50)  NULL," +
                "email            VARCHAR(128) NOT NULL," +
                "emailConfirmCode CHAR(6)      NULL," +
                "phone            VARCHAR(16)  NULL," +
                "phoneConfirmCode CHAR(6)      NULL," +
                "birthdate        DATETIME     NULL," +
                "avatar           VARCHAR(512) NULL," +
                "`login`          VARCHAR(64)  NOT NULL UNIQUE," +
                "salt             CHAR(8)      NOT NULL," +
                "passwordDk       VARCHAR(64)  NOT NULL COMMENT 'Derived Key (RFC 2898)'," +
                "registerDT       DATETIME     DEFAULT  CURRENT_TIMESTAMP," +
                "lastLoginDT      DATETIME     NULL," +
                "culture          CHAR(5)      NULL COMMENT 'uk-UA'," +
                "gender           VARCHAR(64)  NULL," +
                "banDT            DATETIME     NULL," +
                "deleteDT         DATETIME     NULL," +
                "roleId           CHAR(36)     NULL" +
                ") Engine = InnoDB  DEFAULT CHARSET = utf8" ;

        String id = "2e987203-5145-11ee-8444-966dc5fde598" ;  // UUID.randomUUID().toString() ;
        String salt = id.substring(0, 8) ;
        String defaultPassword = "123" ;
        String passwordDk = kdfService.getDerivedKey( defaultPassword, salt ) ;

        String insertSQL = String.format(
                "INSERT INTO %1$sUsers (id, email, `login`, salt, passwordDk) " +
                "VALUES( '%2$s', 'admin@some.mail.com', 'admin', '%3$s', '%4$s' ) " +
                "ON DUPLICATE KEY UPDATE salt = '%3$s', passwordDk = '%4$s' ",
                dbPrefix, id, salt, passwordDk
        ) ;

        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            statement.executeUpdate( createTableSQL ) ;
            statement.executeUpdate( insertSQL ) ;
        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + createTableSQL + "--" + insertSQL
            ) ;
            throw new RuntimeException(ex);
        }

    }
}
