/**
 * Name : Index Controller
 * Author : Elwan Mayencourt
 * Version : 1.0
 * Date : 07.05.2021
 */
$(document).ready(function () {
    $("#loginForm").submit(function (e) {
        e.preventDefault();
    });

    $("#login").click(function () {
        let username = $("#username").val();
        let password = $("#password").val();
        loginRequest(username, password, loginCallback);
    });

    $("#signin").click(function () {
        window.location.replace("./signin.jsp");

    });
});



/**
 * Callback for the login request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function loginCallback(data, status, xhr) {
    if (data["result"] === "OK") {
        changeColor("login", "#38d39f", "green");
        window.location.replace("./main.jsp");
    } else {
        changeColor("login", "#38d39f", "red");



    }
}
/**
 * Callback for the sign in request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function signinCallback(data, status, xhr) {
    if (xhr.status != 200) {
        changeColor("login", "#38d39f", "red");
    } else {
        changeColor("login", "#38d39f", "green");
    }
}
/**
 * Change componenent color with animation
 * @param {type} component
 * @param {type} baseColor
 * @param {type} color
 */
function changeColor(component, baseColor, color) {
    $("#" + component).css("backgroundColor", color)
            .animate({backgroundColor: baseColor}, "slow", null, function () {
                jQuery(this).css("backgroundColor", baseColor);
            });
}