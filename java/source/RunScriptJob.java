package source;

import com.opsware.client.*;
import com.opsware.job.JobRef;
import com.opsware.script.*;
import com.opsware.server.*;
import com.opsware.search.*;

public class RunScriptJob {
	// Connection details to the Opsware SAS core server.
	private static String host = "owc";
	private static int port = 443;
	private static String USERNAME ="admin";
	private static String PASSWD = "opsware_admin";

	// Target server hostname.
	private static String target = "rhel55";
	private static ServerRef[] serverRefs;
	
	// Saved script name
	private static String scriptname = "hello world";
	private static ServerScriptRef scriptRef;

	// Handles to Service.
	private static ServerService serverSvc;
	private static ServerScriptService serverScriptSvc;
	
	
	
	protected static void login() throws Exception {
		
		// Use the OpswareClient to create an https connection to Opsware SAS
		// and authenticate the user.
		OpswareClient.connect("https", host, (short) port, USERNAME,
				PASSWD, true);

		// Get the handle to the required service.
		serverSvc = (ServerService) OpswareClient
				.getService(ServerService.class);
		
		serverScriptSvc = (ServerScriptService) OpswareClient
				.getService(ServerScriptService.class);
	}
	
	protected static void getServerRef() throws Exception {

    	String filterExpr = "ServerVO.hostname *=* " + target;
		Filter filter = new Filter("device", filterExpr);
		// Get references to the servers that match the search filter.
		serverRefs = serverSvc.findServerRefs(filter);
		if (serverRefs.length == 0) {
			System.out.println("No matching servers found.");
			System.out.println("Filter expression: " + filterExpr);
			System.exit(1);
		}
		System.out.println("-------server " + serverRefs[0].toString());
	}
	
	protected static void getScriptRef() throws Exception {

    	String filterExpr = "ServerScriptVO.name *=* " + scriptname;
		Filter filter = new Filter("server_script", filterExpr);
		// Get references to the script that match the search filter.
		ServerScriptRef[] scriptRefs = serverScriptSvc.findServerScriptRefs(filter);
		if (scriptRefs.length == 0) {
			System.out.println("No matching script found.");
			System.out.println("Filter expression: " + filterExpr);
			System.exit(1);
		}
		// Get the value objects for the references.
		scriptRef = scriptRefs[0];
		System.out.println("-------script " + scriptRef.toString());
	}
	
	protected static void runScriptJob() throws Exception {
		ServerScriptJobArgs args = new ServerScriptJobArgs();		
		args.setTargets(serverRefs);
		JobRef jobRef = serverScriptSvc.startServerScript(scriptRef, args,  null, null, null);
		System.out.println("-------job " + jobRef.toString());
	}
	
	public static void main(String[] args) {
		
		try {
			login();
			getServerRef();
			getScriptRef();
			runScriptJob();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
