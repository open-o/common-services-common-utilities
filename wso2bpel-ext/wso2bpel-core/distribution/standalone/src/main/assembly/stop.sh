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
echo @RUNHOME@ $RUNHOME

echo "### Shutdown wso2bps";
cd wso2bps
$RUNHOME/wso2bps/bin/shutdown.sh &
cd $RUNHOME


echo "\n\n### Shutdown wso2bps-ext"
cd wso2bps-ext
$RUNHOME/wso2bps-ext/bin/shutdown.sh &
echo "### Shutdown wso2 end...";
