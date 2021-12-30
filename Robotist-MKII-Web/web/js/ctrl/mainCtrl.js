/**
 * Name : Main Controller
 * Author : Elwan Mayencourt
 * Version : 1.0
 * Date : 07.05.2021
 */

$(document).ready(function () {
    loginRequest("", "", loginCallback);
})

var canvasHeight;
var canvasWidth;
var speedFactor;

var shipImg;


var startX;//for middle measurement

var joystick;
var joystickSize;
var isMoving;

var allButtons;

var oldX;
var oldY;

var totalX = 0;
var totalY = 0;

setTimeout(init, 300);
// init();
//Class for button, calculates all elipses/circles 
function Button(x, y, width) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.clickableWidth = this.width * 0.55;
    this.clickableHeight = this.width * 0.47;
    this.clickableX = this.x + 0.5 * (this.width - this.clickableWidth);
    this.sideDistanceFromTop = 0; //changes when clicked
}
//class for joystick
function Joystick(x, y, width, height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.centerX = this.x + (0.5 * this.width);
    this.centerY = this.y + (0.5 * this.height);
    this.stickThickness = 0.1 * this.width;

    //change when moved/clicked
    this.stickXChange = 0;
    this.stickYChange = 0;
    this.movementTouchStart = [];
    this.isHeldDown = false;
    this.ballCenterX = this.centerX - this.stickXChange;
    this.ballCenterY = this.centerY - this.stickYChange;

    //for animating ball back to rest position
    this.xChangeStart = 0;
    this.yChangeStart = 0;
    this.restoring = false;

    //directions can be derived from this 
    //e.g. changeAngle == 90(+- 1/2 of 45deg), direction = right
    this.changeAngle = 0;
}

var stickYChange = 0;
var stickXChange = 0;
var maxStickChange;
var minStickMovement;

var changeAngle;
// var angleInDegrees;

var xDifference;
var yDifference;
//Set canvas as percentage of window height and update ship images
function init() {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    //stop right click menu on canvas
    c.oncontextmenu = function () {
        return false;
    }
    isMoving = false;
    canvasHeight = 2 * window.innerHeight;
    canvasWidth = window.innerWidth;
    ctx.canvas.height = canvasHeight;
    ctx.canvas.width = canvasWidth;
    speedFactor = canvasWidth / 200;
    joystickSize = 0.1 * canvasWidth;
    maxStickChange = 0.25 * joystickSize;
    minStickMovement = 0.1 * joystickSize;


    //x, y, width, height
    var joystickOne = new Joystick(0.40 * canvasWidth, 0.15 * canvasHeight, joystickSize, joystickSize);

    allJoysticks = [];
    allJoysticks.push(joystickOne);

    // x, y, width
    allButtons = [];
    var newButton = new Button(0.5 * canvasWidth, 0.15 * canvasHeight + 0.45 * joystickSize, joystickSize);
    allButtons.push(newButton);

    //variables for determining which image used for banking animation
    movingUp = false;
    movingDown = false;
    movingLeft = false;
    movingRight = false;



    //event listeners for moving ship/button interaction
    document.addEventListener("mousedown", mouseDown, false);
    document.addEventListener("mouseup", mouseUp, false);
    document.addEventListener("mousemove", mouseMove, false);

    startGame();

}

function startGame() {
    startTime = new Date().getTime();

    requestAnimationFrame(animate); //Move to isloaded code

}



function draw() {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");

    //Clear canvas before redrawing objects (avoid duplicates)
    //	Need refactor to refresh only changed pixels
    ctx.clearRect(0, 0, canvasWidth, canvasHeight);
    // drawJoystickArea(ctx);
    drawJoystickStick(ctx);//need loop
}

function drawJoystickArea(context) {
    context.fillStyle = 'white';//red
    // context.fillRect(joystick[0],joystick[1],joystick[2],joystick[3]);
    context.fillRect(allJoysticks[0].x, allJoysticks[0].y, allJoysticks[0].width, allJoysticks[0].height);
}

var buttons = [];
//@ onclick, check collides button[0, 1, size]
//then change button height 
// var button1 = [buttonCenterX, baseCenterY-joystick[2]*0.08, ];
// x y size/width + height, colour? could add black to create darker parts

function drawJoystickStick(context) {
    for (var j = 0; j < allJoysticks.length; j++) {
        var joyStickToDraw = allJoysticks[j];
        var centerX = joyStickToDraw.x + 0.5 * joyStickToDraw.width;
        var centerY = joyStickToDraw.y + 0.5 * joyStickToDraw.height;
        var stickThickness = joyStickToDraw.stickThickness;
        var baseCenterY = centerY + 3 * stickThickness;

        context.lineWidth = 0.03 * joyStickToDraw.width;
        //only these change
        var circleCenterX = joyStickToDraw.centerX - joyStickToDraw.stickXChange;
        var circleCenterY = joyStickToDraw.centerY - joyStickToDraw.stickYChange;
        // var circleCenterX = joyStickToDraw.centerX;
        // var circleCenterY = joyStickToDraw.centerY;
        // var baseCenterY = centerY+5*stickThickness;
        // var baseCenterY = centerY;//would be circle base

        //main base side
        drawEllipse(centerX, baseCenterY + 0.5 * stickThickness, joyStickToDraw.width * 0.8, joyStickToDraw.height * 0.45, '#5b0b0a', context);
        context.stroke();
        //main base top
        drawEllipse(centerX, baseCenterY, joyStickToDraw.width * 0.8, joyStickToDraw.height * 0.45, 'maroon', context);
        context.stroke();
        //circular bottom of stick
        context.beginPath();
        context.arc(centerX, baseCenterY, stickThickness, 0, 2 * Math.PI);
        context.fillStyle = '#c1c1c1';
        context.fill();
        //the stick
        context.beginPath();
        context.moveTo(centerX - stickThickness, baseCenterY);
        context.lineTo(circleCenterX - stickThickness, circleCenterY);
        context.lineTo(circleCenterX + stickThickness, circleCenterY);
        context.lineTo(centerX + stickThickness, baseCenterY);
        context.closePath();
        context.fill();

        //ball on top, outer
        context.beginPath();
        context.fillStyle = '#c80012';//dark red
        context.strokeStyle = '#5b0b0a';
        context.arc(circleCenterX, circleCenterY, 0.25 * joyStickToDraw.width, 0, 2 * Math.PI);
        context.stroke();
        context.fill();
        //inner
        context.beginPath();
        context.fillStyle = '#e70026';//light red
        context.arc(circleCenterX - 0.015 * joyStickToDraw.width, circleCenterY - 0.015 * joyStickToDraw.height, 0.2 * joyStickToDraw.width, 0, 2 * Math.PI);
        context.fill();
        //light reflection
        // rotated ellipse	
        context.save();
        context.translate(circleCenterX - 0.8 * stickThickness, circleCenterY - 0.8 * stickThickness);
        context.rotate(-45 * Math.PI / 180);
        // drawEllipse(circleCenterX-0.8*stickThickness, circleCenterY-0.8*stickThickness, 0.25*joystick[2], 0.125*joystick[2], 'white', context);
        context.globalAlpha = 0.6;
        drawEllipse(0, 0, 0.25 * joyStickToDraw.width, 0.125 * joyStickToDraw.width, 'white', context);
        context.restore();
    }

    //BUTTONS

    //should loop through allButtons
    for (var i = 0; i < allButtons.length; i++) {
        // var buttonToDraw = allButtons[i];
        var buttonToDraw = allButtons[0];
        var buttonX = buttonToDraw.x;
        var buttonY = buttonToDraw.y;
        var buttonSize = buttonToDraw.width;
        var buttonCenterXY = [buttonX + 0.5 * buttonSize, buttonY + 0.5 * buttonSize];
        var topCenterY = buttonY + 0.2 * buttonSize;
        var clickableWidth = buttonToDraw.clickableWidth;
        var clickableHeight = clickableWidth.clickableHeight;
        var clickableX = clickableWidth.clickableX;
        context.lineWidth = 0.03 * buttonSize;
        //base side
        drawEllipse(buttonCenterXY[0], topCenterY + 0.22 * buttonSize, buttonSize, buttonSize * 0.5, '#5b0b0a', context);
        context.stroke();
        //base top
        drawEllipse(buttonCenterXY[0], topCenterY + 0.15 * buttonSize, buttonSize, buttonSize * 0.5, 'maroon', context);
        context.stroke();
        //button side
        drawEllipse(buttonCenterXY[0], topCenterY + 0.07 * buttonSize, buttonSize * 0.7, buttonSize * 0.40, '#c80012', context);
        //button top
        drawEllipse(buttonCenterXY[0], topCenterY + buttonToDraw.sideDistanceFromTop, buttonSize * 0.7, buttonSize * 0.40, '#e70026', context);
        context.lineWidth = 0.01 * buttonSize;
        context.stroke();
    }
}


function drawEllipse(centerX, centerY, width, height, colour, context) {

    context.beginPath();

    context.moveTo(centerX, centerY - height / 2); // A1

    context.bezierCurveTo(
            centerX + width / 2, centerY - height / 2, // C1
            centerX + width / 2, centerY + height / 2, // C2
            centerX, centerY + height / 2); // A2

    context.bezierCurveTo(
            centerX - width / 2, centerY + height / 2, // C3
            centerX - width / 2, centerY - height / 2, // C4
            centerX, centerY - height / 2); // A1

    context.fillStyle = colour;
    context.fill();
    context.closePath();
}

//var movementTouch;

//var movementTouchStart = [];
function mouseDown(e) {
    var button = allButtons[0];
    //check if in joystick
    // set start position, then check changes to decide which direction
    for (var j = 0; j < allJoysticks.length; j++) {
        var joystick = allJoysticks[j];
        // if(collides2(joystick[0], joystick[1], joystick[2], joystick[2], e.pageX, e.pageY, 3, 3))//i.e. no current movement touch
        if (collides2(joystick.x, joystick.y, joystick.width, joystick.height, e.pageX, e.pageY, 3, 3))//i.e. no current movement touch
        {
            joystick.movementTouchStart = [e.pageX, e.pageY];
            joystick.isHeldDown = true;//need one for each stick
            joystick.restoring = false;
            // restoringJoystick = false;
        }//collides with button
        // else if(collides2(button1[5],button1[1],button1[3],button1[4], e.pageX, e.pageY, 3, 3))
        //need loop through all
        else if (collides2(button.clickableX, button.y, button.clickableWidth, button.clickableHeight, e.pageX, e.pageY, 3, 3)) {
            buttonDown();
        }

    }
}

//should take index of button in allButtons for after collide detect
function buttonDown() {
    //animate the change
    allButtons[0].sideDistanceFromTop = 0.035 * allButtons[0].width;
    playSequence();

}
//should take index of button in allButtons for after collide detect
function buttonUp() {
    allButtons[0].sideDistanceFromTop = 0;
}

function mouseMove(e) {
    for (var i = 0; i < allJoysticks.length; i++) {
        var stickToMove = allJoysticks[i];
        if (stickToMove.isHeldDown) {
            isMoving = true;
            //do movement calculation
            var moveStartX = stickToMove.movementTouchStart[0];
            var moveStartY = stickToMove.movementTouchStart[1];
            var mouseX = e.pageX;
            var mouseY = e.pageY;
            //for tanx transfer
            stickToMove.changeAngle = Math.atan2(mouseX - moveStartX - 0.5 * 2, -(mouseY - moveStartY - 0.5 * 2));
            // angleInDegrees = changeAngle*(180/Math.PI);
            // changeAngle = changeAngle*(180/Math.PI);//to degrees

            stickXChange = moveStartX - mouseX;
            stickYChange = moveStartY - mouseY;

            if (stickXChange > maxStickChange) {
                stickXChange = maxStickChange;
            } else if (stickXChange < -maxStickChange) {
                stickXChange = -maxStickChange;
            }
            if (stickYChange > maxStickChange) {
                stickYChange = maxStickChange;
            } else if (stickYChange < -maxStickChange) {
                stickYChange = -maxStickChange;
            }
            stickToMove.stickXChange = stickXChange;
            stickToMove.stickYChange = stickYChange;
        }
    }

}
var restoringJoystick = false;
// function onTouchEnd(e) 
function mouseUp(e) {
    for (var j = 0; j < allJoysticks.length; j++) {
        // if()
        // {
        var stickToMove = allJoysticks[j];
        // }

        stickToMove.restoring = true;
        stickToMove.xChangeStart = stickToMove.stickXChange;
        stickToMove.yChangeStart = stickToMove.stickYChange;
        // animate x and y change back to 0

        stickToMove.isHeldDown = false;
        isMoving = false;//for flier
        // }
        //need to check collides with all buttons
        // need Button.released()

    }
    for (var i = 0; i < allButtons.length; i++) {
        buttonUp();
    }
}

function animate() {
    gameTime = new Date().getTime() - startTime;

    for (var j = 0; j < allJoysticks.length; j++) {
        var joystickToAnimate = allJoysticks[j];
        //transitions joystick ball back to starting position
        if (joystickToAnimate.restoring) {
            //should take delta
            joystickToAnimate.stickXChange -= joystickToAnimate.xChangeStart / 8;
            if (joystickToAnimate.xChangeStart < 0 && joystickToAnimate.stickXChange > 0) {
                joystickToAnimate.restoring = false;
                joystickToAnimate.stickXChange = 0;
            } else if (joystickToAnimate.xChangeStart > 0 && joystickToAnimate.stickXChange < 0) {
                joystickToAnimate.restoring = false;
                joystickToAnimate.stickXChange = 0;
            }

            joystickToAnimate.stickYChange -= joystickToAnimate.yChangeStart / 8;
            if (joystickToAnimate.yChangeStart < 0 && joystickToAnimate.stickYChange > 0) {
                joystickToAnimate.restoring = false;
                joystickToAnimate.stickYChange = 0;
            } else if (joystickToAnimate.yChangeStart > 0 && joystickToAnimate.stickYChange < 0) {
                joystickToAnimate.restoring = false;
                joystickToAnimate.stickYChange = 0;
            }
        }
        //move based on angle if(isMoving)
        // if(isMoving)
        if (isMoving) {
            totalX = Math.round(-allJoysticks[0].stickXChange, 2)
            totalY = Math.round(allJoysticks[0].stickYChange, 2)
            let newCoordinates = coordinatesToTracksSpeed(totalX, totalY);
            moveRequest(Math.round(totalX / 40 * 999), Math.round(totalY / 40 * 999), moveCallback);
            oldX = totalX;
            oldY = totalY;

            //document.getElementById('data').innerHTML = "x: " +x*-1+"  y: "+y + " angle: "+newCoordinates[1]+" distance : "+newCoordinates[0];
        }
    }

    draw();
    requestAnimationFrame(animate);
}


function playSequence() {
    console.log($("#sequences option:selected").val())
    playSequenceRequest($("#sequences option:selected").val(), playSequenceCallback);
}


function collides2(x1, y1, width1, height1, x2, y2, width2, height2) {
    if (x1 < x2 + width2 &&
            x1 + width1 > x2 &&
            y1 < y2 + height2 &&
            y1 + height1 > y2)
        return true;
}


function coordinatesToTracksSpeed(x, y) {
    let alpha = Math.atan(y / x) * (180 / Math.PI)
    if (x > 0 && y > 0) {

    } else if (x < 0 && y > 0) {
        alpha = alpha + 180
    } else if (x < 0 && y < 0) {
        alpha = alpha + 180
    } else if (x > 0 && y < 0) {
        alpha = alpha + 360
    }

    return [Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), 2), Math.round(alpha, 2)]
}


function logout() {
    logoutRequest(logoutCallback);
}
/**
 * Callback for the login request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function loginCallback(data, status, xhr) {
    console.log(data, xhr, status);
    if (data["result"] === "KO") {
        window.location.replace("./index.jsp");
    } else {
        getUserSequences(getUserSequencesCallback);
    }
}
/**
 * Callback for the move request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function moveCallback(data, status, xhr) {
    if (data["result"] == "OK") {
    } else {
        joyStickToDraw.x = 0;
        joyStickToDraw.y = 0;
    }
}
/**
 * Callback for the play sequence request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function playSequenceCallback(data, status, xhr) {
    console.log(data)
}
/**
 * Callback for the logout request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function logoutCallback(data, status, xhr) {
    if (data["result"] == "OK") {
        window.location.replace("./index.jsp");
    }
}
/**
 * Callback for the getSequence request
 * @param {type} data
 * @param {type} status
 * @param {type} xhr
 */
function getUserSequencesCallback(data, status, xhr) {
    if (xhr.status == 200) {
        $('#sequences').find('option').remove()
        var allSequences = data["sequences"];
        allSequences.forEach(function (sequence) {
            $("#sequences").append(new Option(sequence, sequence));

        });
    }
}