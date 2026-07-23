<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Gestione ordini</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

	</head>

	<body>

		<header id="logo">

			<a href="<%=request.getContextPath()%>/admin/welcome">

				<img id="imglogo" src="<%=request.getContextPath()%>/images/Gemini_Generated_Image_es7nd4es7nd4es7n.png" alt="Model Car Gallery">

			</a>

		</header>

		<main>

			<section class="order-card">

				<h1>Ordini ricevuti</h1>

				<p>
					Da questa pagina puoi visualizzare tutti gli ordini
					e filtrarli per data o cliente.
				</p>

			</section>


			<form class="order-filters" action="<%=request.getContextPath()%>/admin/orders" method="get">

				<div class="form-group">

					<label for="from">
						Dalla data:
					</label>

					<input type="date" name="from" id="from" value="<%=request.getParameter(" from") !=null ?request.getParameter("from") : "" %>">

				</div>

				<div class="form-group">

					<label for="to">
						Alla data:
					</label>

					<input type="date" name="to" id="to" value="<%=request.getParameter(" to") !=null ?request.getParameter("to") : "" %>">

				</div>

				<div class="form-group">

					<label for="mail">
						Email cliente:
					</label>

					<input type="email" name="mail" id="mail" placeholder="cliente@email.it" value="<%=request.getParameter(" mail") !=null ? request.getParameter("mail") : "" %>">

				</div>

				<div class="filter-actions">

					<input type="submit" value="Cerca">

					<a href="<%=request.getContextPath()%>/admin/orders">
						Rimuovi filtri
					</a>

				</div>

			</form>

			<% Collection<OrderBean> orders =
				(Collection<OrderBean>)request.getAttribute("orders");

					if (orders != null && !orders.isEmpty()) {

					for (OrderBean order : orders) {
					%>

					<section class="order-card">

						<h2>
							Ordine numero <%=order.getId()%>
						</h2>

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
										<th>Codice</th>
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
												<%=item.getProductCode()%>
											</td>

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

							<h2>Nessun ordine trovato</h2>

							<p>
								Non sono presenti ordini corrispondenti ai filtri selezionati.
							</p>

						</section>

						<% } %>



							<nav class="order-card">

								<a href="<%=request.getContextPath()%>/admin/welcome">
									Torna all’area amministratore
								</a>

								<br><br>

								<a href="<%=request.getContextPath()%>/admin/logout">
									Logout
								</a>

							</nav>

		</main>

	</body>

	</html>