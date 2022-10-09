
const togglePassword = () => {
    const toggleIcon = $('#togglePasswordIcon');
    const password = document.querySelector("#typePasswordX")

    const type = password.getAttribute('type') === "password" ? "text" : "password";
    password.setAttribute("type", type);

    toggleIcon.toggleClass("bi-eye-slash-fill bi-eye-fill")

}



const validation = (targetBtn) => {
    const fields = document.getElementsByClassName('validation')
    const targetButton = document.getElementById(targetBtn);
    let allFieldIsValid = true;
    for (let field of fields) {
        if(field.getAttribute('for') === targetBtn) {
            if (validateField(field)) {
                field.classList.remove("is-invalid");
            } else {
                allFieldIsValid = false;
            }
        }
    }
    if(allFieldIsValid) {
        targetButton.removeAttribute('disabled');
    } else {
        targetButton.setAttribute('disabled', 'true');
    }
console.log(targetButton);
}

$(function (){
    $(".validation").focusout(function () {
        if(!validateField(this)) {
            this.classList.add('is-invalid');
        }
    })
})

$(function (){
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('input', evt => {
            const targetBtn = form.getAttribute('button-target')
            validation(targetBtn)
        }, false);
    })
    });


const validateEmail = (email) => {
    return email.match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
};

const  validatePassword = (password) => {
    return password.match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/);
}

const validateTextField = (field) => {
    if(field.getAttribute('id') === "typePasswordX") {
        return validatePassword(field.value);
    }

    return field.value.length > 1;
}

const validateField = (field) => {

    switch (field.getAttribute('type')) {
        case "text":
            return validateTextField(field);
        case "email":
           return  validateEmail(field.value);
        case "password":
            if(field.classList.contains('light-validation')) {
                return field.value.length > 0;
            }
            return  validatePassword(field.value);
    }
}
