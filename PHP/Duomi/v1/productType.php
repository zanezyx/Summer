
<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';


// $json_string = file_get_contents("php://input");
// $obj = json_decode($json_string);

// 从表中提取信息的sql语句

$strsql = "select * from product_type";
//print $strsql;
// 执行sql查询
mysql_query("set names 'utf8'");
$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果
if($result)   
{   
    /*数据集 */
 
    $users=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            //echo $row['id'].'-----------'.$row['name'].$row['remark'].'</br>'; 
            $users[$i]=$row; 
            $i++; 
 
    } 
    //echo json_encode(array('productTypeList'=>$users)); 
	echo JSON(array('productTypeList'=>$users));
  
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