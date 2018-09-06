<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$id = $_GET['typeId'];


// 从表中提取信息的sql语句

if($id==1)
{
	$strsql = "select * from product where gender = 1";
}else if($id==2)
{
	$strsql = "select * from product where gender = 0";
}
else{
	$strsql = "select * from product where productType =".$id;
}

//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

if($result)   
{         
    /*数据集 */
    $products=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            $products[$i]=$row; 
            $i++; 
    } 
    echo JSON(array('productList'=>$products)); 
}else{
	echo "failed";
}   
// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>


