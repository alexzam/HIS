<%@ page import="az.his.filters.AuthFilter" %>
<%@ page import="az.his.persist.Account" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%!
    private static String pathRoot = null;
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
%>
<%
    if (pathRoot == null) {
        pathRoot = request.getServletContext().getInitParameter("path.root");
    }

    List<User> usersNotMe = User.getAll();
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

    <link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <script type="text/javascript" src="js/ext/ext-all-debug-w-comments.js"></script>
    <script type="text/javascript">
        Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';
        var transStoreUrl = '<%=pathRoot%>account-data';
        var catStoreUrl = '<%=pathRoot%>trcategory-data';
        var uid = <%=AuthFilter.getUid(session)%>;

        var userRadioOptions = [
            {boxLabel: 'Я', name:'actor', inputValue:'0', checked: true}
            <% for (User user : usersNotMe) { %>
            ,
            {boxLabel: '<%=user.getName()%>', name: 'actor', inputValue: '<%=user.getId()%>'}
            <% } %>
        ];
    </script>

    <script type="text/javascript" src="js/dojo/dojo.js" djConfig="parseOnLoad: true, locale: 'ru'"></script>
    <script type="text/javascript" src="js/account.js"></script>
</head>
<body class="tundra">
<!--div dojoType="dijit.layout.BorderContainer">
    <div dojoType="dijit.layout.ContentPane" region="top" style="height:100px;">
        <div dojoType="dijit.layout.BorderContainer" gutters="false">
            <div dojoType="dijit.layout.ContentPane" region="center">
                <form dojoType="dijit.form.Form" id="frmAddTrans" action="#">
                </form>
            </div>
            <nav dojoType="dijit.layout.ContentPane" region="right" style="text-align:right;">
                <a href="login?mode=out">Выйти</a>
                <br/>
                <span id="account_amount">
                    <%=Account.getCommon().getAmountPrintable()%> р.
                </span>
            </nav>
        </div>
    </div>
    <div dojoType="dijit.layout.BorderContainer" gutters="false" region="right" style="width: 200px;" id="filter_pane">
        <div dojoType="dijit.layout.ContentPane" region="top">
            <h1>Фильтровать</h1>

            <form id="frmFilter" dojoType="dijit.form.Form">
                <table>
                    <tr>
                        <td><label for="filter_datefrom">С</label></td>
                        <td>
                            <input dojotype='dijit.form.DateTextBox' class="input" name="from" id="filter_datefrom"
                                   onchange="account.onFilterChange();"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="filter_dateto">По</label></td>
                        <td>
                            <input dojotype='dijit.form.DateTextBox' class="input" name="to" id="filter_dateto"
                                   onchange="account.onFilterChange();"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="filter_сategory">Категория</label></td>
                        <td><select dojotype="dijit.form.Select" name="cat" id="filter_category" store="catStore"
                                    class="input" value="0" onchange="account.onFilterChange();"></select></td>
                    </tr>
                </table>
            </form>
        </div>
        <div dojoType="dijit.layout.ContentPane" region="center">
            <button dojotype="dijit.form.Button" id="btDelete">Удалить</button>
        </div>
        <div dojoType="dijit.layout.ContentPane" region="bottom">
            <table style="border:0;width:100%">
                <tr>
                    <td>Общие расходы:</td>
                    <td><span id="valTotalExp">0</span></td>
                </tr>
                <tr>
                    <td>На каждого:</td>
                    <td><span id="valEachExp">0</span></td>
                </tr>
                <tr>
                    <td>Мои траты:</td>
                    <td><span id="valPersExp">0</span></td>
                </tr>
                <tr>
                    <td>Мой вклад в Казну:</td>
                    <td><span id="valPersDonation">0</span></td>
                </tr>
                <tr>
                    <td>Всего моих расходов:</td>
                    <td><span id="valPersSpent">0</span></td>
                </tr>
                <tr>
                    <td>Мой баланс:</td>
                    <td><span id="valPersBalance">0</span></td>
                </tr>
            </table>
        </div>
    </div>
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
</div-->
</body>
</html>