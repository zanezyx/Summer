<?php
$json_string = file_get_contents("php://input");
$obj=json_decode($json_string); 
echo "start"; 
echo $obj->username; 
echo $obj->password; 


?>