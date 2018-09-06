<?php

// $json_string = file_get_contents("php://input");
// $obj = json_decode($json_string);
// 
// $username = $obj -> username;
// $password = $obj -> password;
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

$strsql = "select userid,name,password from users";
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
    echo json_encode(array('userlist'=>$users)); 

  
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