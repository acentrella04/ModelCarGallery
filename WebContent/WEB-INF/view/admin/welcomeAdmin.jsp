<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="<%=request.getContextPath()%>/homepageStyle.css" rel="stylesheet" type="text/css">
<title>Admin Control</title>
</head>
<body>
	<h2>Insert</h2>
	<form action="${pageContext.request.contextPath}/admin/welcome" method="post" id="formAdmin">
		<input type="hidden" name="action" value="insert"> 
		<label for="name">Name:</label><br><input name="name" id="name" type="text" required placeholder="enter name"><br> 
		<label for="description">Description:</label><br><textarea name="description" id="description" rows="3" required placeholder="enter description"></textarea><br>
		<label for="price">Price:</label><br><input name="price" id="price" type="number" min="0" value="0" required><br>
		<label for="quantity">Quantity:</label><br><input name="quantity" id="quantity" type="number" min="1" value="1" required><br>
		<input type="submit" value="Add"><input type="reset" value="Reset">
	</form>
	<div>
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
					<br> <a href="${pageContext.request.contextPath}/product?action=delete&code=<%=bean.getCode()%>" class="flex-bottom-link"><input type="button" class="addcartbutton" value="Elimina"></a>
					<br> <a href="${pageContext.request.contextPath}/product?action=read&code=<%=bean.getCode()%>" class="flex-bottom-link"><input type="button" class="addcartbutton" value="Dettagli"></a><br>
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
    <form method="post" action="image" enctype="multipart/form-data">
        <input type="hidden" name="action"  value="upload"/>
        <input type="hidden" name="productCode" value="<%= product.getCode() %>"/>
        <label for="image">Carica Immagine:</label><br><input type="file" name="image" id="image" accept="image/*"/>
        <input type="submit" value="Carica">
    </form>
	<%
		}
	%>
	<a href="<%=request.getContextPath()%>/common/logout">Logout</a> 
</body>
</html>