const toggleSort = (item) => {
    item.value = (item.value === "ASC") ? "DESC" : "ASC";
    const label = item.nextElementSibling;
    const icon = label.children.item(0);
    const isAsc = icon.classList.contains("bi-sort-up-alt");

    if (isAsc) {
        $(icon).removeClass("bi-sort-up-alt");
        $(icon).addClass("bi-sort-down-alt");
        return;
    }
    $(icon).removeClass("bi-sort-down-alt");
    $(icon).addClass("bi-sort-up-alt");
}
const doLogin = () => {
    const login = $('#typeEmailX').val();
    const alert = $('#loginError')
    const password = $('#typePasswordX').val();
    $.ajax({
        url: window.location.pathname,
        type: 'POST',
        dataType: 'json',
        data: {login: login, password: password},
        success: function (data) {
            if (data.redirect) {
                window.location.href = data.redirect;
            } else {
                alert.removeClass("d-none");
            }
        }
    });
}
const recoverAccess = () => {
    const email = $('#typeEmailX');
    $.ajax({
        url: window.location.pathname,
        type: 'POST',
        data: {email: email.val()},
        success: function (data) {
            $("#login-btn").remove();
            $(".alert-success").removeClass("d-none");
            email.attr("disabled", true);
            $(".message-success").delay(1300).show(0);
        }
    });
}
const togglePassword = () => {
    const toggleIcon = $('#togglePasswordIcon');
    const password = document.querySelector("#typePasswordX")

    const type = password.getAttribute('type') === "password" ? "text" : "password";
    password.setAttribute("type", type);
    toggleIcon.toggleClass("bi-eye-slash-fill bi-eye-fill")

}

const countdown = (time, out) => {
    setInterval(function () {
           if(time > 0) {
               out.html("<span>" + time-- + "</span>");
           }
    }
    , 1000);
}
const validateEmail = (email) => {
    return email.match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
};

const validatePassword = (password) => {
    return password.match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/);
}
const validateDate = (date) => {
    if ((!date.value || date.value === "")) {
        return false;
    }
    const inputDate = new Date(date.value).getTime();
    const today = new Date().getTime();
    const min = $('.date-min');
    if (min && min.val() !== "") {
        return inputDate > today && inputDate >= new Date(min.val());
    }
    return (inputDate > today);
}
const validateTextField = (field) => {
    if (field.getAttribute('id') === "typePasswordX") {
        return validatePassword(field.value);
    }
    return field.value.length > 1;
}
const validateField = (field) => {
    switch (field.getAttribute('type')) {
        case "text":
            return validateTextField(field);
        case "email":
            return validateEmail(field.value);
        case "password":
            if (field.classList.contains('light-validation')) {
                return field.value.length > 0;
            }
            return validatePassword(field.value);
        case "datetime-local":
            return validateDate(field);
    }
}


const validation = (targetBtn) => {
    const fields = document.getElementsByClassName('validation')
    const targetButton = document.getElementById(targetBtn);
    let allFieldIsValid = true;
    for (let field of fields) {
        if (field.getAttribute('for') === targetBtn) {
            if (validateField(field)) {
                field.classList.remove("is-invalid");
            } else {
                allFieldIsValid = false;
            }
        }
    }
    if (allFieldIsValid) {
        targetButton.removeAttribute('disabled');
    } else {
        targetButton.setAttribute('disabled', 'true');
    }
}
const validationEventListener = (fields) => {
    Array.from(fields).forEach(field => {
        field.addEventListener('focusout', evt => {
            if (!validateField(field)) {
                field.classList.add('is-invalid')
            }
        })
    })
}
const formValidationEventListener = (forms) => {
    Array.from(forms).forEach(form => {
        form.addEventListener('input', evt => {
            const targetButton = form.getAttribute('button-target');
            validation(targetButton);
        }, false);
    })
}
$(function () {
    validationEventListener($('.validation'));
    formValidationEventListener($('.needs-validation'));
})

$(document).on('input', '.readable-slider', function () {
    $('.out-slider').html($(this).val());
});

$(function () {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
})

$(function () {
    $(".lang-option").click(function () {
        $.ajax({
            url: $('#contextPath').attr("data-ContextPath") + "/locale",
            type: "GET",
            data: {locale: this.getAttribute('value')}
        }).done(function () {
            window.location.reload();
        });
    });
});