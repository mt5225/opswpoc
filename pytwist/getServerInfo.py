#!/opt/opsware/agent/bin/python
# get_server_info.py
# Search for servers by partial hostname.

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
filter.expression = 'ServerVO.name *=* "%s"' % (sys.argv[1])
filter.objectType = 'device'

# Get a reference to ServerService.
serverservice = ts.server.ServerService
# Perform the search, returning a tuple of references.
servers = serverservice.findServerRefs(filter)

# Print some attrs
for server in servers:
	vo = serverservice.getServerVO(server)
	print "Name: " + vo.name
	print " Management IP: " + vo.managementIP
	print " OS Version: " + vo.osVersion