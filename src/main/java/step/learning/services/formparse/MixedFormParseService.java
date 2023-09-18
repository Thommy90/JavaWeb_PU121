package step.learning.services.formparse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for both multipart and urlencoded forms
 */
@Singleton
public class MixedFormParseService implements FormParseService{
    private final String uploadDir;
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    private final ServletFileUpload fileUpload;
    @Inject
    public MixedFormParseService(@Named("UploadDir") String uploadDir) {
        this.uploadDir = uploadDir;
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        fileUpload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        fileUpload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        fileUpload.setSizeMax(MAX_REQUEST_SIZE);


    }

    @Override
    public FormParseResult parse(HttpServletRequest request) {
//        // constructs the directory path to store upload file
//        // this path is relative to application's directory
//        String uploadPath = request.getServletContext().getRealPath("")
//                + File.separator + uploadDir;
//
//        // creates the directory if it does not exist
//        File uploadDir = new File(uploadPath);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
        // готовим коллекцию для результата
        Map<String, String> fields = new HashMap<>();
        Map<String, FileItem> files = new HashMap<>();
        
        //разделяем работу в зависимости от типа запроса (multipart/urlencoder)
        if( ServletFileUpload.isMultipartContent( request ) ) { // multipart
            try {
                fileUpload   // використовуючи створений у конструкторі парсер
                        .parseRequest( request )  // розбираємо запит
                        .forEach( fileItem -> {   // і перевіряємо кожну частину (part of multipart)
                            if( fileItem.isFormField() ) {   // якщо це поле форми -
                                try {
                                    fields.put(                  // то додаємо його до колекції  fields
                                            fileItem.getFieldName(),
                                            fileItem.getString("UTF-8")
                                    );
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else {  // інакше - це файлова частина
                                files.put(   // додаємо відомості про неї до колекції files
                                        fileItem.getFieldName(),
                                        fileItem
                                );
                            }
                        });
            }
            catch( FileUploadException ex ) {
                throw new RuntimeException( ex ) ;
            }
        }
        else{ //urlencoder
            // пользуемся стандартными способами ServletApi
            Enumeration<String> parameterNames = request.getParameterNames();
            //перебираем имена всех параметров запроса и перекладываем их в коллекцию
            while ( parameterNames.hasMoreElements()){
                String name = parameterNames.nextElement();
                fields.put(name, request.getParameter(name));
            }
        }

        return new FormParseResult() {
            @Override
            public Map<String, String> getFields() {
                return fields;
            }

            @Override
            public Map<String, FileItem> getFiles() {
                return files;
            }
        };
    }
}
