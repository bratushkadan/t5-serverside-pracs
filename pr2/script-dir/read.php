<?php
include "db.php";

$sql = "SELECT * FROM students";
$result = $mysqli->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo 'ID: ' . $row['id'] . '<br>';
        echo 'Name: ' . $row['name'] . '<br>';
        echo 'Age: ' . $row['age'] . '<br><br>';
    }
} else {
    echo 'No students found';
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Read Students</title>
</head>
<body>
    <h1>Read Students</h1>
</body>
</html>
