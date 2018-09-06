<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$productId = $_GET['productId'] ;
$customId = $_GET['customId'] ;
$customMobile = $_GET['customMobile'] ;
$productName = $_GET['productName'] ;
$price = $_GET['price'] ;
$amout = $_GET['amout'] ;
$productLogo = $_GET['productLogo'] ;

// 从表中提取信息的sql语句

$strsql = "insert into cart(productId,amout,customId,customMobile,productName,price,productLogo) 
				values('".$productId."',". $amout.",".$customId.","."'".$customMobile."',"."'".$productName."',"
				.$price.","."'".$productLogo."'".")";
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