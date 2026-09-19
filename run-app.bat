@echo off
setlocal enabledelayedexpansion

:: 1. Navigate to project root directory
cd /d "%~dp0"

echo ================================================================
echo       STARTING YUSUF MART E-COMMERCE MARKETPLACE
echo       Anna University R2025 Semester 3 Deliverable
echo ================================================================
echo.

:: 2. Ensure JAVA_HOME is configured
if "%JAVA_HOME%"=="" (
    if exist "C:\Users\MOHAME~1\AppData\Local\Programs\ECLIPS~1\jdk-25.0.4.101-hotspot" (
        set "JAVA_HOME=C:\Users\MOHAME~1\AppData\Local\Programs\ECLIPS~1\jdk-25.0.4.101-hotspot"
    ) else if exist "C:\Users\MOHAMED YUSUF\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.4.101-hotspot" (
        set "JAVA_HOME=C:\Users\MOHAMED YUSUF\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.4.101-hotspot"
    )
)

if not "%JAVA_HOME%"=="" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

:: 3. Detect Maven
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
    pause
    exit /b 1
)

echo [*] Starting YusufMart server...
echo [*] Opening your browser at http://localhost:8080/yusufmart ...
echo [*] KEEP THIS WINDOW OPEN while using the application!
echo.

:: 4. Automatically open Chrome / default browser after 3 seconds in background
start "" cmd /c "timeout /t 3 /nobreak >nul && start http://localhost:8080/yusufmart"

:: 5. Launch Server
call "%MVN_CMD%" exec:java -Dexec.mainClass="com.yusuf.yusufmart.ServerRunner"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Server stopped with error code %ERRORLEVEL%.
    pause
)
