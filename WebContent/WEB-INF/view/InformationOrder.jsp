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
			<%
			List<ProductBean> prodcart = cart.getProducts();

			Map<Integer, ProductBean> uniqueProducts = new LinkedHashMap<>();
			Map<Integer, Integer> quantities = new LinkedHashMap<>();

			for (ProductBean product : prodcart) {

    			uniqueProducts.put(product.getCode(), product);

    			quantities.merge(
        			product.getCode(),
        			1,
        			Integer::sum
    		);
			}	
			%>
			<h2>Cart</h2>
			<table border="1">
				<tr>
					<th>Name</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Subtotal</th>
				</tr>
				<%
    			float totalOrder = 0;

    			for (ProductBean beancart : uniqueProducts.values()) {

        			int quantity = quantities.get(beancart.getCode());

        			float subtotal = beancart.getPrice() * quantity;

        			totalOrder += subtotal;
    			%>
				<tr>
					<td><%=beancart.getName()%></td>
					<td><%=beancart.getPrice()%></td>
					<td><%=quantity %></td>
					<td><%=subtotal %> €</td>
				</tr>
				<%
    			}
    			%>
    			<tr>
        			<th>Totale ordine</th>
        		</tr>
        		<tr>
					<td>
            		<strong>€ <%=totalOrder%></strong>
        			</td>
    			</tr>
				</table>
	<form method="post" action="${pageContext.request.contextPath}/ProcessOrder">
		<fieldset>
			<div>
				<h1>Inserisci i tuoi dati e i dati di spedizione</h1>
				Nome:<input type="text" id="nameSpace" name="name" required><br>
				Cognome:<input type="text" id="surnameSpace" name="surname" required><br>
				Indirizzo:<input type="text" id="addressSpace" name="address" required><br>
				N.Civico:<input type="number" id="numberSpace" name="number" required><br>
				Email:<input type="email" id="mailSpace" name="mail" value="${sessionScope.userMail}" readonly><br>
				<br>
			</div>
			<div>
				<h1>Inserisci i dati di fatturazione</h1>
				Nome intestatario:<input type="text" id="nameCardSpace" name="nameCard" required><br>
				Numero Carta:<input type="number" maxlength="16" id="numberCardSpace" name="numberCard" required><br>
				Data Scadenza (mm/yy):<input type="number" id="expireSpace" name="expire" required><br>
				CVV:<input type="number" maxlength="3" id="cvvSpace" name="CVV" required><br>
				<input type="submit" value="Procedi all'ordine">
			</div>
		</fieldset>
	</form>
</body>
</html>