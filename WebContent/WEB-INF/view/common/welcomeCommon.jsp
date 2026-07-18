<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/homepageStyle.css" rel="stylesheet" type="text/css">
<title>Welcome User</title>
</head>
<body bgcolor="#F0FFFF">
<% 
List<?> errors = (List<?>) request.getAttribute("errors");
if (errors != null){
	for (Object error: errors){ %>
		<%=error %> <br>		
	<%
	}
}
%>
	<div>
		<fieldset id="logo">
			<a href="http://localhost/ModelCarGallery/"><img id="imglogo" src="${pageContext.request.contextPath}/img/Gemini_Generated_Image_es7nd4es7nd4es7n.png"></a>
		</fieldset>
	</div>
	<div id="maincontent">
		<fieldset id="navbar">
			<form method="post" >
				<div id="divlogin">
					Benvenuto<br>
				</div>
			</form>
				<div id="divnavbar">
					<div id="carosellonews">
						<img src="${pageContext.request.contextPath}/img/Gemini_Generated_Image_kqax5fkqax5fkqax.png" id="imgnews"> 
						<img src="${pageContext.request.contextPath}/img/Gemini_Generated_Image_f2fcmef2fcmef2fc.png" id="imgnews2">
					</div>
					<b>Auto Scala 1:10</b><br>
					<b>Moto Scala 1:10</b><br>
					<b>Auto Scala 1:100</b><br>
					<b>Moto Scala 1:100</b><br>
				</div>
				<%
			CartBean cart = (CartBean) session.getAttribute("cart");
			%>
			<h2>Cart</h2>
			<form action="ProcessOrder" method="post">
			<table border="1">
				<tr>
					<th>Name</th>
					<th>Action</th>
				</tr>
				<%
				List<ProductBean> prodcart = cart.getProducts();
				for (ProductBean beancart : prodcart) {
				%>
				<tr>
					<td><%=beancart.getName()%></td>
					<td><a href="welcome?action=deleteC&code=<%=beancart.getCode()%>">Delete from cart</a></td>
				</tr>
				<tr>
				
				<%
				}

				if (!prodcart.isEmpty()) {
				%>
				<td><input type="submit" value="Procedi all'ordine" id="processOrder">
				</tr>
				<%
				}
				%>
			</table>
			</form>
		</fieldset>
		<fieldset id="productfield">
			<div class="product-gallery">
				<%
				Collection<?> products = (Collection<?>) request.getAttribute("products");
				if (products != null && !products.isEmpty()) {
					Iterator<?> it = products.iterator();
					while (it.hasNext()) {
						ProductBean bean = (ProductBean) it.next();
				%>

				<div class="product-card">

					<%
					if (bean.hasImage()) {
					%>
						<img alt="<%=bean.getName()%>" class="productImg" src="<%=request.getContextPath()%>/image?action=show&code=<%=bean.getCode()%>">
					<%
					} else {
					%>
					<img alt="Nessuna Immagine" class="productImg">
					<%
					}
					%>

					<br> <b><%=bean.getName()%></b><br> 
					<br> <a href="welcome?action=addC&code=<%=bean.getCode()%>" class="flex-bottom-link"><input type="button" class="addcartbutton" value="Aggiungi al carrello"></a>
					<br> <a href="${pageContext.request.contextPath}/common/welcome?action=read&code=<%=bean.getCode()%>" class="flex-bottom-link"><input type="button" class="addcartbutton" value="Dettagli"></a><br>
				</div>

				<%
				}
				} else {
				%>
				<p>No products available</p>
				<%
				}
				%>
			</div>

		</fieldset>
	</div>
	<fieldset id="detailsfield">
	<div>
	<h2>Details</h2>
	<%
		ProductBean product = (ProductBean) request.getAttribute("product");
		if (product != null) {
	%>
	<table border="1">
		<tr>
			<th>Code</th>
			<th>Name</th>
			<th>Description</th>
			<th>Price</th>
			<th>Quantity</th>
		</tr>
		<tr>
			<td><%=product.getCode()%></td>
			<td><%=product.getName()%></td>
			<td><%=product.getDescription()%></td>
			<td><%=product.getPrice()%></td>
			<td><%=product.getQuantity()%></td>
		</tr>
	</table>
	<%
	}
	%>
	</div>
	</fieldset>

</body>
</html>