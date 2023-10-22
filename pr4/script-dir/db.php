<?php
$host = 'db';
$username = 'root';
$password = '123';
$dbName = 'appDB';

$mysqli = new mysqli($host, $username, $password, $dbName);

if ($mysqli->connect_errno) {
    echo 'Failed to connect to MySQL: ' . $mysqli->connect_error;
    exit();
}
?>
