<?php
include "db.php";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'];
    $age = $_POST['age'];

    $sql = "INSERT INTO students (name, age) VALUES ('$name', $age)";
    $result = $mysqli->query($sql);

    if ($result) {
        echo 'Student created successfully';
    } else {
        echo 'Error creating student: ' . $mysqli->error;
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Create Student</title>
</head>
<body>
    <h1>Create Student</h1>
    <form method="POST" action="">
        <label for="name">Name:</label>
        <input type="text" name="name" required><br>

        <label for="age">Age:</label>
        <input type="number" name="age" required><br>

        <input type="submit" value="Create">
    </form>
</body>
</html>
