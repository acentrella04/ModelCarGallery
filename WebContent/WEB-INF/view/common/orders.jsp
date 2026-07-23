<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>I miei ordini</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

	</head>

	<body>

		<header id="logo">

			<a href="<%=request.getContextPath()%>/common/welcome">

				<img id="imglogo" src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_es7nd4es7nd4es7n.png" alt="Model Car Gallery">

			</a>

		</header>

		<main>

			<section class="order-card">

				<h1>I miei ordini</h1>

				<p>
					In questa pagina puoi visualizzare tutti gli ordini effettuati con il tuo account.
				</p>

			</section>

			<% Collection<OrderBean> orders =(Collection<OrderBean>)request.getAttribute("orders");

					if (orders != null && !orders.isEmpty()) {

					for (OrderBean order : orders) {
					%>

					<section class="order-card">

						<h2>
							Ordine numero <%=order.getId()%>
						</h2>

						<p>
							<strong>Data:</strong>
							<%=order.getOrderDate()%>
						</p>

						<p>
							<strong>Indirizzo di spedizione:</strong>
							<%=order.getAddress()%>,
								<%=order.getNumberAddress()%>
						</p>

						<p>
							<strong>Metodo di pagamento:</strong>
							<%=order.getPaymentMethod()%>
						</p>

						<div class="table-wrapper">

							<table>

								<thead>

									<tr>
										<th>Prodotto</th>
										<th>Prezzo acquistato</th>
										<th>Quantità</th>
										<th>Subtotale</th>
									</tr>

								</thead>

								<tbody>

									<% for (OrderItemBean item : order.getItems()) { %>

										<tr>

											<td>
												<%=item.getProductName()%>
											</td>

											<td>
												€ <%=String.format( Locale.US, "%.2f" , item.getUnitPrice() )%>
											</td>

											<td>
												<%=item.getQuantity()%>
											</td>

											<td>
												€ <%=String.format( Locale.US, "%.2f" , item.getSubtotal() )%>
											</td>

										</tr>

										<% } %>

								</tbody>

							</table>

						</div>

						<h3>
							Totale ordine:
							€ <%=String.format( Locale.US, "%.2f" , order.getTotal() )%>
						</h3>

					</section>

					<% } } else { %>

						<section class="order-card">

							<h2>Nessun ordine presente</h2>

							<p>
								Non hai ancora effettuato nessun ordine.
							</p>

						</section>

						<% } %>

							<nav class="order-card">

								<a href="<%=request.getContextPath()%>/common/welcome">
									Torna alla pagina principale
								</a>

								<br><br>

								<a href="<%=request.getContextPath()%>/common/logout">
									Logout
								</a>

							</nav>

		</main>

	</body>

	</html>