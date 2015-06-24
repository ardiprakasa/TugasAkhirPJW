<?php
  define('HOST','mysql.idhostinger.com');
  define('USER','u260115757_user');
  define('PASS','123456');
  define('DB','u260115757_db');
  $con = mysqli_connect(HOST,USER,PASS,DB);
  $name = $_POST['name'];
  $address = $_POST['address'];
  $nim = $_POST['nim'];
  $id = $_POST['id'];
  $sql = "UPDATE person SET name='$name', address='$address', nim='$nim' WHERE id='$id'";
  if(mysqli_query($con,$sql)){
    echo 'success';
  }
  else{
    echo 'failure';
  }
  mysqli_close($con);
?>
