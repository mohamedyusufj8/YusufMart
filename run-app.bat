@echo off
setlocal

echo ================================================================
echo       STARTING YUSUF MART E-COMMERCE MARKETPLACE
echo       Anna University R2025 Semester 3 Deliverable
echo ================================================================

:: Check if maven is available in PATH or local folder
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Users\MOHAMED YUSUF\maven\apache-maven-3.9.6\bin\mvn.cmd" (
        set "MVN_CMD=C:\Users\MOHAMED YUSUF\maven\apache-maven-3.9.6\bin\mvn.cmd"
    ) else (
        echo [ERROR] Maven is not installed or not found.
        echo Please ensure Maven is installed or run with JDK.
        pause
        exit /b 1
    )
) else (
    set "MVN_CMD=mvn"
)

echo [1/2] Compiling YusufMart Java Sources...
call "%MVN_CMD%" compile
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo [2/2] Launching Embedded Tomcat Server on http://localhost:8080/yusufmart ...
echo.
call "%MVN_CMD%" exec:java -Dexec.mainClass="com.yusuf.yusufmart.ServerRunner"

pause
