<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$id = $_GET['id'];

// 从表中提取信息的sql语句

$strsql = "select * from t_order_item where orderId =".$id;
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

if($result)   
{   
  
    /*数据集 */
 
    $orderItems=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            //echo $row['id'].'-----------'.$row['name'].$row['password'].'</br>'; 
            $orderItems[$i]=$row; 
            $i++; 
 
    } 
    echo JSON(array('orderItems'=>$orderItems)); 
    /*单条数据*/  
  
    // $row=mysql_fetch_row($result,MYSQL_ASSOC);      
    // echo json_encode(array('jsonObj'=>$row));  
}else{
	echo "1";
}   

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>






