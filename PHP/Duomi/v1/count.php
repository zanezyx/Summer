<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$imei = $_GET['imei'];

// 从表中提取信息的sql语句

$strsql = "select * from count_times where imei=" . "'" . $imei . "'";
print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

$row = mysql_fetch_row($result, MYSQL_ASSOC);

if ($row) {
	$times = $row['times'];
	$times = $times + 1;
	$strsql = "update count_times set times=" . $times . " where id=" . $row['id'];
	mysql_select_db($mysql_database, $conn);
	$result = mysql_db_query($mysql_database, $strsql, $conn);
	echo "0";
} else {

	$strsql = "insert into count_times(imei,times) values(" . "'" . $imei . "'" . ","."'" . 1 . "')";
	mysql_select_db($mysql_database, $conn);
	echo $strsql;
	$result = mysql_db_query($mysql_database, $strsql, $conn);
	if ($result) {
		echo "1";
	} else {
		echo "2";
	}
}

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>