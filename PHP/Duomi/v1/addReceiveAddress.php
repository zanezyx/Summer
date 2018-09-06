<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$customId = $_GET['customId'];
$receiveName = $_GET['receiveName'];
$mobile = $_GET['mobile'];
$province = $_GET['province'];
$city = $_GET['city'];
$county = $_GET['county'];
$address1 = $_GET['address1'];


// 从表中提取信息的sql语句

$strsql = "insert into receive_address(customId,province,city,county,address,receiveName,mobile) 
				values(".$customId.","."'".$province."'".","."'".$city."'".","."'".$county."',"."'".$address1."',"
				."'".$receiveName."',"."'".$mobile."'".")";
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