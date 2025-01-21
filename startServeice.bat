@echo off
echo Starting Redis...
cd /d D:\Develop\redis\Redis-x64-5.0.14.1
start redis-server.exe

echo Waiting for Redis to start...
timeout /t 10 /nobreak

echo Starting Nacos in Standalone mode...
cd /d D:\Develop\nacos\nacos-server-2.2.0\nacos\bin
start startup.cmd -m standalone

echo Nacos and Redis started successfully!
pause
