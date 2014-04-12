public class RearmCheckView implements ModelListener {

	private String path;
	
	private ViewListener model;

	public RearmCheckView(String path) {
		this.path = path;
	}
	
	/**
		Set the model to use
		@param model The model to use
	*/
	public void setModel(ViewListener model) {
		this.model = model;
		this.model.parseRearmFile(path);
	}
	
	/**
		Called when the model is finished parsing the ArmFile
	*/
	public void doneParsing() {
		System.out.println("\tStats");
		System.out.println("\t\tTotal Entity Count: " + model.getEntCount());
		//System.out.println("\t\tUnknown entites found: " + unknownCount);
		System.out.println("\t\tRestricted Entities Found: " + model.getRestrictedCount());
		if (model.doesReplaceSpawns()) {
			System.out.println("\t\tMap will replace spawns: " + model.getSpawnCount() + " spawns found");
		}
		
		else {
			System.out.println("\t\tMap will not replace spawns");
		}
	}
	
	/**
		Called when the model encounters an error while parsing
	*/
	public void error() {
		System.out.println("Error");
	}
	
	
		

}