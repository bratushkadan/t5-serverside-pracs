<?php

header('Content-Type: application/json');

$servername = "db";
$username = "root";
$password = "123";
$dbname = "appDB";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $data = json_decode(file_get_contents('php://input'), true);

  $name = $data['name'];
  $description = $data['description'];

  $sql = "INSERT INTO courses (name, description) VALUES (?, ?)";
  $stmt = $conn->prepare($sql);
  $stmt->bind_param("ss", $name, $description);
  $result = $stmt->execute();

  if ($result) {
      echo json_encode(array("message" => "Course created successfully."));
  } else {
      echo json_encode(array("message" => "Failed to create course."));
  }

  $stmt->close();
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
  $sql = "SELECT * FROM courses";
  $result = $conn->query($sql);

  $courses = array();

  if ($result->num_rows > 0) {
      while ($row = $result->fetch_assoc()) {
          $courses[] = $row;
      }
  }

  echo json_encode($courses);
}

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
  $courseId = $_GET['id'];

  $sql = "DELETE FROM courses WHERE id = ?";
  $stmt = $conn->prepare($sql);
  $stmt->bind_param("i", $courseId);
  $result = $stmt->execute();

  if ($result) {
      echo json_encode(array("message" => "Course deleted successfully."));
  } else {
      echo json_encode(array("message" => "Failed to delete course."));
  }

  $stmt->close();
}

$conn->close();
?>
