call mvn clean
call mvn package
::rmdir .\target\resources /s/q
call rmdir build\ /s/q
call xcopy src\main\resources\* target\resources\  /r /E /y
::拷贝到build 目录
call xcopy target\lib\* build\lib\ /r /E /y
call xcopy target\resources\* build\resources\ /r /E /y
call xcopy target\clilog-netty.jar build\ /r /y
pause

