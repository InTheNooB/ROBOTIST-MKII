<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Robotist MKII</title>
    <script src="js/utils/jquery-3.6.0.min.js"></script>
    <script src="js/services/httpService.js"></script>
    <script src="js/ctrl/signinCtrl.js"></script>
    <link rel="stylesheet" href="./css/index.css">

</head>
<body>
    <div class="flex-container">
        <div class="content-container">
          <div class="form-container">
            <form id="loginForm">
              <h1>
                Sign In
              </h1>
              <br>
              <br>
              <span class="subtitle">USERNAME:</span>
              <br>
              <input type="text" name="username" value="" id="username">
              <br>
              <span class="subtitle">PASSWORD:</span>
              <br>
              <input type="password" name="password" value="" id="password">
              <br><br>
              <input type="submit" value="SIGN IN" class="submit-btn" id="signin">
              <input type="submit" value="HOME" class="submit-btn" id="home">
            </form>
          </div>
        </div>
      </div>
</body>
</html>