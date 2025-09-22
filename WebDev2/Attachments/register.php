<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h2>Register</h2>
        <form method="POST">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <input type="password" name="confirm_password" placeholder="Confirm Password" required>
            <button type="submit">Register</button>
        </form>
    </div>

<?php
include "db.php";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Get and sanitize input
    $username = mysqli_real_escape_string($conn, $_POST['username']);
    $password = $_POST['password'];
    $confirm_password = $_POST['confirm_password'];

    // Validate inputs
    if (empty($username) || empty($password) || empty($confirm_password)) {
        echo "<p style='color:red;'>All fields are required.</p>";
        exit();
    }

    if ($password !== $confirm_password) {
        echo "<p style='color:red;'>Password and confirm password do not match.</p>";
        exit();
    }

    
    $getuser = "SELECT username FROM users WHERE username = '$username'";
    $result = $conn->query($getuser);

    if ($result && $result->num_rows > 0) {
        echo "Username already exists. Please choose another one.</p>";
        exit();
    }



    $sql = "INSERT INTO users (username, password) VALUES ('$username', '$password')";
    if ($conn->query($sql) === TRUE) {
        echo "Registration successful! You can now <a href='login.php'>log in</a>.</p>";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error . "</p>";
    }
}

$conn->close();
?>
</body>
</html>
