<?php
    include("conn.php");

	$senderID = $_GET['senderID'];
	$receiverID = $_GET['receiverID'];
	
    $container = "";
    $query = $conn->query("SELECT * FROM messages where senderID = $senderID and receiverID = $receiverID;");

    $separator = "/";

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
        $id = $row["ID"];
        $senderID = $row["senderID"];
        $receiverID = $row["receiverID"];
        $message = $row["message"];
        $timestamp = $row["timestamp"];
        $fromAdmin = $row["fromAdmin"];
		
        $container .= "#".$separator
                    ."$id".$separator
                    ."$senderID".$separator
                    ."$receiverID".$separator
                    ."$message".$separator
                    ."$timestamp".$separator
                    ."$fromAdmin".$separator."";
    }

    echo  $container;  
?>