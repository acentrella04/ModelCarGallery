<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ordine Completato</title>
</head>
<body>
	<h1>Ordine effettuato con successo</h1>
	<% 
		OrderBean order=(OrderBean) request.getAttribute("order");
		if(order!=null){
	%>
	Id:<%=order.getId() %><br>
	Totale:<%=order.getUnitPrice() %><br>
	Quantità:<%=order.getQuantity() %>
	<%
	} 
	%>
</body>
</html>