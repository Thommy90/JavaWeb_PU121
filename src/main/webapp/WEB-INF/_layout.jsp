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
            <li <% if("jsp.jsp".equals(pageName)) { %>class="active"<% } %>> <a href="<%= contextPath %>/jsp" > JSP</a></li>
            <li <% if("aboutServlet.jsp".equals(pageName)) { %>class="active"<% } %>> <a href="<%= contextPath %>/aboutServlet">aboutServlet</a></li>
            <li <% if("url.jsp".equals(pageName)) { %>class="active"<% } %>><a href="<%= contextPath %>/url">About URL</a></li>
            <li <% if("hash.jsp".equals(pageName)) { %>class="active"<% } %>><a href="<%= contextPath %>/hash">Hash</a></li>
            <li> <a class="waves-effect light-blue btn modal-trigger" href="#auth-modal"><span class="material-icons">login</span></a></li>
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
                <input id="authLogin" name="authLogin" type="text" class="validate">
                <label for="authLogin">Логін</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">mode_edit</i>
                <input id="authPassword" name="authPassword" type="text" class="validate">
                <label for="authPassword">Пароль</label>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <div id="signup"  style="color: red;" class="waves-effect"></div>
        <a href="<%= contextPath %>/signup" class="modal-close waves-effect waves-green btn-flat teal lighten-3">Реєстрація</a>
        <a href="#!" class="modal-close waves-effect waves-green btn-flat indigo lighten-3">Забув пароль</a>
        <a class="waves-effect waves-green btn-flat green lighten-3" id="submit">Вхід</a>
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
        var elems = document.querySelectorAll('.modal');
        M.Modal.init(elems, {
            opacity: 0.5,
            dismissible: false
        });
        const signupButton = document.getElementById('submit');
        if( signupButton ) {
            signupButton.addEventListener( 'click', loginClick ) ;
        }
        else {
            console.error( 'signupButton not found' )
        }
    });

    function loginClick() {
        const authLogin = document.getElementById('authLogin');
        if( ! authLogin ) throw "input id='auth-login' not found" ;
        const authPassword = document.getElementById('authPassword');
        if( ! authPassword ) throw "input id='auth-password' not found" ;
     

        const url = "/JavaWeb_PU121/signup?authLogin="+ authLogin.value+"&authPassword="+authPassword.value;
        fetch( url, {
            method: 'PUT'
        }).then( r => r.json()).then(r => document.getElementById('signup').textContent=r.message) ;
    }
</script>
</body>
</html>
