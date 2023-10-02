<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath() ;  // база сайту - домашнє посилання
    String pageName =           // Вилучаємо значення атрибуту (закладеного у сервлеті)
            (String)            // оскільки значення Object, необхідне пряме перетворення
            request             // об'єкт request доступний у всіх JSP незалежно від сервлетів
            .getAttribute(      //
                "pageName")     // збіг імен зі змінною - не вимагається
           + ".jsp" ;           // Параметри можна модифікувати
%>
<html>
<head>
    <meta charset="utf-8" />
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"  media="screen,projection"/>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="<%= contextPath %>/css/style.css" />
</head>
<body>
<nav>
    <div class="nav-wrapper light-blue lighten-1">
        <a href="<%= contextPath %>" class="brand-logo">Logo(Home)</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li><a href="#front">Front</a></li>
            <li <% if("jsp.jsp".equals(pageName)) { %>class="active"<% } %>> <a href="<%= contextPath %>/jsp" > JSP</a></li>
            <li <% if("aboutServlet.jsp".equals(pageName)) { %>class="active"<% } %>> <a href="<%= contextPath %>/aboutServlet">aboutServlet</a></li>
            <li <% if("url.jsp".equals(pageName)) { %>class="active"<% } %>><a href="<%= contextPath %>/url">About URL</a></li>
            <li <% if("hash.jsp".equals(pageName)) { %>class="active"<% } %>><a href="<%= contextPath %>/hash">Hash</a></li>
            <li <% if ("servlet.jsp".equals(pageName)) { %> class="active" <% } %> >
                <a href="<%=contextPath%>/mail">Mail</a>
            </li>
            <li> <a class="waves-effect light-blue btn modal-trigger" href="#auth-modal" id="sub"><span class="material-icons">login</span></a></li>
            <li> <a href="#!" id="exit" class=" light-blue btn modal-trigger">Exit</a></li>
            <li>
                <div id="user-avatar" class="avatar-container" style="width: 50px; margin-top:7px; height: 50px; border-radius: 50%; overflow: hidden;">

                </div>
            </li>
        </ul>
    </div>
</nav>

<jsp:include page="<%= pageName %>" />

<%-- Materialize Modal (Auth block) --%>
<!-- Modal Structure -->
<div id="auth-modal" class="modal">
    <div class="modal-content">
        <h4>Автентифікація</h4>
        <div class="row valign-wrapper">
            <div class="input-field col s6">
                <i class="material-icons prefix">account_circle</i>
                <input id="auth-login" name="auth-login" type="text" class="validate">
                <label for="auth-login">Логін</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">mode_edit</i>
                <input id="auth-password" name="auth-password" type="text" class="validate">
                <label for="auth-password">Пароль</label>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <div id="result"  style="color: red;" class="waves-effect"></div>
        <a href="<%= contextPath %>/signup" class="modal-close waves-effect waves-green btn-flat teal lighten-3">Реєстрація</a>
        <a href="#!" class="modal-close waves-effect waves-green btn-flat indigo lighten-3">Забув пароль</a>
        <a class="waves-effect waves-green btn-flat green lighten-3" id="sign-button">Вхід</a>

    </div>

</div>

<footer class="page-footer light-blue lighten-1">
    <div class="footer-copyright">
        <div class="container">
            <div class="grey-text text-lighten-4 right">
                © 2023 JavaWEB-PU121
            </div>
        </div>
    </div>
</footer>
<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const elems = document.querySelectorAll('.modal');
        M.Modal.init(elems, {
            opacity: 0.5
        } ) ;
        //initModalButtons() ;
        const token = window.localStorage.getItem('token');
        const submitBtn=document.getElementById("sub")
        const exitBtn=document.getElementById("exit")
        exitBtn.addEventListener( 'click', exitClick ) ;
        if(token)
            submitBtn.style.display="none";
        else
            exitBtn.style.display="none";



        const signupButton = document.getElementById('sign-button');

        if( signupButton ) {
            signupButton.addEventListener( 'click', loginClick ) ;
        }
        else {
            console.error( 'signupButton not found' )
        }
        window.addEventListener('hashchange', frontRouter) ;
        frontRouter() ;
    });
    function exitClick()
    {
        const submitBtn=document.getElementById("sub")
        const exitBtn=document.getElementById("exit")
        exitBtn.style.display="none";
        submitBtn.style.display="block";
        window.localStorage.clear();
        window.location.href="<%=contextPath%>/";
    }
    function frontRouter() {
        switch( location.hash ) {
            case '#front':
                loadFrontPage();
                break;
            default:
        }
    }

    function loadFrontPage() {
        const token = window.localStorage.getItem('token');
        if(!token){
            alert("Для этой страницы нужна авторизация");
            window.location.href ="<%= contextPath %>/";
            return;
        }
        try {
            let data = JSON.parse(atob(token))
        }catch (ex){
            alert("Токен недействителен");
            window.location.href ="<%= contextPath %>/";
            localStorage.removeItem('token');
            return;
        }
        const tokenParse = JSON.parse(atob(localStorage.getItem('token')));
        const currentDate = new Date();
        const tokenDate = new Date(tokenParse.exp);
        if (currentDate.getTime() > tokenDate.getTime()) {
            alert("Нужно перезалогиниться (токен устарел)");
            localStorage.removeItem('token');
            return;
        }
        console.log("дата сейчас: " + currentDate)
        console.log(" - дата окончания токена: " + tokenDate);
        console.log("проверка пройдена!");
        const headers = (token == null) ? {} : {
            'Authorization': `Bearer ${token}`
        }
        fetch('<%= contextPath %>/front', {
            method: 'GET',
            headers: headers
        }).then(r => r.json())
            .then(j=>{
                    if(typeof j.login!='undefined')
                    {
                        const userAvatar=document.getElementById("user-avatar");

                        if(!userAvatar)throw "user-avatar not found";
                        if(j.avatar!=null && j.avatar!="undefined")
                        {
                            let i = j.avatar.lastIndexOf('\\');
                            if(i>-1)
                            {
                                j.avatar=j.avatar.substring(i+1);
                            }
                        }
                        else
                        {
                            j.avatar="default.png"
                        }
                        userAvatar.innerHTML=`<img style="max-height:50px; border-radius:15px; margin-right:2%;" src="<%=contextPath%>/upload/${j.avatar}" />`
// ---------------------- CONFIRM EMAIL --------------------------
                        if(typeof j.emailConfirmCode=='string'&& j.emailConfirmCode.length > 0 ) {  // є код -- пошта не підтверджена
                            const confirmDiv = document.getElementById("confirm-email");
                            if( ! confirmDiv ) throw "confirm-email not found" ;
                            confirmDiv.innerHTML = `Ваша пошта не підтверджена, введіть код з е-листа
    <div class="input-field inline"><input id='email-code'/></div><button onclick='confirmCodeClick()'>Підтвердити</button>` ;
                            confirmDiv.style.border = "1px solid maroon" ;
                            confirmDiv.style.padding = "5px 10px" ;
                        }
                    }
                    console.log(j)
            });
    }

    // function initModalButtons() {
    //     const signButton = document.getElementById('sign-button');
    //     if( signButton ) {
    //         signButton.addEventListener( 'click', loginClick ) ;
    //     }
    //     else {
    //         console.error( '#sign-button not found' );
    //     }
    // }
    function confirmCodeClick() {
        const emailCodeInput = document.getElementById("email-code");
        if( ! emailCodeInput ) throw "email-code not found" ;
        fetch('<%=contextPath%>/signup?code='+emailCodeInput.value,{
            method:"PATCH",
            headers:{ 'Authorization':`Bearer `+ window.localStorage.getItem('token')}
        }).then(r=>{
            if(r.status==202)
            {
                alert("Пошту підтверджено");
                window.location.reload();//оновлюємо сторінку - має зникнути поле підтвердження
            }
            else
            {
                r.text().then(alert)//передаемо в alert результат r.text()
            }
        })
        console.log( emailCodeInput.value ) ;
    }
    function loginClick() {
        const loginInput = document.getElementById('auth-login');
        if( ! loginInput ) throw "input id='auth-login' not found" ;
        const passwordInput = document.getElementById('auth-password');
        if( ! passwordInput ) throw "input id='auth-password' not found" ;

        const authLogin = loginInput.value.trim() ;
        if( authLogin.length < 2 ) {
            alert( "Логін занадто короткий або не введений!" ) ;
            return ;
        }
        const authPassword = passwordInput.value ;
        if( authPassword.length < 2 ) {
            alert( "Пароль занадто короткий або не введений" ) ;
            return ;
        }
        // const url = `<%= contextPath %>/signup?auth-login=${authLogin}&auth-password=${authPassword}`;
        const url = `<%=contextPath%>/signup` ;
        // let formData = new FormData();
        // formData.append('auth-login',authLogin );
        // formData.append('auth-password',authPassword );
        // fetch(url, { method: 'PUT', body: formData })
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'auth-login': authLogin,
                'auth-password': authPassword
            })
        })
            .then(response => {
                return response.json(); // Парсимо відповідь як JSON
            })
            .then(data => {
// data буде об'єктом, який містить поля statusCode та message
                console.log(data);
                if(data.statusCode==200)
                {
                    //декодуємо токен, дізнаємось дати
                    let token = JSON.parse(atob(data.message))
                    window.localStorage.setItem('token',data.message);
                    const submitBtn=document.getElementById("sub")
                    const exitBtn=document.getElementById("exit")
                    exitBtn.style.display="block";
                    submitBtn.style.display="none";
                    loginInput.value="";
                    passwordInput.value="";
                    var instance = M.Modal.getInstance(document.getElementById("auth-modal"));
                    instance.close();
                    location.reload();
                }
                else
                {  const result = document.getElementById('result');
                    result.textContent=data.message;}



            })
            .catch(error => {
                console.error('Fetch Error:', error);
            });
    }
</script>
</body>
</html>
