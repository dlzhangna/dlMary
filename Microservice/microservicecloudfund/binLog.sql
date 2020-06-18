C:\ProgramData\MySQL\MySQL Server 8.0\my.ini
C:\Program Files\MySQL\MySQL Server 8.0\bin
mysqld --defaults-file="C:\ProgramData\MySQL\MySQL Server 8.0\my.ini" --initialize --console
mysqld --initialize --console
mysqld --initialize-insecure
\bin>net stop mysql
\bin>sc delete mysql
\bin>mysqld --install mysql --defaults-file="C:\Program Files\MySQL\MySQL Server 8.0\my.ini"
\bin>net start mysql
mysql -u root -p
alter user 'root'@'localhost' identified with mysql_native_password by 'root';
exit;
show variables like '%log_bin%'
show binary logs;
show binlog  events in "CN-00121535-bin.000099";

https://www.cnblogs.com/yjmyzz/p/canal-standalone-tutorial.html