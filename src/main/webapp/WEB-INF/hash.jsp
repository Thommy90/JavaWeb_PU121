<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String result = (String) request.getAttribute( "result" ) ;
    if ( result == null ) {
        result = "" ;
    }
%>
<div class="container">
    <div class="card-panel grey lighten-5">
        <div class="row">
            <form action="" method="post" class="col s12">
                <div class="row valign-wrapper">
                    <div class="input-field col s6">
                        <input id="input_text"
                               name="input_text"
                               type="text"
                               data-length="10" />
                        <label for="input_text">Введіть рядок:</label>
                    </div>
                    <div class="col s6">
                        <button class="btn waves-effect waves-light red lighten-3 offset-s1"
                                type="submit">Відправити
                                <i class="material-icons right">send</i>
                        </button>
                    </div>
                </div>
                <div class="row valign-wrapper">
                    <div class="input-field col s6">
                        <textarea id="textarea2"
                                  name="textarea2"
                                  class="materialize-textarea"
                                  data-length="120"
                                  readonly><%=result%></textarea>
                        <label for="textarea2">Результат:</label>
                    </div>
                    <div class="col s6">
                        <button class="btn waves-effect waves-light red lighten-3 "
                                type="submit" name="mode" value="download">download
                            <i class="material-icons right">file_download</i>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>