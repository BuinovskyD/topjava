<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>
    <style>
        tr.red {color: red}
        tr.green {color: green}
    </style>
</head>
<body>
<h2>Meals</h2>

<table border="1" cellspacing="0" cellpadding="2">
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${listMealTo}">
        <tr class="${meal.excess? 'red' : 'green'}">
            <td><javatime:format value="${meal.dateTime}" pattern="dd-MM-yyyy HH:mm"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>