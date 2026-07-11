<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<%=request.getContextPath()%>/homepageStyle.css" rel="stylesheet" type="text/css">
<title>Model Car Gallery</title>
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
			<form method="post">
				<div id="divlogin">
					<b>Login</b><br>
						Email:<input type="email" id="mailSpace" name="mail"> <br>Password:<input type="password" id="pwdSpace" name="pwd"> <br> 
						<input type="submit" value="Login" id="loginbutton" formaction="loginServlet"> 
						<input type="submit" value="Registrati" id="regbutton" formaction="Setup"><br>
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
					<td><a href="product?action=deleteC&code=<%=beancart.getCode()%>">Delete from cart</a></td>
				</tr>
				<tr>
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
						<img alt="<%=bean.getName()%>" class="productImg" src="image?action=show&code=<%=bean.getCode()%>">
					<%
					} else {
					%>
					<img alt="Nessuna Immagine" class="productImg" src="${pageContext.request.contextPath}/img/Modellini-auto-scala-1-24-da-collezione.jpg">
					<%
					}
					%>

					<br> <b><%=bean.getName()%></b><br> 
					<br> <a href="product?action=addC&code=<%=bean.getCode()%>" class="flex-bottom-link"><input type="button" class="addcartbutton" value="Aggiungi al carrello"></a>
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


</body>
</html>