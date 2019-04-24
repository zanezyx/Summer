
<?php
$mysql_server_name = "localhost";
//数据库服务器名称

$mysql_username = "root";
// 连接数据库用户名

$mysql_password = "123456";
// 连接数据库密码

$mysql_database = "pigsite";
// 数据库的名字

// 连接到数据库

$conn = mysql_connect($mysql_server_name, $mysql_username, $mysql_password);

mysql_query("set names 'utf8'");
?>