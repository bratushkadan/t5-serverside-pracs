<?php
include "db.php";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = $_POST['id'];
    $name = $_POST['name'];
    $age = $_POST['age'];

    $checkQuery = "SELECT 1 FROM students WHERE id=$id";
    $checkResult = $mysqli->query($checkQuery);

    $student_exists = $checkResult->num_rows !== 0;

    if ($student_exists) {
      $sql = "UPDATE students SET name='$name', age=$age WHERE id=$id";
      $result = $mysqli->query($sql);
  
      if ($result) {
          echo 'Student updated successfully';
      } else {
          echo 'Error updating student: ' . $mysqli->error;
      }
    } else {
      echo 'Student with ID ' . $id . ' does not exist';
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Update Student</title>
</head>
<body>
    <h1>Update Student</h1>
    <form method="POST" action="">
        <label for="id">ID:</label>
        <input type="number" name="id" required><br>

        <label for="name">Name:</label>
        <input type="text" name="name" required><br>

        <label for="age">Age:</label>
        <input type="number" name="age" required><br>

        <input type="submit" value="Update">
    </form>
</body>
</html>
