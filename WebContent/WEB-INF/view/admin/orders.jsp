<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*,it.unisa.modelcargallery.model.*"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestione ordini</title>
</head>

<body>

<h1>Ordini ricevuti</h1>

<form action="<%=request.getContextPath()%>/admin/orders"
      method="get">

    <label for="from">Dalla data:</label>

    <input type="date"
           name="from"
           id="from"
           value="<%=request.getParameter("from") != null
               ? request.getParameter("from")
               : ""%>">

    <label for="to">Alla data:</label>

    <input type="date"
           name="to"
           id="to"
           value="<%=request.getParameter("to") != null
               ? request.getParameter("to")
               : ""%>">

    <label for="mail">Cliente:</label>

    <input type="email"
           name="mail"
           id="mail"
           value="<%=request.getParameter("mail") != null
               ? request.getParameter("mail")
               : ""%>">

    <input type="submit" value="Cerca">
</form>

<%
Collection<OrderBean> orders =(Collection<OrderBean>) request.getAttribute("orders");

if (orders != null && !orders.isEmpty()) {

    for (OrderBean order : orders) {
%>

<hr>

<h2>Ordine numero <%=order.getId()%></h2>

<p>
    Cliente:
    <%=order.getName()%>
    <%=order.getSurname()%>
</p>

<p>
    Email: <%=order.getMail()%>
</p>

<p>
    Data: <%=order.getOrderDate()%>
</p>

<p>
    Totale: € <%=order.getTotal()%>
</p>

<table border="1">

    <tr>
        <th>Codice</th>
        <th>Prodotto</th>
        <th>Prezzo acquistato</th>
        <th>Quantità</th>
    </tr>

    <%
    for (OrderItemBean item : order.getItems()) {
    %>

    <tr>
        <td><%=item.getProductCode()%></td>
        <td><%=item.getProductName()%></td>
        <td>€ <%=item.getUnitPrice()%></td>
        <td><%=item.getQuantity()%></td>
    </tr>

    <%
    }
    %>

</table>

<%
    }
} else {
%>

<p>Nessun ordine trovato.</p>

<%
}
%>

<a href="<%=request.getContextPath()%>/admin/welcome">
    Torna all'area amministratore
</a>

</body>
</html>