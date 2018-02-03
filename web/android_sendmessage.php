<?php
    include("conn.php");

	$senderID = $_GET["senderID"];
	$receiverID = $_GET["receiverID"];
	$message = $_GET["message"];
	$timestamp = $_GET["timestamp"];
	$fromAdmin = $_GET["fromAdmin"];
	
	$sql = "INSERT INTO messages (senderID, receiverID, message, timestamp, fromAdmin)
	VALUES ($senderID, $receiverID, '$message', '$timestamp', $fromAdmin)";

	if (mysqli_query($conn, $sql)) {
		echo "New record created successfully";
	} else {
		echo "Error: " . $sql . "<br>" . mysqli_error($conn);
	}

	
?>