<%@ page contentType="text/html;charset=UTF-8" %>
<div class="container flow-text" style="font-family: 'New Peninim MT'">
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
<h4 style="text-align: center">про URL</h4>
<p>URL состоит из различных частей, некоторые из которых являются обязательными,
    а некоторые - факультативными. Рассмотрим наиболее важные части на примере:</p>

<p> <span class=" indigo lighten-4">http://www.example.com:80/path/to/myfile.html?key1=value1&key2=value2#SomewhereInTheDocument</span></p>
<img style="width: 100%" src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-all.png">
    </div>
<br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
    <h4  style="text-align: center">Protocol</h4>
    <p><span class=" indigo lighten-4">http://</span> это протокол. Он отображает, какой протокол браузер должен использовать.
    Обычно это HTTP-протокол или его безопасная версия - HTTPS.
    Интернет требует эти 2 протокола, но браузеры часто могут использовать и другие протоколы,
    например mailto: (чтобы открыть почтовый клиент) или ftp: для запуска передачи файлов,
    так что не стоит удивляться, если вы вдруг увидите другие протоколы.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-protocol@x2_update.png">
</div>
    <br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
        <h4 style="text-align: center">Domaine Name</h4>
    <p> <span class=" indigo lighten-4">www.example.com</span> это доменное имя. Оно означает, какой веб-сервер должен быть запрошен.
    В качестве альтернативы может быть использован и IP-адрес,
    но это делается редко, поскольку запоминать IP сложнее,
    и это не популярно в интернете.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-authority.png">
    </div>
    <br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
    <h4 style="text-align: center">Port</h4>
    <p><span class=" indigo lighten-4">:80</span> это порт. Он отображает технический параметр, используемый для доступа к ресурсам на веб-сервере.
    Обычно подразумевается, что веб-сервер использует стандартные порты HTTP-протокола
    (80 для HTTP и 443 для HTTPS) для доступа к своим ресурсам.
    В любом случае, порт - это факультативная составная часть URL.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-authority.png">
    </div>
    <br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
        <h4 style="text-align: center">Path to the file</h4>
    <p><span class=" indigo lighten-4">/path/to/myfile.html</span> это адрес ресурса на веб-сервере.
    В прошлом, адрес отображал местоположение реального файла в реальной директории на веб-сервере.
    В наши дни это чаще всего абстракция, позволяющая обрабатывать адреса и
    отображать тот или иной контент из баз данных.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-path@x2.png">
    </div>
    <br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
        <h4 style="text-align: center">Parameters</h4>
    <p><span class=" indigo lighten-4">?key1=value1&key2=value2</span> это дополнительные параметры, которые браузер сообщает веб-серверу.
    Эти параметры - список пар ключ/значение, которые разделены символом &.
    Веб-сервер может использовать эти параметры для исполнения дополнительных команд перед тем
    как отдать ресурс. Каждый веб-сервер имеет свои собственные правила обработки этих параметров
    и узнать их можно, только спросив владельца сервера.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-parameters@x2.png">
    </div>
    <br/>
    <div class="z-depth-5" style="margin: 15px; padding: 15px">
        <h4 style="text-align: center">Anchor</h4>
    <p><span class=" indigo lighten-4">#SomewhereInTheDocument</span> это якорь на другую часть того же самого ресурса.
    Якорь представляет собой вид "закладки" внутри ресурса, которая переадресовывает браузер на
    "заложенную" часть ресурса. В HTML-документе, например, браузер может переместиться в точку,
    где установлен якорь; в видео- или аудио-документе браузер может перейти к времени,
    на которое ссылается якорь. Важно отметить, что часть URL после #,
    которая также известна как идентификатор фрагмента,
    никогда не посылается на сервер вместе с запросом.</p>
<img src="https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_URL/mdn-url-anchor@x2.png">
</div>
</div>
<br/>