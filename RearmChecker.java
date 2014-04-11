import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RearmChecker {

	//Enum of all known things that could be spawned
	private enum KnownEnts {
		weapon_gh_checker, weapon_jihadbomb, weapon_ttt_ak47, weapon_ttt_aug, weapon_ttt_awp,
		weapon_ttt_awperhand, weapon_ttt_beacon, weapon_ttt_binoculars, weapon_ttt_blackhole,
		weapon_ttt_c4, weapon_ttt_chickennade, weapon_ttt_confgrenade, weapon_ttt_cse, weapon_ttt_decoy,
		weapon_ttt_defuser, weapon_ttt_det_pistol, weapon_ttt_dual_elits, weapon_ttt_famas, 
		weapon_ttt_flaregun, weapon_ttt_freezegrenade, weapon_ttt_galil, weapon_ttt_glock,
		weapon_ttt_health_station, weapon_ttt_healthkit, weapon_ttt_knife, weapon_ttt_m4a1,
		weapon_ttt_m16, weapon_ttt_magnum, weapon_ttt_mp5, weapon_ttt_p90, weapon_ttt_p228,
		weapon_ttt_phammer, weapon_ttt_poison_dart, weapon_ttt_pump_shotgun, weapon_ttt_push,
		weapon_ttt_radio, weapon_ttt_sg552, weapon_ttt_shuriken, weapon_ttt_silentsniper, 
		weapon_ttt_sipistol, weapon_ttt_smg, weapon_ttt_smokegrenade, weapon_ttt_stungun,
		weapon_ttt_teleport, weapon_ttt_turtlenade, weapon_ttt_unarmed, weapon_ttt_wtester,
		weapon_ttt_tttbase, weapon_tttbasegrenade, weapon_zm_carry, weapon_zm_improvised,
		weapon_zm_mac10, weapon_zm_molotov, weapon_zm_pistol, weapon_zm_revolver, weapon_zm_rifle,
		weapon_zm_shotgun, weapon_zm_sledge
	}
	
	//Enums that people should not be spawning
	private enum Restricted {
		weapon_gh_checker, weapon_jihadbomb, weapon_ttt_ak47, weapon_ttt_awp, weapon_ttt_awperhand,
		weapon_ttt_binoculars, weapon_ttt_blackhole, weapon_ttt_c4, weapon_ttt_chickennade,
		weapon_ttt_decoy, weapon_ttt_defuser, weapon_ttt_det_pistol, weapon_ttt_flaregun,
		weapon_ttt_freezegrenade, weapon_ttt_health_station, weapon_ttt_healthkit, weapon_ttt_knife,
		weapon_ttt_m4a1, weapon_ttt_poison_dart, weapon_ttt_radio, weapon_ttt_shuriken,
		weapon_ttt_silentsniper, weapon_ttt_sipistol, weapon_ttt_stungun, weapon_ttt_teleport,
		weapon_ttt_turtlenade
	}
	
	//Count of unknown entities found
	private int unknownCount;
	
	//Number of restricted entites found
	private int restrictedCount;
	
	private HashMap<String, Integer> counts = new HashMap<String, Integer>();
	
	public RearmChecker() {

	}
	
	/**
		Open a rearm file and parse it to generate the information
		@param path: The path to the rearm file
	*/
	public void parseRearmFile(String path) {
		//Clear the old data
		clear();
		
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(new File(path)));
		}
		
		catch (FileNotFoundException e) {
			return;
		}
		
		String line = "";
		
		try {
			//Find all the entities, and add them to the counters
			while ((line = r.readLine()) != null) {
				//Get the first token, its the entity name and thats all we care about
				String entity = line.split("\\s+")[0];
				addEntity(entity);
				
			}
			
			//Send the data to whereever it goes
			sendData();
		}
		
		catch (IOException e) {
			return;
		}
	}
	
	/**
		Increment the counter for a given entity
		@param entity: The entity name taken from the ReArm file
	*/
	private void addEntity(String entity) {
	
		if (counts.containsKey(entity)) {
			int soFar = counts.get(entity);
			counts.put(entity, soFar + 1);
		}
		
		else {
			unknownCount++;
		}
		
	}
	
	/**
		Check if the entity is restricted
		@param entity, the entity to check
	*/
	private boolean isRestricted(String entity) {
		for (Restricted r: Restricted.values()) {
			if (r.name().equals(entity)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
		Send the generated data to the view
	*/
	private void sendData() {
		int finalCount = 0;
		System.out.print("\n\n\n");
		System.out.println("Results");
		System.out.println("---------------------------------------------");
		
		System.out.println("\tWeapons Found");
		for (String key: counts.keySet()) {
			if (!isRestricted(key)) {
				System.out.println("\t\t" + key + ": " + counts.get(key));
			}
			else {
				//Restricted keys are still in the counts file, I only care if someone actually
				//tried placing that item
				if (counts.get(key) > 0) {
					restrictedCount++;
				}
			}
			//Regardless of its restricted status it is still a valid entity
			finalCount++;
		}
		
		finalCount += unknownCount;
		
		System.out.println("\tStats");
		System.out.println("\t\tUnknown entites found: " + unknownCount);
		System.out.println("\t\tTotal Entity Count: " + finalCount);
		System.out.println("\t\tRestricted Entities Found: " + restrictedCount);
	}
	
	/**
		Clear all the stored data so old data wont be given by accident.
	*/
	private void clear() {
	
	
		//Clear the counts dictionary
		counts = new HashMap<String, Integer>();
		for (KnownEnts p: KnownEnts.values()) {
			counts.put(p.name(), 0);
		}
		
		//Clear the not found counter
		unknownCount = 0;
		
		//Clear the restricted count
		restrictedCount = 0;
	
	}
		
	
	public static void main(String[] args) {
		RearmChecker r = new RearmChecker();
		r.parseRearmFile("clue_ttt.txt");
	}
}