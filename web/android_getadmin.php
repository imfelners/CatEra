<?php
    include("conn.php");

    $container = "";
    $query = $conn->query("SELECT * FROM user_admin ORDER BY ID DESC");

    $separator = "/";

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
		$id = $row["ID"];
        $empid = $row["employee_ID"];
        $pw = $row["password"];
        $fn = $row["first_name"];
        $mn = $row["middle_name"];
        $ln = $row["last_name"];

        $container .= "#".$separator."$id".$separator."$empid".$separator."$pw".$separator."$fn".$separator."$mn".$separator."$ln".$separator."admin";
    }

    $query = $conn->query("SELECT * FROM user_secretary ORDER BY ID DESC");

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
		$id = $row["ID"];
        $empid = $row["employee_id"];
        $pw = $row["password"];
        $fn = $row["first_Name"];
        $mn = $row["middle_Name"];
        $ln = $row["last_Name"];

        $container .= "#".$separator."$id".$separator."$empid".$separator."$pw".$separator."$fn".$separator."$mn".$separator."$ln".$separator."secretary";
    }

    echo  $container;  
?>