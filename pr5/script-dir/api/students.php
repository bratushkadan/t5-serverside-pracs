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
    $age = $data['age'];

    $sql = "INSERT INTO students (name, age) VALUES (?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si", $name, $age);
    $result = $stmt->execute();

    if ($result) {
        echo json_encode(array("message" => "Student created successfully."));
    } else {
        echo json_encode(array("message" => "Failed to create student."));
    }

    $stmt->close();
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $sql = "SELECT * FROM students";
    $result = $conn->query($sql);

    $students = array();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $students[] = $row;
        }
    }

    echo json_encode($students);
}

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    $studentId = $_GET['id'] ?? '';
    $studentId = (int) $studentId;

    $sql = "DELETE FROM students WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $studentId);
    $result = $stmt->execute();

    if ($result) {
        echo json_encode(array("message" => "Student deleted successfully."));
    } else {
        echo json_encode(array("message" => "Failed to delete student."));
    }

    $stmt->close();
}

$conn->close();
?>