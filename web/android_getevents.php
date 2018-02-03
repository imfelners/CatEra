<?php
    include("conn.php");

    $container = "";
    $query = $conn->query("SELECT * FROM post_event ORDER BY ID DESC");

    $separator = "/";

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
        $id = $row["ID"];
        $event = $row["event"];
        $description = $row["description"];
        $organizer_name = $row["organizer_name"];
        $department = $row["inclusion"];
        $announce_date = $row["announce_date"];
        $announce_time = $row["announce_time"];
        $status_event = $row["status_event"];
		$event_color = $row['event_color'];
		
        $container .= "#".$separator
                    ."$id".$separator
                    ."$event".$separator
                    ."$description".$separator
                    ."$organizer_name".$separator
                    ."$department".$separator
                    ."$announce_date".$separator
                    ."$announce_time".$separator
                    ."$status_event".$separator
					."$event_color".$separator."";
    }

    echo  $container;  
?>