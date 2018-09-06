
<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

// 从表中提取信息的sql语句
$newList=array(); 
$hotSaleList=array(); 

$strsql = "select * from product where isNew>0";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果
if($result)   
{   
    /*数据集 */
    //$users=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            //echo $row['id'].'-----------'.$row['name'].$row['remark'].'</br>'; 
            $newList[$i]=$row; 
			//echo $newList[i];
            $i++; 
 
    } 
	//echo JSON(array('newList'=>$users));
} 

$strsql = "select * from product where hotSales>0";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果
if($result)   
{   
    /*数据集 */
    //$users=array(); 
    $i=0; 
    while($row=mysql_fetch_array($result,MYSQL_ASSOC)){ 
 
            //echo $row['id'].'-----------'.$row['name'].$row['remark'].'</br>'; 
            $hotSaleList[$i]=$row; 
            $i++; 
 
    } 
	
} 

echo JSON(array('newList'=>$newList, 'hotSales'=>$hotSaleList));



// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>





