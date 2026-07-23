<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Admin Control</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

		<script src="<%=request.getContextPath()%>/scripts/validation.js" defer>
		</script>

	</head>

	<body>

		<% List<?> errors =(List<?>) request.getAttribute("errors");

if (errors != null && !errors.isEmpty()) {
%>

<div class="form-message form-message-error">

    <%
    for (Object error : errors) {
    %>

    <p><%=error%></p>

    <%
    }
    %>

</div>

<%
}
%>


<header id="logo">

    <a href="<%=request.getContextPath()%>/admin/welcome">

        <img id="imglogo"src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_es7nd4es7nd4es7n.png"alt="Model Car Gallery">

    </a>

</header>


<main>

    <nav class="order-card">

        <h1>Area amministratore</h1>

        <p>
            <a href="<%=request.getContextPath()%>/admin/orders">
                Visualizza ordini
            </a>
        </p>

        <p>
            <a href="<%=request.getContextPath()%>/admin/logout">
                Logout
            </a>
        </p>

    </nav>


    <section class="order-card">

        <h2>Inserisci prodotto</h2>

        <form id="formAdmin"action="<%=request.getContextPath()%>/admin/welcome" method="post" novalidate>

            <input type="hidden" name="action" value="insert">


        

            <label for="name">
                Nome:
            </label>

            <input type="text"id="name"name="name"maxlength="100"placeholder="Inserisci il nome"required>


            <label for="description">
                Descrizione:
            </label>

            <select name="description" required>
            	<option value="Auto Scala 1:24">Auto Scala 1:24</option>
            	<option value="Moto Scala 1:24">Moto Scala 1:24</option>
            	<option value="Auto Scala 1:48">Auto Scala 1:48</option>
            	<option value="Moto Scala 1:48">Moto Scala 1:48</option>
            </select>


            <label for="price">
                Prezzo:
            </label>

            <input type="number"id="price"name="price"min="0"step="0.01"value="0"required>


            <label for="quantity">
                Quantità:
            </label>

            <input type="number"id="quantity"name="quantity"min="0"value="1"required>


            <div class="login-actions">

                <input type="submit"value="Aggiungi">

                <input type="reset"value="Reimposta">

            </div>

        </form>

    </section>


    <section id="productfield">

        <h2>Catalogo prodotti</h2>

        <div class="product-gallery">

        <%
        Collection<?> products =(Collection<?>) request.getAttribute("products");

        if (products != null && !products.isEmpty()) {

            Iterator<?> iterator =products.iterator();

			while (iterator.hasNext()) {

			ProductBean bean =(ProductBean) iterator.next();
			%>

			<article class="product-card">

				<% if (bean.hasImage()) { %>

					<img alt="<%=bean.getName()%>" class="productImg"src="<%=request.getContextPath()%>/image?action=show&amp;code=<%=bean.getCode()%>">

					<% } else { %>

						<img alt="Nessuna immagine disponibile" class="productImg"src="<%=request.getContextPath()%>/images/Modellini-auto-scala-1-24-da-collezione.jpg">

						<% } %>

							<b>
								<%=bean.getName()%>
							</b>

							<div class="product-actions">

								<form action="<%=request.getContextPath()%>/admin/welcome" method="post">

									<input type="hidden" name="action" value="delete">

									<input type="hidden" name="code" value="<%=bean.getCode()%>">

									<button type="submit" class="addcartbutton">

										Elimina

									</button>

								</form>


								<form action="<%=request.getContextPath()%>/admin/welcome" method="get">

									<input type="hidden" name="action" value="read">

									<input type="hidden" name="code" value="<%=bean.getCode()%>">

									<button type="submit" class="addcartbutton">

										Dettagli

									</button>

								</form>

							</div>

			</article>

			<% } } else { %>

				<p>Nessun prodotto disponibile.</p>

				<% } %>

					</div>

					</section>


					<section id="detailsfield">

						<div id="detailsSection">

							<h2>Modifica prodotto</h2>

							<% ProductBean product=(ProductBean) request.getAttribute( "product" ); if (product !=null)
								{ %>

								<form id="updateProductForm" action="<%=request.getContextPath()%>/admin/welcome"method="post" novalidate>

									<input type="hidden" name="action" value="update">

									<input type="hidden" name="code" value="<%=product.getCode()%>">


									<label for="updateName">
										Nome:
									</label>

									<input type="text" id="updateName" name="name" maxlength="100" value="<%=product.getName()%>" required>


									<label for="updatePrice">
										Prezzo:
									</label>

									<input type="number" id="updatePrice" name="price" min="0" step="0.01"value="<%=product.getPrice()%>" required>
									
									<label for="updateQuantity">
										Quantità disponibile:
									</label>

									<input type="number" id="updateQuantity" name="quantity" min="0"value="<%=product.getQuantity()%>" required>


									<input type="submit" value="Salva modifiche">

								</form>
								
								<form class="order-card" method="post" action="<%=request.getContextPath()%>/image"
									enctype="multipart/form-data">

									<h3>Modifica immagine</h3>

									<input type="hidden" name="action" value="upload">

									<input type="hidden" name="productCode" value="<%=product.getCode()%>">

									<label for="image">
										Seleziona una nuova immagine:
									</label>

									<input type="file" id="image" name="image" accept="image/*" required>

									<input type="submit" value="Carica immagine">

								</form>

								<% } else { %>

									<p>
										Seleziona un prodotto tramite il pulsante
										<strong>Dettagli</strong> per modificarlo.
									</p>

									<% } %>

						</div>

					</section>

					</main>

	</body>

	</html>