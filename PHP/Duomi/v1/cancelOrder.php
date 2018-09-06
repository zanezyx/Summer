<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$id = $_GET['id'] ;


// 从表中提取信息的sql语句

$strsql = "update t_order set orderStatus=2"." where id=".$id;
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

if($result)   
{   
  	echo "success";
}else{
	echo "failed";
}   

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>