package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import step.learning.db.dao.UserDao;
import step.learning.db.dao.WebTokenDao;
import step.learning.db.dto.User;
import step.learning.db.dto.WebToken;
import step.learning.services.email.EmailService;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;
import step.learning.services.kdf.KdfService;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final String uploadDir;
    private final KdfService kdfService ;
    private final UserDao userDao;
    private final WebTokenDao webTokenDao;
    private final Logger logger;
    private final EmailService emailService;

    @Inject
    public SignupServlet(FormParseService formParseService, @Named("UploadDir") String uploadDir, KdfService kdfService, UserDao userDao, WebTokenDao webTokenDao, Logger logger, EmailService emailService) {
        this.formParseService = formParseService;
        this.uploadDir = uploadDir;
        this.kdfService = kdfService;
        this.userDao = userDao;
        this.webTokenDao = webTokenDao;
        this.logger = logger;
        this.emailService = emailService;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//метод викликається до того як відбувається розподіл за doXxxx методами,
        //тут є можливість вплинути на цей розподіл
        switch ((req.getMethod().toUpperCase()))
        {

            case "PATCH": this.doPatch(req,resp);
                break;
            default:
                super.service(req,resp);//розподіл за замовчуванням
        }
    }

    protected void doPatch( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if(code==null)
        {
            resp.setStatus(400);
            resp.getWriter().print("Missing required parameter: code");
            return;
        }
        String authHeader = req.getHeader("Authorization");
        if(authHeader==null)
        {
            resp.setStatus(401);
            resp.getWriter().print("Unauthorized");
            return;
        }
        User user = webTokenDao.getSubject(authHeader);
        if(user==null)
        {
            resp.setStatus(403);
            resp.getWriter().print("Forbidden: invalid or expired token");
            return;
        }
        // перевіряємо збіг коду у БД та коду у запиті
        if(userDao.confirmEmailCode(user,code))
        {
            resp.setStatus(202);
            resp.getWriter().print("Code accepted");

        }
        else {
            resp.setStatus(203);
            resp.getWriter().print("Code rejected");

        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("pageName", "signup");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignupFormData formData ;
        ResponseData responseData;
        try {
            formData = new SignupFormData( req ) ;
            User user = formData.toUserDto();
            userDao.add(user);
            //send confirm codes
            Message message = emailService.prepareMessage() ;
            message.setRecipients(  Message.RecipientType.TO, InternetAddress.parse( user.getEmail()) ) ;
            message.setContent(
                    String.format(  "<b>Вітаємо</b> з реєстрацією на <a href='http://localhost:8080/JavaWeb_PU121/ч'> сайті JavaWeb </a>! </br>"+
                                    "Для підтвердження пошти використайте код <b style='font-size:large; color=green'>%s</b>",
                            user.getEmailConfirmCode()),
                    "text/html; charset=UTF-8" );
            emailService.send( message ) ;
            responseData= new ResponseData(200,"Ok");

        }
        catch( Exception ex ) {
            logger.log( Level.SEVERE, ex.getMessage() ) ;
            responseData = new ResponseData(500, "There was an error. Look at server's logs");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        resp.getWriter().print(
                gson.toJson(responseData)
        );
    }

    class ResponseData{
        int statusCode;
        String message;

        public ResponseData() {
        }

        public ResponseData(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Автентифікація - перевірка логіну/паролю
        ResponseData responseData ;
        try {
            /* // При передачі URL-параметрів
            String login = req.getParameter( "auth-login" ) ;
            String password = req.getParameter( "auth-password" ) ;
             */
            /*  // При передачі form-data (див. зміни у MixedFormParseService)
            FormParseResult parseResult = formParseService.parse( req ) ;
            String login = parseResult.getFields().get("auth-login");
            String password = parseResult.getFields().get("auth-password");
             */
            // При передачі JSON
            JsonObject json = JsonParser.parseReader(req.getReader()).getAsJsonObject();
            String login = json.get("auth-login").getAsString() ;
            String password = json.get("auth-password").getAsString() ;

            User user = userDao.authenticate( login, password ) ;
            if( user != null ) {
                // генеруємо токен та повертаємо його у відповідь
                WebToken webToken = webTokenDao.create( user ) ;
                responseData= new ResponseData(
                        200,
                        webToken.toBase64()
                ) ;
            }
            else {
                responseData= new ResponseData( 401, "Unauthorized" ) ;
            }
        }
        catch( Exception ex ) {
            logger.log( Level.SEVERE, ex.getMessage() ) ;
            responseData = new ResponseData(500, "There was an error. Look at server's logs");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        resp.getWriter().print( gson.toJson( responseData ) ) ;
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
        private Date birthdate;
        private String avatar;
        private final transient SimpleDateFormat dateParser =
                new SimpleDateFormat("yyyy-MM-dd") ;

        public User toUserDto(){
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setAvatar(this.getAvatar());
            user.setFirstName(this.getName());
            user.setLastName(this.getLastname());
            user.setLogin(this.getLogin());
            user.setGender(this.getGender());
            user.setCulture(this.getCulture());
            user.setBirthdate(this.getBirthdate());
            user.setPhone(this.getPhone());
            user.setEmail(this.getEmail());
            if( user.getPhone() != null){
                String phoneCode = UUID.randomUUID().toString().substring(0, 6);
                user.setPhoneConfirmCode(phoneCode);
            }
            if( user.getEmail() != null){
                String emailCode = UUID.randomUUID().toString().substring(0, 6);
                user.setEmailConfirmCode(emailCode);
            }
            user.setDeleteDT(null);
            user.setBanDT(null);
            user.setRegisterDT(new Date());
            user.setLastLoginDT(null);

            user.setSalt( user.getId().toString().substring(0, 8) ) ;
            user.setPasswordDk( kdfService.getDerivedKey( this.getPassword(), user.getSalt() ) ) ;
            user.setRoleId(null);
            return user;
        }

        // endregion
        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public void setBirthdate(String birthdate) {
            if( birthdate != null && ! birthdate.isEmpty() ) {
                try {
                    setBirthdate( dateParser.parse( birthdate ) ) ;
                }
                catch( ParseException ex ) {
                    throw new RuntimeException( ex ) ;
                }
            }
            else {
                setBirthdate( (Date) null ) ;
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