<?php
  define('HOST','mysql.idhostinger.com');
  define('USER','u260115757_user');
  define('PASS','123456');
  define('DB','u260115757_db');
  $con = mysqli_connect(HOST,USER,PASS,DB);
  $id = $_POST['id'];
  $sql = "DELETE FROM person WHERE id='$id'";
//echo $sql;
  if(mysqli_query($con,$sql)){
    echo 'success';
  }
  else{
   echo 'failure';
 }
  mysqli_close($con);
?>
