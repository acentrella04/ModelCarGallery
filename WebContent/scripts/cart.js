document.addEventListener("DOMContentLoaded", function () {

    var cartContainer = document.getElementById("cartContainer");

    /*
     * Lo script può essere collegato anche a pagine che non hanno
     * il carrello. In quel caso non deve fare nulla.
     */
    if (cartContainer == null) {
        return;
    }

    var contextPath = document.body.dataset.contextPath || "";

    /*
     * Delegazione degli eventi:
     * intercetta sia i pulsanti presenti nella JSP sia quelli
     * creati successivamente tramite AJAX.
     */
    document.addEventListener("click", function (event) {

        var cartAction = event.target.closest(".cart-action");

        if (cartAction == null) {
            return;
        }

        event.preventDefault();

        var action = cartAction.dataset.action;
        var code = cartAction.dataset.code;

        if (action == null || action === "") {
            showCartMessage(
                "Azione del carrello non valida.",
                true
            );
            return;
        }

        updateCart(
            contextPath,
            action,
            code
        );
    });
});


function updateCart(contextPath, action, code) {

    var parameters = new URLSearchParams();

    parameters.append("action", action);

    if (code != null && code !== "") {
        parameters.append("code", code);
    }

    showCartMessage(
        "Aggiornamento carrello...",
        false
    );

    fetch(contextPath + "/cart-ajax", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded;charset=UTF-8",

            "Accept":
                "application/json"
        },

        body: parameters.toString()
    })
    .then(function (response) {

        return response.json()
            .then(function (data) {

                if (!response.ok) {

                    throw new Error(
                        data.message ||
                        "Errore durante l'aggiornamento del carrello."
                    );
                }

                return data;
            });
    })
    .then(function (data) {

        if (data.success !== true) {
            throw new Error(
                data.message ||
                "Impossibile aggiornare il carrello."
            );
        }

        renderCart(data);

        showCartMessage(
            "Carrello aggiornato.",
            false,
            true
        );
    })
    .catch(function (error) {

        showCartMessage(
            error.message,
            true
        );

        console.error(error);
    });
}


function renderCart(data) {

    var cartTable =
        document.getElementById("cartTable");

    var cartBody =
        document.getElementById("cartBody");

    var cartEmpty =
        document.getElementById("cartEmpty");

    var cartSummary =
        document.getElementById("cartSummary");

    var cartTotal =
        document.getElementById("cartTotal");

    if (cartBody == null) {
        return;
    }

    /*
     * Cancella tutte le vecchie righe.
     */
    cartBody.innerHTML = "";

    if (data.items == null || data.items.length === 0) {

        if (cartTable != null) {
            cartTable.hidden = true;
        }

        if (cartEmpty != null) {
            cartEmpty.hidden = false;
        }

        if (cartSummary != null) {
            cartSummary.hidden = true;
        }

        if (cartTotal != null) {
            cartTotal.textContent = "0.00";
        }

        return;
    }

    if (cartTable != null) {
        cartTable.hidden = false;
    }

    if (cartEmpty != null) {
        cartEmpty.hidden = true;
    }

    if (cartSummary != null) {
        cartSummary.hidden = false;
    }

    for (var i = 0; i < data.items.length; i++) {

        var item = data.items[i];

        var row = document.createElement("tr");

        appendTextCell(
            row,
            item.name
        );

        appendTextCell(
            row,
            "€ " + formatMoney(item.price)
        );

        var quantityCell =
            document.createElement("td");

        quantityCell.className =
            "cart-quantity-controls";

        quantityCell.appendChild(
            createCartAction(
                "−",
                "deleteC",
                item.code,
                "Diminuisci la quantità di " + item.name
            )
        );

        var quantity =
            document.createElement("strong");

        quantity.textContent =
            " " + item.quantity + " ";

        quantityCell.appendChild(quantity);

        quantityCell.appendChild(
            createCartAction(
                "+",
                "addC",
                item.code,
                "Aumenta la quantità di " + item.name
            )
        );

        row.appendChild(quantityCell);

        appendTextCell(
            row,
            "€ " + formatMoney(item.subtotal)
        );

        var actionCell =
            document.createElement("td");

        actionCell.appendChild(
            createCartAction(
                "Rimuovi",
                "removeAllC",
                item.code,
                "Rimuovi " + item.name + " dal carrello"
            )
        );

        row.appendChild(actionCell);

        cartBody.appendChild(row);
    }

    if (cartTotal != null) {
        cartTotal.textContent =
            formatMoney(data.total);
    }
}


function appendTextCell(row, value) {

    var cell = document.createElement("td");

    cell.textContent = value;

    row.appendChild(cell);
}


function createCartAction(
        text,
        action,
        code,
        ariaLabel) {

    var link = document.createElement("a");

    link.href = "#";

    link.className = "cart-action";

    link.dataset.action = action;
    link.dataset.code = code;

    link.textContent = text;

    link.setAttribute(
        "aria-label",
        ariaLabel
    );

    return link;
}


function formatMoney(value) {

    var number = Number(value);

    if (!Number.isFinite(number)) {
        return "0.00";
    }

    return number.toFixed(2);
}


function showCartMessage(
        message,
        isError,
        hideAutomatically) {

    var messageElement =
        document.getElementById("cartAjaxMessage");

    if (messageElement == null) {
        return;
    }

    messageElement.textContent = message;

    if (isError) {

        messageElement.className =
            "cart-message cart-message-error";

    } else {

        messageElement.className =
            "cart-message cart-message-success";
    }

    if (hideAutomatically === true) {

        window.setTimeout(function () {

            /*
             * Evita di cancellare un eventuale nuovo messaggio.
             */
            if (messageElement.textContent === message) {
                messageElement.textContent = "";
                messageElement.className = "cart-message";
            }

        }, 1500);
    }
}
