/**
	RearmChecker.java
	@author Colin Murphy

*/

//Required imports
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RearmCheckModel implements ViewListener {

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
		weapon_tttbase, weapon_tttbasegrenade, weapon_zm_carry, weapon_zm_improvised,
		weapon_zm_mac10, weapon_zm_molotov, weapon_zm_pistol, weapon_zm_revolver, weapon_zm_rifle,
		weapon_zm_shotgun, weapon_zm_sledge, ttt_random_weapon, item_ammo_revolver_ttt, item_ammo_smg1_ttt,
		ttt_playerspawn, item_box_buckshot_ttt, ttt_random_ammo, item_ammo_pistol_ttt, item_ammo_357_ttt,
		
	}
	
	//Enums that people should not be spawning
	private enum Restricted {
		weapon_gh_checker, weapon_jihadbomb, weapon_ttt_ak47, weapon_ttt_awp, weapon_ttt_awperhand,
		weapon_ttt_binoculars, weapon_ttt_blackhole, weapon_ttt_c4, weapon_ttt_chickennade,
		weapon_ttt_decoy, weapon_ttt_defuser, weapon_ttt_det_pistol, weapon_ttt_flaregun,
		weapon_ttt_freezegrenade, weapon_ttt_health_station, weapon_ttt_healthkit, weapon_ttt_knife,
		weapon_ttt_m4a1, weapon_ttt_poison_dart, weapon_ttt_radio, weapon_ttt_shuriken,
		weapon_ttt_silentsniper, weapon_ttt_sipistol, weapon_ttt_stungun, weapon_ttt_teleport,
		weapon_ttt_turtlenade, weapon_ttt_beacon, weapon_ttt_basegrenade, weapon_ttt_wtester,
		weapon_zm_improvised, weapon_tttbase, weapon_ttt_cse, weapon_ttt_unarmed, weapon_ttt_phammer,
		weapon_tttbasegrenade, weapon_zm_carry, weapon_ttt_push
	}
	
	//Count of unknown entities found
	private int unknownCount;
	
	//Number of restricted entites found
	private int restrictedCount;
	
	//Number of valid entities placed
	private int validEntCount = 0;
	
	private HashMap<String, Integer> counts = new HashMap<String, Integer>();
	
	//Are spawns replaced?
	private boolean replaceSpawns;
	
	//The view to talk to
	private ModelListener view;
	
	public RearmCheckModel() {
		//Poor little guy has nothing to do :c
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
				
				//Handle replace spawns setting
				if (entity.equals("setting:") &&  line.split("\\s+")[1].equals("replacespawns")) {
					String s = line.split("\\s+")[2];
					if (s.equals("1")) {
						replaceSpawns = true;
					}
					
					else {
						replaceSpawns = false;
					}
				}
				//Ignore comments
				else if (!entity.equals("#")) {
					addEntity(entity);
				}
				
			}
			
			//Store the data as appropriate
			
			for (String key: counts.keySet()) {
				//Handle restricted entites
				if (isRestricted(key) && counts.get(key) > 0) {
					restrictedCount+= counts.get(key);
				}
				
				//Handle legal entites - not sure why I do this second, but it doesnt matter
				else {
					//Regardless of its restricted status it is still a valid entity
					validEntCount += counts.get(key);
				}
			}
			
			view.doneParsing();
		}
		
		catch (IOException e) {
			view.error();
			return;
		}
	}
	
	/**
		Get the total entity count
		@return The entity count
	*/
	public int getEntCount() {
		return validEntCount;
	}
	
	
	/**
		Get the count of restricted entities
		@return The count of restricted entites
	*/
	public int getRestrictedCount() {
		return restrictedCount;
	}
	
	/**
		Check if the script replaces the spawns
		@return true or false if spawns are replaced
	*/
	public boolean doesReplaceSpawns() {
		return replaceSpawns;
	}
	
	/**
		Get the count of the placed spawns
		@return PlayerSpawn count
	*/
	public int getSpawnCount() {
		return counts.get("ttt_playerspawn");
	}
	
	/**
		Set the view
		@param view The view to use
	*/
	public void setView(ModelListener view) {
		this.view = view;
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
		
		//Clear the valid count
		validEntCount = 0;
		
	
	}
		
	
	public static void main(String[] args) {
		RearmCheckModel r = new RearmCheckModel();
		r.parseRearmFile(args[0]);
	}
}