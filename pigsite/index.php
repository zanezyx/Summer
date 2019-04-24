<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <style>
        .container{
             background-color: #868686;
             width: 100%;
             height: 300px;
             margin-top: 50px;
         }
         .radios{
             position: absolute;
             bottom: 50px;
			 margin:0 auto
         }
     </style>
</head>
<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$first_txt1="该二维码首次扫码！";
$first_txt2="长按下方二维码关注贝亲公众号，";
$first_txt3="有机会获取现金红包哦！";

$txt11="该二维码已被扫描过";
$txt12="次，最后一次扫描时间：";
$txt2= date("Y-m-d H:i:s");
$txt3="如需帮助请拨打：800-820-3376（座机）、400-820-3367（手机）进行咨询。长按下方二维码关注贝亲公众号，有机会获取现金红包哦！";

$dest_txt1 = $first_txt1;
$dest_txt2 = $first_txt2;
$dest_txt3 = $first_txt3;
$align = "center";

$pid = $_GET['pid'];

// 从表中提取信息的sql语句

$strsql = "select * from product where pid=" . "\"" . $pid . "\"";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

$row=mysql_fetch_row($result,MYSQL_ASSOC);      
$times = $row['times'];
if ($row) {
	//echo JSON(array('user'=>$row));  ;
	$times = $times+1;
	$strsql = "update product set times=" . $times . " where id=".$row['id'];;
	//print $strsql;
	mysql_select_db($mysql_database, $conn);
	$result = mysql_db_query($mysql_database, $strsql, $conn); 
	$dest_txt1 = $txt11.$times.$txt12;
	$dest_txt2 = $txt2;
	$dest_txt3 = $txt3;
	$align = "left";
	
} else {
	//echo "";
	$dest_txt1 = $first_txt1;
	$dest_txt2 = $first_txt2;
	$dest_txt3 = $first_txt3;
	$strsql = "insert into product(pid) values(".$pid.")";
	mysql_select_db($mysql_database, $conn);
	mysql_query($strsql);
}


// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>

<body background="background1.png" style="width:1080px" alt="">
<p><img src="logo.png" width="260" height="146" /></p>
<div>
<p align=<?php echo $align ?> style="color:#FC0;font-size:35px" >
<?php echo $dest_txt1; ?>
<br>
<br>
<?php echo $dest_txt2; ?>
<br>
<br>
<?php echo $dest_txt3; ?>
</p>
</div>
</body>
</html>