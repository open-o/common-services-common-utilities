#!/bin/bash
#
# Copyright 2016 ZTE Corporation.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

DIRNAME=`dirname $0`
HOME=`cd $DIRNAME/; pwd`
user=$1
password=$2
port=$3
host=$4


echo "start to change db ip and port"
sed -i "s|localhost|$host|" wso2bps/repository/conf/datasources/bps-datasources.xml
sed -i "s|3306|$port|" wso2bps/repository/conf/datasources/bps-datasources.xml
cat wso2bps/repository/conf/datasources/bps-datasources.xml
echo "end of change db ip and port"
echo ""

echo "start create wso2 bpel db"
mysql -u$user -p$password -P$port -h$host <$HOME/wso2bps/dbscripts/bps/bpel/create/wso-bpel-mysql-createdb.sql
sql_result=$?
if [ $sql_result != 0 ] ; then
    echo "failed to create wso2 bpel database"
    exit 1
fi


echo "start create wso2 bpel table"
mysql -u$user -p$password -P$port -h$host -Dwso_bpel <$HOME/wso2bps/dbscripts/bps/bpel/create/mysql.sql
sql_result=$?
if [ $sql_result != 0 ] ; then
  echo "failed to create wso2 bpel table"
  exit 1
fi

echo "init wso2 bpel database success!"
exit 0


