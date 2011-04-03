<%@ page import="az.his.ejb.ContentManager" %>
<%@ page import="az.his.filters.AuthFilter" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%!
    private static String pathRoot = null;
    private static ContentManager cm;
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    static {
        try {
            cm = (ContentManager) (new InitialContext()).lookup("java:module/ContMan");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
%>
<%
    if (pathRoot == null) {
        pathRoot = request.getServletContext().getInitParameter("path.root");
    }

    List<User> usersNotMe = cm.getAllUsers();
    for (User user : usersNotMe) {
        if (user.getId() == AuthFilter.getUid(session)) {
            usersNotMe.remove(user);
            break;
        }
    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Казна</title>
    <link rel="stylesheet" href="css/main.css"/>
    <link rel="stylesheet" href="js/dijit/themes/tundra/tundra.css">
    <link rel="stylesheet" href="js/dojox/grid/resources/Grid.css"/>
    <link rel="stylesheet" href="js/dojox/grid/resources/tundraGrid.css"/>
    <script type="text/javascript" src="js/dojo/dojo.js" djConfig="parseOnLoad: true, locale: 'ru'"></script>
    <script type="text/javascript">
        dojo.require("dijit.layout.BorderContainer");
        dojo.require("dijit.layout.ContentPane");
        dojo.require("dojox.grid.DataGrid");
        dojo.require("dojox.grid.cells.dijit");
        dojo.require("dojo.data.ItemFileReadStore");
        dojo.require("dojo.data.ItemFileWriteStore");
        dojo.require("dijit.form.Form");
        dojo.require("dijit.form.RadioButton");
        dojo.require("dijit.form.DateTextBox");
        dojo.require("dijit.form.CurrencyTextBox");
        dojo.require("dijit.form.ComboBox");
        dojo.require("dijit.form.SimpleTextarea");
        dojo.require("dijit.form.Button");

        var transStoreUrl = '<%=pathRoot%>account-data';
        var catStoreUrl = '<%=pathRoot%>trcategory-data?type=';
        var uid = <%=AuthFilter.getUid(session)%>;
    </script>
    <script type="text/javascript" src="js/account.js"></script>
</head>
<body class="tundra">
<div dojoType="dijit.layout.BorderContainer">
    <div dojoType="dijit.layout.ContentPane" region="top" style="height:100px;">
        <div dojoType="dijit.layout.BorderContainer" gutters="false">
            <div dojoType="dijit.layout.ContentPane" region="center">
                <form dojoType="dijit.form.Form" id="frmAddTrans" action="#">
                    <div class="form_column">
                        <h1>Кто</h1>
                        <input type="radio" dojotype="dijit.form.RadioButton" checked="true" id="rbActorMe" name="actor"
                               value="0"/>
                        <label for="rbActorMe">Я</label>
                        <% for (User user : usersNotMe) {
                            int uid = user.getId();
                        %>
                        <br/>
                        <input type="radio" dojotype="dijit.form.RadioButton" name="actor" id="rbActor<%=uid%>"
                               value="<%=uid%>"/>
                        <label for="rbActor<%=uid%>"><%=user.getName()%>
                        </label>
                        <% } %>
                    </div>
                    <div class="form_column">
                        <h1>Когда</h1>
                        <input dojotype='dijit.form.DateTextBox' class="date" required="true" name="date"
                               constraints="{max:'<%=dateFormatter.format(new Date())%>'}"
                               rangeMessage="Не позже, чем сегодня" id="tbAddDate">
                    </div>
                    <div class="form_column">
                        <h1>Сколько</h1>
                        <input dojotype="dijit.form.CurrencyTextBox" currency="RUB" class="sum" required="true"
                               name="amount" constraints="{min:0.01}" rangeMessage="Не менее копейки" id="tbAddAmount"/>
                        <br/>
                        <input type="radio" dojotype="dijit.form.RadioButton" checked="true" id="rbTypeExp" name="type"
                               value="e"/>
                        <label for="rbTypeExp">Расход</label>
                        <input type="radio" dojotype="dijit.form.RadioButton" id="rbTypeInc" name="type"
                               value="i"/>
                        <label for="rbTypeInc">Приход</label>
                    </div>
                    <div class="form_column">
                        <h1>Категория</h1>
                        <select dojotype="dijit.form.ComboBox" name="cat" id="cbCategory" store="catStore"
                                required="true"></select>
                    </div>
                    <div class="form_column">
                        <h1>Комментарий</h1>
                        <textarea dojotype="dijit.form.SimpleTextarea" trim="true" rows="3" name="comment"
                                  id="taAddComment">
                        </textarea>
                    </div>
                    <div class="form_column">
                        <button dojotype="dijit.form.Button" onclick="account.addSubmit();">Добавить</button>
                    </div>
                </form>
            </div>
            <nav dojoType="dijit.layout.ContentPane" region="right">
                <a href="login?mode=out">Logout</a>
                <br/>
                <span id="account_amount"><%=cm.getAccountAmountPrintable(1)%> р.</span>
            </nav>
        </div>
    </div>
    <div dojoType="dijit.layout.ContentPane" region="right" style="width: 200px;">Filters will be here</div>
    <div dojoType="dijit.layout.ContentPane" region="center">
        <table dojoType="dojox.grid.DataGrid" store="transStore" id="tabTrans" query="{id:'*'}"
               onResizeColumn="account.onTableColResize();">
            <thead>
            <tr>
                <th field="timestamp" width="200px" cellType="dojox.grid.cells.DateTextBox" editable="true">Когда</th>
                <th field="actor_name" width="200px">Кто</th>
                <th field="amount" width="200px" editable="true">Сколько</th>
                <th field="category_name" width="200px">Категория</th>
                <th field="comment" width="100%" editable="true">Комментарий</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>