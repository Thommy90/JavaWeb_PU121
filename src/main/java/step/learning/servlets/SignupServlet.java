package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Singleton
public class SignupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageName", "signup" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward( req, resp ) ;
    }
    private static final long serialVersionUID = 1L;

    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if( ! ServletFileUpload.isMultipartContent( req ) ) {
            // if not, we stop here
            resp.getWriter().print("Error: Form must has enctype=multipart/form-data.");
            return;
        }
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        // this path is relative to application's directory
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            List<FileItem> formItems = upload.parseRequest(req);
            StringBuilder st = new StringBuilder("String: ");
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);

                        // saves the file on disk
                        item.write(storeFile);
                        req.setAttribute("message",
                                "Upload has been done successfully!");
                    }
                    else { // робота з полем форми (не файл)
                        st.append(item.getFieldName()).append(" - ").append(item.getString()).append(", ");  //  назва поля

                        //item.getString();  // значення
                        // Д.З. Зібрати усі дані про поля форми у рядок (StringBuilder)
                        // та повернути як відповідть сервера. З боку фронтенда змінити
                        // прийом на r.text() та одержати у консоль ці дані
                    }
                }
                resp.getWriter().print(
                       st
                ) ;
            }
        } catch (Exception ex) {
            req.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }

        SignupFormData formData = new SignupFormData( req ) ;

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        resp.getWriter().print(
                gson.toJson( formData )
        ) ;
    }

    class SignupFormData {
        private String name;
        private String lastname;
        private String email;

        public SignupFormData( HttpServletRequest req ) {
            setName( req.getParameter( "reg-name" ) ) ;
            setLastname( req.getParameter( "reg-lastname" ) ) ;
            setEmail( req.getParameter( "reg-email" ) ) ;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
