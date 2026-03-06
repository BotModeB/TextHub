@echo off
echo Остановка процессов на порту 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do taskkill /F /PID %%a >nul 2>&1
timeout /t 2 /nobreak >nul

echo Запуск TextHub с профилем dev (H2 in-memory база данных)...
set SPRING_PROFILES_ACTIVE=dev
call mvnw.cmd spring-boot:run
