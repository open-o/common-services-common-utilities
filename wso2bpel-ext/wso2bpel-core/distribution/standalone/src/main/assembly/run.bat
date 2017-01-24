@REM
@REM Copyright 2016 ZTE Corporation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@echo off
title wso2

set RUNHOME=%~dp0
echo ##RUNHOME %RUNHOME%


echo ### Starting wso2bps
start /D %RUNHOME%wso2bps bin\wso2server.bat
cd /D %RUNHOME%wso2bps-ext\bin

echo ### Starting wso2bps-ext
start startup.bat

cd /D %RUNHOME%

