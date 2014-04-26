import java.util.HashMap;
import java.util.Set;

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
		System.out.println("\nStats");
		System.out.println("\tTotal Entity Count: " + model.getEntCount());
		System.out.println("\tUnknown entites found: " + model.getUnknownCount());
		System.out.println("\tRestricted Entities Found: " + model.getRestrictedCount());
		if (model.doesReplaceSpawns()) {
			System.out.println("\tMap will replace spawns: " + model.getSpawnCount() + " spawns found");
		}
		
		else {
			System.out.println("\t\tMap will not replace spawns");
		}
		
		System.out.println("\n");
		System.out.println("Entity Counts\n");
		
		HashMap<String, Integer> counts = model.getCounts();
		Set<String> names = counts.keySet();
		
		for (String name:names) {
			if (!model.isRestricted(name)) {
				System.out.println("\t" + model.getName(name) + ": " + counts.get(name));
			}
		}
		
	}
	
	/**
		Called when the model encounters an error while parsing
	*/
	public void error() {
		System.out.println("Error");
	}
	
	
		

}