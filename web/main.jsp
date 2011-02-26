<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private static String pathRoot = null;
%>
<%
    if (pathRoot == null) {
        pathRoot = request.getServletContext().getInitParameter("path.root");
    }
%>
<html>
<head>
    <title>Казна</title>
    <link rel="stylesheet" href="css/main.css"/>
    <link rel="stylesheet" href="js/dijit/themes/tundra/tundra.css">
    <link rel="stylesheet" href="js/dojox/grid/resources/Grid.css"/>
    <link rel="stylesheet" href="js/dojox/grid/resources/tundraGrid.css"/>
    <script type="text/javascript" src="js/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
    <script type="text/javascript">
        dojo.require("dijit.layout.BorderContainer");
        dojo.require("dijit.layout.ContentPane");
        dojo.require("dojox.grid.DataGrid");
        dojo.require("dojo.data.ItemFileReadStore");

        var fileStoreUrl = '<%=pathRoot%>account-data';
    </script>
    <script type="text/javascript" src="js/account.js"></script>
</head>
<body class="tundra">
<div dojoType="dijit.layout.BorderContainer">
    <div dojoType="dijit.layout.ContentPane" region="top" style="height:100px;">
        <div dojoType="dijit.layout.BorderContainer" gutters="false">
            <div dojoType="dijit.layout.ContentPane" region="center">
                Add form
            </div>
            <div dojoType="dijit.layout.ContentPane" region="right">
                <a href="login?mode=out">Logout</a>
            </div>
        </div>
    </div>
    <div dojoType="dijit.layout.ContentPane" region="right" style="width: 200px;">Filters will be here</div>
    <div dojoType="dijit.layout.ContentPane" region="center">
        <table dojoType="dojox.grid.DataGrid" store="transStore" id="tabTrans">
            <thead>
            <tr>
                <th field="actor_name" width="200px">Кто</th>
                <th field="amount" width="200px">Сколько</th>
                <th field="category_name" width="200px">Категория</th>
                <th field="comment" width="200px">Комментарий</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>