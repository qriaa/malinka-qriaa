
var radios = document.querySelectorAll('input[type=radio]');
var checked = document.querySelectorAll('input[type=radio]:checked');
var btn = document.querySelector('[type=submit]');
var btn_div = document.getElementById('order-basket');

var error_not_selected = document.getElementById('error_not_selected');
var empty_input = document.getElementById('empty_input');

var address_input = document.querySelectorAll('.address_input');
var selected_input = document.getElementById('select-parcel-locker');
var person_input = document.getElementById('pickup-time');

if(!checked.length){
    btn.setAttribute("disabled", "disabled");
    btn_div.classList.add("add-to-cart-empty")
}

btn_div.addEventListener('click', function () {
    if (btn_div.classList.contains("add-to-cart-empty")) {
        error_not_selected.style.display = "block";
    }
});

function checkInputs() {
    let isNotEmpty = true;

    if (checked[0].id.localeCompare("standard") === 0) {

        address_input.forEach(function (ad_el) {

            if(ad_el.value.trim() === "") {
                isNotEmpty = false;
            }
        })
    } else if (checked[0].id.localeCompare("person") === 0) {

        if (person_input.value.trim() === "") {
            console.log(person_input.value)
            isNotEmpty = false;
        }
    }

    if (isNotEmpty) {
        empty_input.style.display = "none";

        btn.removeAttribute("disabled");
        btn_div.classList.remove("add-to-cart-empty")
    } else {
        empty_input.style.display = "block";
    }

}

address_input.forEach(function (e) {
    e.addEventListener('change', function (el) {
        checkInputs()
    })
});

person_input.addEventListener('change', function (el) {
    checkInputs()
});

radios.forEach(function(el){
    el.addEventListener('click', function(){
        checked = document.querySelectorAll('input[type=radio]:checked');
        error_not_selected.style.display = "none";

        if(checked.length){
            checkInputs()
        }
    });
});

