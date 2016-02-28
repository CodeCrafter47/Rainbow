@echo off

REM Example Backup Routine
REM ----------------------------------------------
REM This batch file backs up the folder it is in.
REM ...
REM This assumes you've downloaded the following program
REM http://downloads.sourceforge.net/sevenzip/7za920.zip
REM And placed it at c:\bin\7zip\7za.exe
REM ...
REM Customize however you want...
REM ----------------------------------------------


REM Get date/time for unique filename
REM ----------------------------------------------
FOR /f %%a IN ('WMIC OS GET LocalDateTime ^| FIND "."') DO SET DTS=%%a
SET DateTime=%DTS:~0,8%-%DTS:~8,6%
REM ----------------------------------------------

echo ==================================================
echo.
echo About to create backup. Pausing 5 seconds...
echo --------------------------------------------
ping -n 5 127.0.0.1 > nul

echo Creating backup...
echo --------------------------------------------
c:\bin\7zip\7za.exe a -r ..\backup_%DateTime%.7z *


REM Notify console it is complete
echo --------------------------------------------
echo Backup Complete!  Pausing 5 more seconds...
ping -n 5 127.0.0.1 > nul


echo --------------------------------------------
echo All done.  Create a loop here to restart your server
echo.


