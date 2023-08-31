<%--
  Created by IntelliJ IDEA.
  User: thommy
  Date: 29.08.2023
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<jsp:include page="nav.jsp"/>
<div class="container">
<h1>Основы JSP</h1>
<p>
    <b>JSP</b> - Java server pages - технология веб разработки, аналогичкая ASP или PHP
</p>
<p>
    Которые возможности обычно дают до HTML способы препроцессингу?<br />
    - сменные <br />
    - вычисления <br />
    - условные операторы (умовная верстка) <br />
    - цикловые конструкции <br />
    - композиция (подключение отдельных файлов)
</p>
    <p>
        Код Java вставляется в выборочное место JSP-файлу с помощью конструкции
        &lt;% %&gt;
        <%
            int x = 10;
            String str = "Hello" ;
        %>
        Вывод (ВСтавка в HTML) использует конструкцию       &lt;%= %&gt;
        x = <%= x %> , str + 'World' = <%= str + " World"%>
    </p>
    <p>
        Умовные конструкции создаются следующим образом <br />
        &lt;% if (condition) {%&gt;<br />
        &nbsp;Тело условного блока (как HTML, так и вставки кода) <br/>
        &lt;% } %&gt;<br/>
        <% if (x < 10) {%>
            <b> x меньше 10 </b>
        <% } else {%>
        <i> x больше или равно 10 </i>
        <% } %>
    </p>
    <p>
       Цикловая конструкция<br />
        &lt;% цикл() { %&gt;<br />
        &nbsp;тело цикла <br />
        &lt;% } %&gt;<br />
        <% for (int i = 0; i < 10; i++) { %>
         <b><%= i + 1 %></b>.  <i>index: <%= i %></i><br/>
        <% } %>
    </p>
</div>
<div>
    <% String[] DaysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}; %>
    <h2>Days of the week</h2>
    <table class="highlight">
        <thead>
        <tr>
            <th style="text-align: center">Day</th>
            <th>Title</th>
        </tr>
        </thead>
        <tbody>
    <% for (int i = 0; i < DaysOfWeek.length; i++) { %>
    <tr><td style="text-align: center"><b><%= i + 1 %></b></td> <td><i><%= DaysOfWeek[i] %></i></td></tr>
    <% } %>
        </tbody>
    </table>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
