package source;

import com.opsware.client.OpswareClient;
import com.opsware.compliance.sco.*;
import com.opsware.job.JobRef;
import com.opsware.search.Filter;

public class runAuditJob {

	// @TODO read info from properties file
	// Connection details to the Opsware SAS core server.
	private static String host = "owc";
	private static int port = 443;
	private static String USERNAME = "admin";
	private static String PASSWD = "opsware_admin";

	// Saved script name
	private static String auditName = "policy1";
	private static AuditTaskRef auditRef;

	// Handles to Service.
	private static AuditTaskService auditTaskSvc;

	protected static void login() throws Exception {

		// Use the OpswareClient to create an https connection to Opsware SAS
		// and authenticate the user.
		OpswareClient.connect("https", host, (short) port, USERNAME, PASSWD,
				true);

		auditTaskSvc = (AuditTaskService) OpswareClient
				.getService(AuditTaskService.class);
	}


	protected static void getAuditTaskRef() throws Exception {
		String filterExpr = "AuditTaskVO.name *=* " + auditName;
		Filter filter = new Filter("audit_task", filterExpr);
		// Get references to the servers that match the search filter.
		auditRef = auditTaskSvc.findAuditTaskRefs(filter)[0];
		System.out.println("-------audit policy " + auditRef.toString());
	}

	protected static void runAuditTask() throws Exception {
		JobRef jobRef = auditTaskSvc.startAudit(auditRef, null, null, null);
		System.out.println("-------audit job " + jobRef.toString());
	}

	public static void main(String[] args) {
		try {
			login();
			getAuditTaskRef();
			runAuditTask();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
