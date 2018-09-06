<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';


// 从表中提取信息的sql语句

$strsql = "select mobile from users";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

if($result)   
{   
  
//  $array=mysql_fetch_array($result,MYSQL_ASSOC);  
     
    /*数据集 */
 
    $users=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            //echo $row['id'].'-----------'.$row['name'].$row['password'].'</br>'; 
            $users[$i]=$row; 
            $i++; 
 
    } 
    echo JSON(array('userlist'=>$users)); 
	
}else{
	echo "1";
}   

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>





