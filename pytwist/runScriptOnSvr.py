#!/opt/opsware/agent/bin/python
# run script on server.py

import sys
sys.path.append("/opt/opsware/agent/pylibs")
from pytwist import *
from pytwist.com.opsware.search import Filter
from pytwist.com.opsware.script import ServerScriptJobArgs


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
filter.expression = 'ServerScriptVO.name *=* "%s"' % 'hello world'
filter.objectType = 'server_script'

# Get a reference to ScriptService.
scriptservice = ts.script.ServerScriptService
# Perform the search, returning server references.
scriptRef = scriptservice.findServerScriptRefs(filter)[0]
print "----------script %s" % scriptRef

#constuct the args
args = ServerScriptJobArgs()
args.setTargets(serverRef)

# Run script job
jobRef = scriptservice.startServerScript(scriptRef,args, None, None, None)
print "---------jobRef %s" % jobRef