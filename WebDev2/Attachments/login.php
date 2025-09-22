<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="style.css"> <!-- Update with the actual CSS path -->
</head>
<body>
    <nav class="navbar">
        <div>
            <a href="home.php">Home</a>
            <a href="login.php">Login</a>
            <a href="register.php">Register</a>
            <?php session_start(); if (isset($_SESSION['username'])): ?>
                <a href="logout.php">Logout</a>
            <?php endif; ?>
        </div>
    </nav>

    <form class="form1" action="login.php" method="POST">
        <label for="username">Username: </label>
        <input type="text" name="username" id="username" required><br><br>

        <label for="password">Password: </label>
        <input type="password" name="password" id="password" required><br><br>

        <input type="submit" value="Login" id="button">
    </form>

<?php
include "db.php"; // Ensure the file contains the database connection code

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Sanitize user input
    $username = mysqli_real_escape_string($conn, $_POST['username']);
    $password = $_POST['password'];

    // Query the database for user credentials
    $logincheck = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";
    $result = $conn->query($logincheck);

    if ($result->num_rows > 0) {

    
            // Store username in the session
            $_SESSION['username'] = $username;
            header("Location: Welcome.php");
            exit();
        } else {
            echo "Invalid password!";
        }
    } else {
        echo "User not found!";
    }


$conn->close();
?>
</body>
</html>
