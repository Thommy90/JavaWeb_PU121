<%@ page contentType="text/html;charset=UTF-8" %>


<div class="row container">
    <form class="col s12">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">mode_edit</i>
                <textarea id="icon_prefix2" class="materialize-textarea"></textarea>
                <label for="icon_prefix2">Enter text</label>
                Hash string kapina (123): <b><%= request.getAttribute("hashString")%></b>
            </div>
        </div>
    </form>
</div>

