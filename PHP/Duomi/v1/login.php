<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

#$json_string = file_get_contents("php://input");
#$obj = json_decode($json_string);
#$username = $obj -> username;
#$password = $obj -> password;

$username = $_GET['mobile'];
$password = $_GET['password'];

// 从表中提取信息的sql语句

$strsql = "select * from users where mobile=" . "\"" . $username . "\"" . " and password=" . "\"" . $password . "\"";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

$row=mysql_fetch_row($result,MYSQL_ASSOC);      

if ($row) {
	echo JSON(array('user'=>$row));  ;
} else {
	echo "";
}
$score = $row['score'];
$score1 = $score+5;
$strsql = "update users set score=" . $score1 . " where id=".$row['id'];;
print $strsql;
mysql_select_db($mysql_database, $conn);
$result = mysql_db_query($mysql_database, $strsql, $conn); 
// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>