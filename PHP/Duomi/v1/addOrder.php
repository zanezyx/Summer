<?php
include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

$customId = $_GET['customId'];
$addressId = $_GET['addressId'];
$deliveryFee = $_GET['deliveryFee'];
$paymentType = $_GET['paymentType'];
$totalPrice = $_GET['totalPrice'];
$cartIds = $_GET['cartIds'];
$name = $_GET['name'];
$mobile = $_GET['mobile'];
$receiverName = $_GET['receiverName'];
$receiverMobile = $_GET['receiverMobile'];
$receiverAddress = $_GET['receiverAddress'];
$cartIdArray = explode(".", $cartIds);

$orderRemark = "";
print_r($cartIdArray);
foreach ($cartIdArray as &$value) {
	$strsql = "select * from cart where id=" . $value;
	$result = mysql_db_query($mysql_database, $strsql, $conn);
	$row=mysql_fetch_row($result,MYSQL_ASSOC);  
	$orderRemark .= $row['productName'];
	$orderRemark .= " ";
}


// 从表中提取信息的sql语句

$strsql = "insert into t_order(customId	,addressId	,totalPrice,paymentType,orderRemark,
				name,mobile,receiverName,receiverMobile,receiverAddress) 
				values(" . $customId . "," . $addressId . "," . $totalPrice . "," 
				. $paymentType .","."'".$orderRemark."',"."'".$name."',"."'".$mobile."','".$receiverName.
				"','".$receiverMobile."','".$receiverAddress."')";
// echo $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

if ($result) {
	echo "success";
	$id = mysql_insert_id();
	// print_r($id);
} else {
	echo "failed";
}

foreach ($cartIdArray as &$value) {
	$strsql = "select * from cart where id=" . $value;
	$result = mysql_db_query($mysql_database, $strsql, $conn);

	// 获取查询结果

	if ($result) {
		/*单条数据*/
		$cartItem = mysql_fetch_row($result, MYSQL_ASSOC);
		$productId = $cartItem['productId'];
		$productName = $cartItem['productName'];
		$productImageUrl = $cartItem['productLogo'];
		$amout = $cartItem['amout'];
		$orderId = $id;
		$productPrice = $cartItem['price'];
		$strsql = "insert into t_order_item(productId,productName,productImageUrl,amout,orderId,productPrice) 
				values('" . $productId . "'," . "'" . $productName . "'," . "'" . $productImageUrl . "'," . $amout . "," . $orderId . "," . $productPrice . ")";
		$result1 = mysql_db_query($mysql_database, $strsql, $conn);
		if ($result1) {
			//echo "success";
			$strsql = "delete from cart where id=" . $value;
			$result2 = mysql_db_query($mysql_database, $strsql, $conn);
		} else {
			//echo "failed";
		}
	}
}

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>

