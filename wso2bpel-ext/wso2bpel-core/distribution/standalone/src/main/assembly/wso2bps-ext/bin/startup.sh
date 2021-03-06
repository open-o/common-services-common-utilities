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
RUNHOME=`cd $DIRNAME/; pwd`
WSO_EXT_HOME=${RUNHOME}/..
echo @RUNHOME@ $RUNHOME

echo @JAVA_HOME@ $JAVA_HOME
JAVA="$JAVA_HOME/bin/java"
echo @JAVA@ $JAVA

JAVA_OPTS="-Xms50m -Xmx128m -Djava.awt.headless=true"
port=8312
# JAVA_OPTS="$JAVA_OPTS -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$port,server=y,suspend=n"
JAVA_OPTS="$JAVA_OPTS -DWSO2_EXT_HOME=${WSO_EXT_HOME}"
echo @JAVA_OPTS@ $JAVA_OPTS

class_path="$RUNHOME/:$RUNHOME/../lib/*:$RUNHOME/../wso2bpel-service.jar"
echo @class_path@ $class_path

"$JAVA" $JAVA_OPTS -classpath "$class_path" org.openo.carbon.bpel.Wso2BpelApplication server "$RUNHOME/../conf/wso2bpel.yml"

