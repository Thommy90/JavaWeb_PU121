package step.learning.services.db;

import com.google.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanetDbProvider implements DbProvider{
    private Connection connection;
    private final Logger logger;
    @Inject
    public PlanetDbProvider(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Connection getConnection() {
        if (connection == null){
            try (InputStream stream =
                         this.getClass()
                                 .getClassLoader()
                                 .getResourceAsStream("planetdb.properties")) {
                if (stream == null){
                    logger.log(Level.SEVERE, "Resource - planetdb.properties not found");
                    throw new RuntimeException("Connection failed");
                }
                ByteArrayOutputStream arr = new ByteArrayOutputStream();
                byte[] buf = new byte[2048];
                int len;
                while ((len = stream.read(buf) ) > 0){
                    arr.write(buf, 0, len);
                }
                String properties = arr.toString(StandardCharsets.UTF_8.name());
                String[] lines = properties.split("[\r\n]+");
                Map<String, String> dbProperties = new HashMap<>();
                for (String line: lines){
                    String[] pair = line.split("=", 2);
                    dbProperties.put( pair[0], pair[1]);
                }
               Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        dbProperties.get("url"),
                        dbProperties.get("user"),
                        dbProperties.get("password")
                );
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw new RuntimeException("Connection failed");
            }
        }
        return connection;
    }
}
