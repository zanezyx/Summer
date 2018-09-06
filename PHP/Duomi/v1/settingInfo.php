<?php

include 'header.php';
include 'jsonUtil.php';

$settings=array('latestVersionCode'=>'1.6', 'serviceMobile'=>'15119442416',
				'latestVersionURL'=>'http://192.168.1.100/Duomi/release/android/Duomi.apk'); 
$settingInfo=array('settingInfo'=>$settings); 
echo JSON($settingInfo);

?>