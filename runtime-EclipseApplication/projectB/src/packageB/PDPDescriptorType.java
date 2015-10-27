package packageB;

@XmlType(name = "ClassB", propOrder = { 
		"authzService",
		"assertionIDRequestService", 
		"nameIDFormat" })
public class PDPDescriptorType {
	protected String authz_service;
	protected String assertionIDRequestService;
	protected String nameIDFormat;

	public String getAuthzService() {
		if (authz_service == null) {
			authz_service = new String();
		}
		return this.authz_service;
	}

	public String getAssertionIDRequestService() {
		if (assertionIDRequestService == null) {
			assertionIDRequestService = new String();
		}
		return this.assertionIDRequestService;
	}

	public String getNameIDFormat() {
		if (nameIDFormat == null) {
			nameIDFormat = new String();
		}
		return this.nameIDFormat;
	}

}
