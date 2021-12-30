/**
 * Name : Sign In Controller
 * Author : Elwan Mayencourt
 * Version : 1.0
 * Date : 07.05.2021
 */
$(document).ready(function () {
    $("#loginForm").submit(function (e) {
        e.preventDefault();
    });


    $("#signin").click(function () {
        let username = $("#username").val();
        let password = $("#password").val();
        signInRequest(username, password, signinCallback);
    });
    $("#home").click(function () {
        window.location.replace("./index.jsp");
    });


});

/**
 * 
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 * @returns {undefined}
 */
function signinCallback(data, status, xhr) {
    if (xhr.status != 200) {
        changeColor("signin", "#38d39f", "red");
    }
    else {
        changeColor("signin", "#38d39f", "green");
    }
}

/*
 * Change componenent color with animation
 * @param {type} component
 * @param {type} baseColor
 * @param {type} color
 */
function changeColor(component, baseColor, color) {
    $("#" + component).css("backgroundColor", color)
        .animate({ backgroundColor: baseColor }, "slow", null, function () {
            jQuery(this).css("backgroundColor", baseColor);
        });
}