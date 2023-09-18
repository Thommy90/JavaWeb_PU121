package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;


@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final String uploadDir;

    @Inject
    public SignupServlet(FormParseService formParseService, @Named("UploadDir") String uploadDir) {
        this.formParseService = formParseService;
        this.uploadDir = uploadDir;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("pageName", "signup");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        SignupFormData formData;
        try {
            formData = new SignupFormData(req);
        } catch (Exception ex) {
            resp.getWriter().print(
                    "There was an error: " + ex.getMessage()
            );
            formData = null;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        resp.getWriter().print(
                gson.toJson(formData)
        );
    }

    class SignupFormData {
        // region form
        private String name;
        private String lastname;
        private String email;
        private String phone;
        private String login;
        private String password;
        private String culture;
        private String gender;
        private LocalDate birthdate;
        private String avatar;

        // endregion
        public LocalDate getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
        }

        public void setBirthdate(String birthdate) {
            if (birthdate != null && !birthdate.equals("")) {
                this.birthdate = LocalDate.parse(birthdate);
            } else {
                this.birthdate = null;
            }
        }

        public SignupFormData(HttpServletRequest req) throws Exception {
            FormParseResult parseResult = formParseService.parse(req);
            Map<String, String> fields = parseResult.getFields();
            setName(fields.get("reg-name"));
            setLastname(fields.get("reg-lastname"));
            setEmail(fields.get("reg-email"));
            setPhone(fields.get("reg-phone"));
            setLogin(fields.get("reg-login"));
            setPassword(fields.get("reg-password"));
            setCulture(fields.get("reg-culture"));
            setGender(fields.get("reg-gender"));
            setBirthdate(fields.get("reg-birthdate"));

            Map<String, FileItem> files = parseResult.getFiles();
            if (files.containsKey("reg-avatar")) {
                setAvatar(files.get("reg-avatar"), req);
            } else {
                setAvatar(null);
            }
        }

        public void setAvatar(FileItem fileItem, HttpServletRequest req) throws Exception {
            String uploadPath = getServletContext().getRealPath("") + File.separator + uploadDir;
            if (fileItem != null && !fileItem.getName().isEmpty()) {
                int lastIndex = fileItem.getName().lastIndexOf(".");
                if (lastIndex != -1) {
                    String format = fileItem.getName().substring(lastIndex + 1);
                    String[] formats = {"jpg", "jpeg", "png", "gif", "bmp"};
                    for (String f : formats) {
                        if (format.equalsIgnoreCase(f)) {
                            String fileName = UUID.randomUUID() + "." + format;
                            File directory = new File(uploadPath);
                            if (directory.exists() && directory.isDirectory()) {
                                File[] files = directory.listFiles();
                                if (files != null) {
                                    for (File file : files) {
                                        if (fileName.equalsIgnoreCase(file.getName())) {
                                            fileName = UUID.randomUUID() + "." + format;
                                        }
                                    }
                                }
                            }
                            File uploadedFile = new File(uploadPath, fileName);
                            fileItem.write(uploadedFile);
                            this.avatar = fileName;
                        }
                    }
                } else {
                    this.avatar = null;
                }
            }
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        // region get-set
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCulture() {
            return culture;
        }

        public void setCulture(String culture) {
            this.culture = culture;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
        // endregion
    }
}