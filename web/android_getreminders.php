<?php
    include("conn.php");

    $container = "";
    $query = $conn->query("SELECT * FROM post_reminder ORDER BY ID DESC");

    $separator = "/";

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
        $id = $row["ID"];
        $announce = $row["remind"];
        $descrip = $row["description"];
        $organizer = $row["organizer_name"];
        $departy = $row["inclusion"];
        $date = $row["announce_date"];
        $time = $row["announce_time"];
        $status = $row["status_remind"];

        $container .= "#".$separator."$id".$separator."$announce".$separator."$descrip".$separator."$organizer".$separator."$departy".$separator."$date".$separator."$time".$separator."$status".$separator."";
    }

    echo  $container;  
?>