@echo off
:: Kiểm tra nếu không có quyền admin thì chạy lại với quyền admin
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo Yêu cầu quyền admin...
    powershell start-process '%0' -verb runas
    exit /B
)

echo Chỉnh sửa file hosts...
set HOSTS_FILE=%SystemRoot%\System32\drivers\etc\hosts
set NEW_ENTRY=127.0.0.1   pet-Haven.com

findstr /C:"%NEW_ENTRY%" %HOSTS_FILE%
if not %errorlevel%==0 (
    echo %NEW_ENTRY%>>%HOSTS_FILE%
    echo them thanh cong
) else (
    echo dong da ton tai trong file host
)

pause
