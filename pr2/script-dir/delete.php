<?php
include "db.php";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = $_POST['id'];

    $checkQuery = "SELECT 1 FROM students WHERE id=$id";
    $checkResult = $mysqli->query($checkQuery);

    $student_exists = $checkResult->num_rows !== 0;

    if ($student_exists) {
      $sql = "DELETE FROM students WHERE id=$id";
      $result = $mysqli->query($sql);
  
      if ($result) {
          echo 'Student deleted successfully';
      } else {
          echo 'Error deleting student: ' . $mysqli->error;
      }
    } else {
      echo 'Student with ID ' . $id . ' does not exist';
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Delete Student</title>
</head>
<body>
    <h1>Delete Student</h1>
    <form method="POST" action="">
        <label for="id">ID:</label>
        <input type="number" name="id" required><br>

        <input type="submit" value="Delete">
    </form>
</body>
</html>
