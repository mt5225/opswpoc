#!/bin/bash
# script name : hello world
# server name : rhel55

#####################
#find server handle
#####################
cd /opsw/api/com/opsware/server/ServerService/method
server_id=`./.findServerRefs:i filter='{ServerVO.name EQUAL_TO "rhel55"}'`
echo --- server_id = $server_id

#####################
#find script handle
#####################
cd /opsw/api/com/opsware/script/ServerScriptService/method
script_id=`./.findScript:i filter='{ServerScriptVO.name BEGINS_WITH "hello world"}'`
echo --- script_id = $script_id

##################################
# run the job with given server
##################################
cd /opsw/api/com/opsware/script/ServerScriptService/method
job_id=`./.startServerScript:i self:i=$script_id args={targets:i=$server_id}`
echo -- job_id = $job_id