@echo off
setlocal enabledelayedexpansion

:: Navigate to project directory
cd /d "%~dp0"

echo ================================================================
echo       STARTING YUSUF MART E-COMMERCE MARKETPLACE
echo       Anna University R2025 Semester 3 Deliverable
echo ================================================================
echo.

:: Detect Maven command without space-handling issues
set "MVN_CMD="

if exist "C:\maven\bin\mvn.cmd" (
    set "MVN_CMD=C:\maven\bin\mvn.cmd"
) else (
    for %%I in ("%USERPROFILE%") do set "SHORT_USER=%%~sI"
    if exist "!SHORT_USER!\maven\apache-maven-3.9.6\bin\mvn.cmd" (
        set "MVN_CMD=!SHORT_USER!\maven\apache-maven-3.9.6\bin\mvn.cmd"
    ) else (
        where mvn >nul 2>&1
        if !ERRORLEVEL! EQU 0 (
            set "MVN_CMD=mvn"
        )
    )
)

if "%MVN_CMD%"=="" (
    echo [ERROR] Maven was not found on your system.
    echo Please verify Maven installation.
    pause
    exit /b 1
)

echo [*] Launching YusufMart Server on http://localhost:8080/yusufmart ...
echo [*] Press Ctrl+C anytime to stop the server.
echo.

call "%MVN_CMD%" compile exec:java -Dexec.mainClass="com.yusuf.yusufmart.ServerRunner"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Server encountered an error.
    pause
)
