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
  
    $studentId = $data['student_id'];
    $courseId = $data['course_id'];
  
    $sql = "INSERT INTO course_enrollment (student_id, course_id) VALUES (?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $studentId, $courseId);
    $result = $stmt->execute();
  
    if ($result) {
        echo json_encode(array("message" => "Enrollment created successfully."));
    } else {
        echo json_encode(array("message" => "Failed to create enrollment."));
    }
  
    $stmt->close();
  }
  
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $sql = "SELECT ce.id AS enrollment_id, ce.student_id, ce.course_id, s.name AS student_name, c.name AS course_name, c.description AS course_description FROM course_enrollment AS ce JOIN students s ON student_id = s.id JOIN courses c ON course_id = c.id";
    $result = $conn->query($sql);
  
    $enrollments = array();
  
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $enrollments[] = $row;
        }
    }
  
    echo json_encode($enrollments);
  }
  
  if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    $data = json_decode(file_get_contents('php://input'), true);
  
    $studentId = $data['student_id'];
    $courseId = $data['course_id'];
  
    $sql = "DELETE FROM course_enrollment WHERE student_id = ? AND course_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $studentId, $courseId);
    $result = $stmt->execute();
  
    if ($result) {
        echo json_encode(array("message" => "Enrollment deleted successfully."));
    } else {
        echo json_encode(array("message" => "Failed to delete enrollment."));
    }
  
    $stmt->close();
  }

$conn->close();
?>

