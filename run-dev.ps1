# Скрипт для запуска TextHub с профилем dev (H2 база данных)
Write-Host "Остановка процессов на порту 8080..." -ForegroundColor Yellow
$processes = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
if ($processes) {
    $processes | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }
    Start-Sleep -Seconds 2
    Write-Host "Процессы остановлены" -ForegroundColor Green
}

Write-Host "Запуск TextHub с профилем dev (H2 in-memory база данных)..." -ForegroundColor Green
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
