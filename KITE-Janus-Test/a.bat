@echo off
rem Launching allure server. You'll need javaAllure.exe and allure_custom.bat available in your PATH.
taskkill /IM javaAllure.exe /F
allure_custom serve kite-allure-reports

