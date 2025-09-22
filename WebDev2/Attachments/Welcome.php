<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Reservation System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>Welcome</h1>
            <nav> 
                <a href="logout.php">LogOut</a>
            </nav>
        </div>
    </header>
    <?php 
    session_start();
    echo "Your session ID is: " . session_id();
    ?>

</body>
</html>
