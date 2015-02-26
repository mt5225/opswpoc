#!/bin/bash
# audit name : policy1

#####################
#find audit policy
#####################
cd /opsw/api/com/opsware/compliance/sco/AuditTaskService/method
audit_id=`./.findAuditTask:i filter='{AuditTaskVO.name EQUAL_TO "policy1"}'`
echo --- audit_id = $audit_id 

##################################
# run audit
##################################
job_id=`./.startAudit:i self:i=$audit_id`
echo -- job_id = $job_id