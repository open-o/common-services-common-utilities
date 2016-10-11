--
-- Copyright 2016 ZTE Corporation.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

/******************drop old database and user***************************/
use mysql;
drop database IF  EXISTS wso_bpel;
delete from user where User='wso_bpel';
FLUSH PRIVILEGES;

/******************create new database and user***************************/
create database wso_bpel CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON wso_bpel.* TO 'wso_bpel'@'%' IDENTIFIED BY 'wso_bpel' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'wso_bpel'@'%' IDENTIFIED BY 'wso_bpel' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON wso_bpel.* TO 'wso_bpel'@'localhost' IDENTIFIED BY 'wso_bpel' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'wso_bpel'@'localhost' IDENTIFIED BY 'wso_bpel' WITH GRANT OPTION;
FLUSH PRIVILEGES; 