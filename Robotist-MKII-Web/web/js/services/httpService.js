/**
 * Name : Http Service
 * Author : Elwan Mayencourt
 * Version : 1.0
 * Date : 07.05.2021
 */

/**
 * Url to the Servlet
 * @type String
 */
const SERVLET_PATH = "http://WSTEMFA45-13:8080/300231/G05/Servlet"


/**
 * Send sign in request
 * @param {type} username
 * @param {type} password
 * @param {type} callback
 */
function signInRequest(username, password, callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'POST',
        dataType: "json",
        data: { action: "register", username: username, password: password },
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}
/**
 * Send login request
 * @param {type} username
 * @param {type} password
 * @param {type} callback
 */
function loginRequest(username, password, callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'POST',
        dataType: "json",
        data: { action: "login", username: username, password: password },
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}
/**
 * Send logout  request
 * @param {type} callback
 */
function logoutRequest(callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'POST',
        dataType: "json",
        data: { action: "logout" },
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}

/**
 * Send get user sequence request
 * @param {type} callback
 */
function getUserSequences(callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'GET',
        dataType: "json",
        data: { action: "getUserSequences" },
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}

/**
 * Send move request
 * @param {type} x
 * @param {type} y
 * @param {type} callback
 */
function moveRequest(x,y,callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'POST',
        data: { action: "moveRobot", leftTrackSpeed: x, rightTrackSpeed: y },
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}
/**
 * Send play sequence request
 * @param {type} sequence
 * @param {type} callback
 */
function playSequenceRequest(sequence,callback) {
    $.ajax({
        url: SERVLET_PATH,
        type: 'POST',
        dataType: "json",
        data: { action: "playSequence", sequence: sequence},
        success: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
        error: function (data, textStatus, xhr) {
            callback(data, textStatus, xhr);
        },
    });
}