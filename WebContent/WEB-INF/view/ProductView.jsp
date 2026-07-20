<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Model Car Gallery</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

		<script src="<%=request.getContextPath()%>/scripts/validation.js" defer>
		</script>

		<script src="<%=request.getContextPath()%>/scripts/cart.js" defer>
		</script>

	</head>

	<body data-context-path="<%=request.getContextPath()%>">

		<% List<?> errors =
			(List
			<?>) request.getAttribute("errors");

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

<!-- =========================
     LOGO
     ========================= -->

<header id="logo">

    <a href="<%=request.getContextPath()%>/product">

        <img id="imglogo"
             src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_es7nd4es7nd4es7n.png"
             alt="Model Car Gallery">

    </a>

</header>


<!-- =========================
     CONTENUTO PRINCIPALE
     ========================= -->

<main id="maincontent">


    <!-- =========================
         BARRA LATERALE
         ========================= -->

    <aside id="navbar">


        <!-- LOGIN -->

        <form id="loginForm"
              action="<%=request.getContextPath()%>/loginServlet"
              method="post"
              novalidate>

            <div id="divlogin">

                <h3>Login</h3>

                <label for="mailSpace">
                    Email:
                </label>

                <input type="email"
                       id="mailSpace"
                       name="mail">

                <label for="pwdSpace">
                    Password:
                </label>

                <input type="password"
                       id="pwdSpace"
                       name="pwd">

                <div class="login-actions">

                    <input type="submit"
                           value="Login"
                           id="loginbutton">

                    <a href="<%=request.getContextPath()%>/Setup"
                       id="regbutton">
                        Registrati
                    </a>

                </div>

            </div>

        </form>


        <!-- MENU E CAROSELLO -->

        <div id="divnavbar">

            <div id="carosellonews">

                <img src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_kqax5fkqax5fkqax.png"
                     id="imgnews"
                     alt="Novità modellini auto">

                <img src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_f2fcmef2fcmef2fc.png"
                     id="imgnews2"
                     alt="Offerte modellini auto">

            </div>

            <div class="scale-list">

                <span>Auto Scala 1:10</span>
                <span>Moto Scala 1:10</span>
                <span>Auto Scala 1:100</span>
                <span>Moto Scala 1:100</span>

            </div>

        </div>


        <!-- =========================
             CARRELLO
             ========================= -->

        <%
        CartBean cart =
                (CartBean) session.getAttribute("cart");

        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        List<ProductBean> distinctProducts =
                cart.getDistinctProducts();

        boolean cartIsEmpty =
                cart.getProducts().isEmpty();
        %>

        <section id="cartContainer">

            <h2>Cart</h2>

            <p id="cartAjaxMessage"
               class="cart-message"
               aria-live="polite">
            </p>

            <div class="table-wrapper">

                <table id="cartTable"
                       <%=cartIsEmpty ? "hidden" : ""%>>

                    <thead>

                        <tr>
                            <th>Nome</th>
                            <th>Prezzo</th>
                            <th>Quantità</th>
                            <th>Subtotale</th>
                            <th>Azioni</th>
                        </tr>

                    </thead>

                    <tbody id="cartBody">

                    <%
                    for (ProductBean beancart :
                            distinctProducts) {

                        int cartQuantity =
                                cart.getQuantity(
                                        beancart.getCode()
                                );

                        float rowSubtotal =
                                beancart.getPrice()
                                * cartQuantity;
                    %>

                        <tr>

                            <td>
                                <%=beancart.getName()%>
                            </td>

                            <td>
                                € <%=String.format(
                                        Locale.US,
                                        "%.2f",
                                        beancart.getPrice()
                                )%>
                            </td>

                            <td class="cart-quantity-controls">

                                <a href="<%=request.getContextPath()%>/product?action=deleteC&code=<%=beancart.getCode()%>"
                                   class="cart-action"
                                   data-action="deleteC"
                                   data-code="<%=beancart.getCode()%>"
                                   aria-label="Diminuisci quantità">
                                    −
                                </a>

                                <strong>
                                    <%=cartQuantity%>
                                </strong>

                                <a href="<%=request.getContextPath()%>/product?action=addC&code=<%=beancart.getCode()%>"
                                   class="cart-action"
                                   data-action="addC"
                                   data-code="<%=beancart.getCode()%>"
                                   aria-label="Aumenta quantità">
                                    +
                                </a>

                            </td>

                            <td>
                                € <%=String.format(
                                        Locale.US,
                                        "%.2f",
                                        rowSubtotal
                                )%>
                            </td>

                            <td>

                                <a href="<%=request.getContextPath()%>/product?action=removeAllC&code=<%=beancart.getCode()%>"
                                   class="cart-action"
                                   data-action="removeAllC"
                                   data-code="<%=beancart.getCode()%>">
                                    Rimuovi
                                </a>

                            </td>

                        </tr>

                    <%
                    }
                    %>

                    </tbody>

                </table>

            </div>


            <!-- MESSAGGIO CARRELLO VUOTO -->

            <p id="cartEmpty"
               <%=!cartIsEmpty ? "hidden" : ""%>>

                Il carrello è vuoto.

            </p>


            <!-- RIEPILOGO CARRELLO -->

            <div id="cartSummary"
                 <%=cartIsEmpty ? "hidden" : ""%>>

                <p>

                    <strong>

                        Totale:
                        € <span id="cartTotal">

                            <%=String.format(
                                    Locale.US,
                                    "%.2f",
                                    cart.getTotal()
                            )%>

                        </span>

                    </strong>

                </p>

                <p>

                    <a href="<%=request.getContextPath()%>/product?action=clearC"
                       class="cart-action"
                       data-action="clearC">

                        Svuota carrello

                    </a>

                </p>

                <form action="<%=request.getContextPath()%>/Setup"
                      method="get">

                    <input type="submit"
                           value="Procedi all'ordine"
                           id="processOrder">

                </form>

            </div>

        </section>

    </aside>


    <!-- =========================
         CATALOGO PRODOTTI
         ========================= -->

    <section id="productfield">

        <div class="product-gallery">

        <%
        Collection<?> products =
			(Collection
			<?>) request.getAttribute(
                        "products"
                );

        if (products != null &&
                !products.isEmpty()) {

            Iterator<?> iterator =
			products.iterator();

			while (iterator.hasNext()) {

			ProductBean bean =
			(ProductBean) iterator.next();
			%>

			<article class="product-card">

				<% if (bean.hasImage()) { %>

					<img alt="<%=bean.getName()%>" class="productImg"
						src="<%=request.getContextPath()%>/image?action=show&code=<%=bean.getCode()%>">

					<% } else { %>

						<img alt="Nessuna immagine disponibile" class="productImg"
							src="<%=request.getContextPath()%>/images/Modellini-auto-scala-1-24-da-collezione.jpg">

						<% } %>

							<b>
								<%=bean.getName()%>
							</b>

							<div class="product-actions">


								<!-- AGGIUNGI AL CARRELLO -->

								<form action="<%=request.getContextPath()%>/product" method="get">

									<input type="hidden" name="action" value="addC">

									<input type="hidden" name="code" value="<%=bean.getCode()%>">

									<button type="submit" class="addcartbutton cart-action" data-action="addC"
										data-code="<%=bean.getCode()%>">

										Aggiungi al carrello

									</button>

								</form>


								<!-- DETTAGLI -->

								<form action="<%=request.getContextPath()%>/product" method="get">

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

					</main>


					<!-- =========================
     DETTAGLI PRODOTTO
     ========================= -->

					<section id="detailsfield">

						<div id="detailsSection">

							<h2>Details</h2>

							<% ProductBean product=(ProductBean) request.getAttribute( "product" ); if (product !=null)
								{ %>

								<div class="table-wrapper">

									<table>

										<thead>

											<tr>
												<th>Code</th>
												<th>Name</th>
												<th>Description</th>
												<th>Price</th>
												<th>Quantity</th>
											</tr>

										</thead>

										<tbody>

											<tr>

												<td>
													<%=product.getCode()%>
												</td>

												<td>
													<%=product.getName()%>
												</td>

												<td>
													<%=product.getDescription()%>
												</td>

												<td>
													€ <%=String.format( Locale.US, "%.2f" , product.getPrice() )%>
												</td>

												<td>
													<%=product.getQuantity()%>
												</td>

											</tr>

										</tbody>

									</table>

								</div>

								<% } else { %>

									<p>
										Seleziona un prodotto per visualizzarne i dettagli.
									</p>

									<% } %>

						</div>

					</section>

	</body>

	</html>