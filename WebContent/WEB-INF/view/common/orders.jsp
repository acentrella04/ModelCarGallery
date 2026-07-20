<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*,it.unisa.modelcargallery.model.*"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>I miei ordini</title>
</head>

<body>

<h1>I miei ordini</h1>

<%
Collection<OrderBean> orders =(Collection<OrderBean>) request.getAttribute("orders");

if (orders != null && !orders.isEmpty()) {

    for (OrderBean order : orders) {
%>

<hr>

<h2>Ordine numero <%=order.getId()%></h2>

<p>
    Data: <%=order.getOrderDate()%>
</p>

<p>
    Totale: € <%=order.getTotal()%>
</p>

<p>
    Spedizione:
    <%=order.getAddress()%>,
    <%=order.getNumberAddress()%>
</p>

<table border="1">

    <tr>
        <th>Prodotto</th>
        <th>Prezzo</th>
        <th>Quantità</th>
        <th>Subtotale</th>
    </tr>

    <%
    for (OrderItemBean item : order.getItems()) {
    %>

    <tr>
        <td><%=item.getProductName()%></td>
        <td>€ <%=item.getUnitPrice()%></td>
        <td><%=item.getQuantity()%></td>
        <td>€ <%=item.getSubtotal()%></td>
    </tr>

    <%
    }
    %>

</table>

<%
    }
} else {
%>

<p>Non hai ancora effettuato ordini.</p>

<%
}
%>

<a href="<%=request.getContextPath()%>/common/welcome">
    Torna alla pagina principale
</a>

</body>
</html>