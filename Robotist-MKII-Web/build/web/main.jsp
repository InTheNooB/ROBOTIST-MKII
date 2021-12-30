<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Main - Robotist MKII</title>
    <script src="js/utils/jquery-3.6.0.min.js"></script>
    <script src="js/services/httpService.js"></script>
    <script src="js/ctrl/mainCtrl.js"></script>
    <link rel="stylesheet" href="./css/main.css">
</head>

<body>
    <canvas id="myCanvas">
    </canvas>
    <div class="mainDiv">
        <h1>Robotist MKII</h1>
        <select  id="sequences">
          </select>
    </div>
    <p id='data'></p>
    <a href="javascript:logout();">Logout</a>


</body>

</html>