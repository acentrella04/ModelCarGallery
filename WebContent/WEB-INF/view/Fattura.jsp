<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Ordine completato</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

	</head>

	<body>

		<header id="logo">

			<a href="<%=request.getContextPath()%>/common/welcome">

				<img id="imglogo" src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_es7nd4es7nd4es7n.png"
					alt="Model Car Gallery">

			</a>

		</header>

		<main>

			<% OrderBean order=(OrderBean) request.getAttribute("order"); if (order !=null) { %>

				<section class="order-card">

					<h1>Ordine effettuato con successo</h1>

					<p>
						Il tuo ordine è stato registrato correttamente.
					</p>

					<p>
						<strong>Numero ordine:</strong>
						<%=order.getId()%>
					</p>

					<% if (order.getOrderDate() !=null) { %>

						<p>
							<strong>Data:</strong>
							<%=order.getOrderDate()%>
						</p>

						<% } %>

							<p>
								<strong>Cliente:</strong>
								<%=order.getName()%>
									<%=order.getSurname()%>
							</p>

							<p>
								<strong>Email:</strong>
								<%=order.getMail()%>
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

				</section>


				<section class="order-card">

					<h2>Prodotti acquistati</h2>

					<div class="table-wrapper">

						<table>

							<thead>

								<tr>
									<th>Prodotto</th>
									<th>Prezzo unitario</th>
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

					<h2>
						Totale ordine:
						€ <%=String.format( Locale.US, "%.2f" , order.getTotal() )%>
					</h2>

				</section>


				<nav class="order-card">

					<a href="<%=request.getContextPath()%>/common/orders">
						Visualizza i miei ordini
					</a>

					<br><br>

					<a href="<%=request.getContextPath()%>/common/welcome">
						Torna alla pagina principale
					</a>

				</nav>

				<% } else { %>

					<section class="order-card">

						<h1>Ordine non disponibile</h1>

						<p>
							Non è stato possibile recuperare le informazioni
							relative all’ordine.
						</p>

						<a href="<%=request.getContextPath()%>/common/welcome">
							Torna alla pagina principale
						</a>

					</section>

					<% } %>

		</main>

	</body>

	</html>