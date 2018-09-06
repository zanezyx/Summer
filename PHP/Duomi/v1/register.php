<?php

#$json_string = file_get_contents("php://input");
#$obj = json_decode($json_string);
#$username = $obj -> username;
#$password = $obj -> password;

$username = $_GET['mobile'] ;
$password = $_GET['password'] ;

$mysql_server_name = "localhost";
//数据库服务器名称

$mysql_username = "root";
// 连接数据库用户名

$mysql_password = "root";
// 连接数据库密码

$mysql_database = "balilai";
// 数据库的名字

// 连接到数据库

$conn = mysql_connect($mysql_server_name, $mysql_username, $mysql_password);

// 从表中提取信息的sql语句

$strsql = "select * from users where mobile="."\"".$username."\"";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

$row = mysql_fetch_row($result);

if ($row) {
	echo "1";
} else {
	$strsql = "insert into users(mobile,password) values("."\"".$username."\"".","."\"".$password."\"".")";
	
	mysql_select_db($mysql_database, $conn);
	// echo $strsql;
	if(mysql_query($strsql))
	{
		echo "0";
	}else{
		echo "2";
	}
}

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>