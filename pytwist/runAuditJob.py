#!/opt/opsware/agent/bin/python
# run script on server.py

import sys
sys.path.append("/opt/opsware/agent/pylibs")
from pytwist import *
from pytwist.com.opsware.search import Filter


# Create a TwistServer object.
ts = twistserver.TwistServer()
# Authenticate by passing an SA username and password
try:
 	ts.authenticate('admin','opsware_admin')
except:
	print "Authentication failed."
	sys.exit(2)


# Construct a search filter.
filter = Filter()
filter.expression = 'ServerVO.name *=* "%s"' % 'rhel55'
filter.objectType = 'device'

# Get a reference to ServerService.
serverservice = ts.server.ServerService
# Perform the search, returning server references.
serverRef = serverservice.findServerRefs(filter)
print "-------server %s" % serverRef

# Contruct a search filter.
filter = Filter()
filter.expression = 'AuditTaskVO.name *=* "%s"' % 'policy1'
filter.objectType = 'audit_task'

# Get a reference to AuditTaskService.
auditservice = ts.compliance.sco.AuditTaskService
# Perform the search, returning server references.
auditRef = auditservice.findAuditTaskRefs(filter)[0]
print "----------auditRef %s" % auditRef

# Run audit job
jobRef = auditservice.startAudit(auditRef,serverRef, None, None, None)
print "---------jobRef %s" % jobRef