<?php
    include("conn.php");

    $type = $_GET['type'];

    $container = "";
    $query = $conn->query("SELECT * FROM user_" . $type . " ORDER BY ID DESC");

    $separator = "/";

    $usrsCount=$query->num_rows;

    while($row=mysqli_fetch_array($query)){
        $id = $row["ID"];
        $empid = $row[$type == "admin"? "employee_ID" : ($type == "faculty"? "employee_id" : "student_number")];
        $pw = $row["password"];
        $fn = $row["first_name"];
        $mn = $row["middle_name"];
        $ln = $row["last_name"];

        $container .= "#".$separator."$id".$separator."$empid".$separator."$pw".$separator."$fn".$separator."$mn".$separator."$ln".$separator."";
    }

    echo  $container;  
?>