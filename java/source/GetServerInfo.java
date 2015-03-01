package demo;

import com.opsware.client.*;
import com.opsware.server.*;
import com.opsware.search.*;

public class GetServerInfo {

	// Default host name of the Opsware SAS core server.
	private static String host = "owc";

	// Default port of where the Opsware server is listenting for requests
	private static int port = 443;

	// Target server hostname. Substring of hostname allowed.
	private static String target;

	// A handle to the ServerService.
	private static ServerService serverSvc;
	
	// User password
	private static String USERNAME ="admin";
	private static String PASSWD = "opsware_admin";

	
	public static void main(String[] args) {
		if (!parseArgs(args)) {
			System.exit(1);
			return;
		}
		try {
			login();
			getServerInfo();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	protected static boolean parseArgs(String[] args) {
		// Parse the command-line arguments.
/*		for (int i = 0; i < args.length; i++) {
			if ("--host".equals(args[i])) {
				host = args[++i];

			} else if ("--port".equals(args[i])) {
				port = java.lang.Integer.parseInt(args[++i]);

			} else {
				target = args[i];
			}
		}*/
		target = args[0];

		if (target == null) {
			System.out
					.println("Missing target server name.  Partial names allowed.");
			usage();
			return false;
		}

		return true;
	}

	protected static void usage() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("usage: GetServerInfo [options] <target>\n");
		sbuf.append("option --host <occ host>   OCC host name.\n");
		sbuf.append("option --port <occ port>   OCC port.\n");
		sbuf.append("<target>   All or part of a managed server's hostname.\n");
		System.out.println(sbuf.toString());
	}

	protected static void login() throws Exception {
		
		// Use the OpswareClient to create an https connection to Opsware SAS
		// and authenticate the user.
		OpswareClient.connect("https", host, (short) port, USERNAME,
				PASSWD, true);

		// Get the handle to the required service.
		serverSvc = (ServerService) OpswareClient
				.getService(ServerService.class);
	}

	protected static void getServerInfo() throws Exception {

		// Construct a search filter to search for all managed
		// servers with a hostname that contains the target string
		// specified on the command line.
		String filterExpr = "ServerVO.hostname CONTAINS " + target;
		Filter filter = new Filter("device", filterExpr);
		// Get references to the servers that match the search filter.
		ServerRef[] serverRefs = serverSvc.findServerRefs(filter);
		if (serverRefs.length == 0) {
			System.out.println("No matching servers found.");
			System.out.println("Filter expression: " + filterExpr);
			System.exit(1);
		}
		// Get the value objects for the server references.
		ServerVO[] serverVOs = serverSvc.getServerVOs(serverRefs);

		// Iterate through the server value objects and print some attributes.
		for (int i = 0; i < serverVOs.length; i++) {
			System.out.println(serverVOs[i].getName());
			System.out.println("  " + serverVOs[i].getManagementIP());
			System.out.println("  " + serverVOs[i].getOsVersion());
			System.out.println();
		}
	}
}
