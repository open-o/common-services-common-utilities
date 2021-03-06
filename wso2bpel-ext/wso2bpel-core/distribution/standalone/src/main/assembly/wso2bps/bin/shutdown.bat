@REM
@REM Copyright (C) 2015 ZTE, Inc. and others. All rights reserved. (ZTE)
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM         http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM
@echo off
title stopping WSO2

set HOME=%~dp0
set Main_Class="org.wso2.carbon.bootstrap.Bootstrap"

echo ================== WSO2 info  =============================================
echo HOME=$HOME
echo Main_Class=%Main_Class%
echo ===============================================================================

echo ### Stopping WSO2
cd /d %HOME%

rem set JAVA_HOME=D:\WorkSoftware\jdk1.7.0_60
for /f "delims=" %%i in ('"%JAVA_HOME%\bin\jcmd"') do (
  call find_kill_process "%%i" %Main_Class%
)
exit
