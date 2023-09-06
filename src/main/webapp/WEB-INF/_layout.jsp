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
        </ul>
    </div>
</nav>
<jsp:include page="<%= pageName %>" />

<p>
    Установлено кодирование: <%= request.getAttribute("charset") %>
    <%-- атрибут устанавливается в филтре CharsetFilter --%>
</p>

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
</body>
</html>
