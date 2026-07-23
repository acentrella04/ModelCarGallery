document.addEventListener("DOMContentLoaded", function() {
    configureEmailAvailability();

    configureForm("loginForm", [
        {
            id: "mailSpace",
            validator: validateEmail
        },
        {
            id: "pwdSpace",
            validator: validateLoginPassword
        }
    ]);

    configureForm("registrationForm", [
        {
            id: "mailSpace",
            validator: validateEmail
        },
        {
            id: "pwdSpace",
            validator: validateRegistrationPassword
        }
    ]);

    configureForm("orderForm", [
        {
            id: "nameSpace",
            validator: validatePersonName
        },
        {
            id: "surnameSpace",
            validator: validatePersonName
        },
        {
            id: "addressSpace",
            validator: validateAddress
        },
        {
            id: "numberSpace",
            validator: validateHouseNumber
        },
        {
            id: "nameCardSpace",
            validator: validatePersonName
        },
        {
            id: "numberCardSpace",
            validator: validateCardNumber
        },
        {
            id: "expireSpace",
            validator: validateExpirationDate
        },
        {
            id: "cvvSpace",
            validator: validateCvv
        }
    ]);

    configureForm("formAdmin", [
        {
            id: "name",
            validator: validateProductName
        },
        {
            id: "description",
            validator: validateDescription
        },
        {
            id: "price",
            validator: validatePrice
        },
        {
            id: "quantity",
            validator: validateQuantity
        }
    ]);

    configureForm("updateProductForm", [
        {
            id: "updateName",
            validator: validateProductName
        },
        {
            id: "updateDescription",
            validator: validateDescription
        },
        {
            id: "updatePrice",
            validator: validatePrice
        },
        {
            id: "updateQuantity",
            validator: validateQuantity
        }
    ]);
    var cardInput = document.getElementById("numberCardSpace");

    if (cardInput != null) {

        cardInput.addEventListener("input", function() {

            var value = cardInput.value.replace(/\D/g, "");

            value = value.substring(0, 19);

            cardInput.value =value.replace(/(.{4})/g, "$1 ").trim();
        });
    }
    var expirationInput = document.getElementById("expireSpace");

    if (expirationInput != null) {

        expirationInput.addEventListener("input", function() {

            var value =expirationInput.value.replace(/\D/g, "");

            value = value.substring(0, 4);

            if (value.length > 2) {
                value =value.substring(0, 2)+ "/"+ value.substring(2);
            }

            expirationInput.value = value;
        });
    }
    var cvvInput = document.getElementById("cvvSpace");

    if (cvvInput != null) {

        cvvInput.addEventListener("input", function() {

            cvvInput.value =cvvInput.value.replace(/\D/g, "").substring(0, 4);
        });
    }
});


function configureForm(formId, fields) {

    var form = document.getElementById(formId);

    if (form == null) {
        return;
    }

    for (var i = 0;i < fields.length;i++) {

        var rule = fields[i];
        var input = document.getElementById(rule.id);

        if (input != null) {

            bindChangeValidation(input,rule.validator);
        }
    }

    form.addEventListener("submit", function(event) {

        var formIsValid = true;
        var firstInvalidInput = null;

        for (var i = 0;i < fields.length;i++) {

            var rule = fields[i];
            var input = document.getElementById(rule.id);

            if (input != null) {

                var fieldIsValid =validateField(input,rule.validator);

                if (!fieldIsValid) {

                    formIsValid = false;

                    if (firstInvalidInput == null) {
                        firstInvalidInput = input;
                    }
                }
            }
        }

        if (!formIsValid) {

            event.preventDefault();

            if (firstInvalidInput != null) {
                firstInvalidInput.focus();
            }
        }
    });
}


function bindChangeValidation(input, validator) {

    input.addEventListener("change", function() {

        validateField(input,validator);
    });
}

function validateField(input, validator) {

    var errorMessage =validator(input.value);

    if (errorMessage !== "") {

        showError(input,errorMessage);

        return false;
    }

    clearError(input);

    return true;
}


function showError(input, message) {

    var errorId =input.id + "Error";

    var errorElement =document.getElementById(errorId);

    if (errorElement == null) {

        errorElement =document.createElement("span");

        errorElement.id = errorId;
        errorElement.className = "field-error";

        input.insertAdjacentElement("afterend",errorElement);
    }

    errorElement.textContent = message;

    input.classList.add("input-invalid");
    input.classList.remove("input-valid");

    input.setAttribute("aria-invalid","true");

    input.setAttribute("aria-describedby",errorId);
}

function clearError(input) {

    var errorId =input.id + "Error";

    var errorElement =document.getElementById(errorId);

    if (errorElement != null) {
        errorElement.textContent = "";
    }

    input.classList.remove("input-invalid");
    input.classList.add("input-valid");

    input.setAttribute("aria-invalid","false");
}


function validateEmail(value) {

    var email = value.trim();

    if (email === "") {
        return "Inserisci l'indirizzo email.";
    }

    var emailRegex =/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    if (!emailRegex.test(email)) {
        return "Inserisci un indirizzo email valido.";
    }

    return "";
}


function validateLoginPassword(value) {

    if (value.trim() === "") {
        return "Inserisci la password.";
    }

    return "";
}

function validateRegistrationPassword(value) {

    if (value === "") {
        return "Inserisci una password.";
    }

    if (value.length < 8) {
        return "La password deve contenere almeno 8 caratteri.";
    }

    var passwordRegex =/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$/;

    if (!passwordRegex.test(value)) {
        return "La password deve contenere una maiuscola, una minuscola e un numero.";
    }

    return "";
}


function validatePersonName(value) {

    var name = value.trim();

    if (name === "") {
        return "Questo campo è obbligatorio.";
    }

    var nameRegex =/^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/;

    if (!nameRegex.test(name)) {
        return "Usa solo lettere, spazi, apostrofi o trattini.";
    }

    return "";
}


function validateAddress(value) {

    var address = value.trim();

    if (address === "") {
        return "Inserisci l'indirizzo.";
    }

    var addressRegex =/^[A-Za-zÀ-ÖØ-öø-ÿ0-9'.,/ -]{5,80}$/;

    if (!addressRegex.test(address)) {
        return "L'indirizzo contiene caratteri non validi.";
    }

    return "";
}


function validateHouseNumber(value) {

    var number = value.trim();

    if (number === "") {
        return "Inserisci il numero civico.";
    }

    var numberRegex =/^[0-9]{1,5}[A-Za-z]?$/;

    if (!numberRegex.test(number)) {
        return "Inserisci un numero civico valido, ad esempio 14 oppure 14A.";
    }

    return "";
}


function validateCardNumber(value) {

    var cardNumber =value.replace(/[\s-]/g, "");

    if (cardNumber === "") {
        return "Inserisci il numero della carta.";
    }

    var cardRegex =/^[0-9]{13,19}$/;

    if (!cardRegex.test(cardNumber)) {
        return "Il numero della carta deve contenere da 13 a 19 cifre.";
    }

    return "";
}


function validateExpirationDate(value) {

    var expiration = value.trim();

    if (expiration === "") {
		return "Inserisci la data di scadenza.";
    }

    var expirationRegex =/^(0[1-9]|1[0-2])\/([0-9]{2})$/;

    var result =expirationRegex.exec(expiration);

    if (result == null) {
        return "Usa il formato MM/AA, ad esempio 09/28.";
    }

    var month =parseInt(result[1], 10);

    var year =2000 + parseInt(result[2], 10);

    var currentDate =new Date();

    var currentMonth =currentDate.getMonth() + 1;

    var currentYear =currentDate.getFullYear();

    if (year < currentYear ||(year === currentYear &&month < currentMonth)) {

        return "La carta risulta scaduta.";
    }

    return "";
}


function validateCvv(value) {

    var cvv = value.trim();

    if (cvv === "") {
        return "Inserisci il codice CVV.";
    }

    var cvvRegex =/^[0-9]{3,4}$/;

    if (!cvvRegex.test(cvv)) {
        return "Il CVV deve contenere 3 o 4 cifre.";
    }

    return "";
}


function validateProductName(value) {

    var productName = value.trim();

    if (productName === "") {
        return "Inserisci il nome del prodotto.";
    }

    if (productName.length < 3 ||productName.length > 100) {

        return "Il nome deve contenere da 3 a 100 caratteri.";
    }

    var productNameRegex =/^[A-Za-zÀ-ÖØ-öø-ÿ0-9#'.,/&()\- ]+$/;

    if (!productNameRegex.test(productName)) {
        return "Il nome del prodotto contiene caratteri non validi.";
    }

    return "";
}


function validateDescription(value) {

    var description = value.trim();

    if (description === "") {
        return "Inserisci una descrizione.";
    }

    if (description.length < 3 ||description.length > 100) {

        return "La descrizione deve contenere da 3 a 100 caratteri.";
    }

    return "";
}


function validatePrice(value) {

    if (value.trim() === "") {
        return "Inserisci il prezzo.";
    }

    var price =parseFloat(value);

    if (isNaN(price)) {
        return "Il prezzo deve essere un numero.";
    }

    if (price < 0) {
        return "Il prezzo non può essere negativo.";
    }

    return "";
}


function validateQuantity(value) {

    if (value.trim() === "") {
        return "Inserisci la quantità.";
    }

    var quantity =Number(value);

    if (!Number.isInteger(quantity)) {
        return "La quantità deve essere un numero intero.";
    }

    if (quantity < 0) {
        return "La quantità non può essere negativa.";
    }

    return "";
}
function configureEmailAvailability() {

    var registrationForm =document.getElementById("registrationForm");

    if (registrationForm == null) {
        return;
    }

    var emailInput =document.getElementById("mailSpace");

    var messageElement =document.getElementById("emailAvailabilityMessage");

    if (emailInput == null ||messageElement == null) {

        return;
    }

    emailInput.addEventListener("change", function() {

        var email =emailInput.value.trim();

        var localError =validateEmail(email);

        if (localError !== "") {

            clearAvailabilityMessage(messageElement);

            return;
        }

        messageElement.textContent ="Controllo email in corso...";

        messageElement.className ="availability-message checking";

        var contextPath =document.body.dataset.contextPath || "";

        var url =contextPath+ "/check-email?mail="+ encodeURIComponent(email);

        fetch(url, {
            method: "GET",
            headers: {
                "Accept": "text/plain"
            }
        })
            .then(function(response) {

                if (!response.ok) {
                    throw new Error("Errore nella risposta del server");
                }

                return response.text();
            })
            .then(function(result) {

                if (result === "available") {

                    clearError(emailInput);

                    messageElement.textContent ="Email disponibile.";

                    messageElement.className ="availability-message available";

                } else if (result === "taken") {

                    clearAvailabilityMessage(messageElement);

                    showError(emailInput,"Questa email è già registrata.");

                } else {

                    clearAvailabilityMessage(messageElement);
                }
            })
            .catch(function(error) {

                messageElement.textContent ="Impossibile verificare l'email in questo momento.";

                messageElement.className ="availability-message not-available";

                console.error(error);
            });
    });


    emailInput.addEventListener("input", function() {

        clearAvailabilityMessage(messageElement);
    });
}


function clearAvailabilityMessage(messageElement) {

    messageElement.textContent = "";

    messageElement.className ="availability-message";
}