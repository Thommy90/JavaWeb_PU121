package step.learning.ioc;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggerConfig extends AbstractModule {
    @Override
    protected void configure() {
        try( InputStream propertiesStream =
                    this.getClass() // обращаемся к типу
                            .getClassLoader() // достаем загрузчик типов
                            .getResourceAsStream("logging.properties") // обращаемся до ресурса
            // папка resources есть частью проекта, .getResourceAsStream обращается к нему
        ){
            LogManager logManager = LogManager.getLogManager() ;
            logManager.reset();
            logManager.readConfiguration( propertiesStream );
        }
        catch (IOException ex){
            System.err.println( ex.getMessage() );
        }
    }
}
/*
Настройка логгера (для Guice)
- в папку resources создаем/копируем файл logging.properties (он есть частью JDK/JRE
образец можно найти в папках установки JAVA или в интернете
- в классе настройки передаем содержимое этого файла до настроек логгера
- добавляем этот класс до конфигурации Guice
 */