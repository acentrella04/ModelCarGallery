<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Details</title>
</head>
<body>
<% 
List<?> errors = (List<?>) request.getAttribute("errors");
if (errors != null){
	for (Object error: errors){ %>
		<%=error %> <br>		
	<%
	}
}
%>
			<%
			CartBean cart = (CartBean) session.getAttribute("cart");
			%>
			<h2>Cart</h2>
			<table border="1">
				<tr>
					<th>Name</th>
					<th>Price</th>
					<th>Subtotal</th>
				</tr>
				<%
				float subtotal=0;
				List<ProductBean> prodcart = cart.getProducts();
    			for (ProductBean beancart : prodcart) {
					subtotal=subtotal+beancart.getPrice();
    			%>
				<tr>
					<td><%=beancart.getName()%></td>
					<td><%=beancart.getPrice()%></td>
					<td><%=subtotal%> €</td>
				</tr>
				<%
    			}
    			%>
				</table>
	<form action="<%=request.getContextPath()%>/ProcessOrder" method="post">

    <label>Nome:</label>
    <input type="text" name="name" required>

    <label>Cognome:</label>
    <input type="text" name="surname" required>

    <label>Indirizzo:</label>
    <input type="text" name="address" required>

    <label>Numero civico:</label>
    <input type="number" name="number" required>

    <label>Metodo di pagamento:</label>

    <select name="paymentMethod" required>
        <option value="">Seleziona</option>
        <option value="Carta">Carta</option>
        <option value="PayPal">PayPal</option>
        <option value="Contrassegno">Contrassegno</option>
    </select>

    <input type="submit" value="Conferma ordine">
</form>
</body>
</html>