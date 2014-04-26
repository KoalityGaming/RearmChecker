import java.util.HashMap;

public interface ViewListener {
	
	/**
		Parse a ReArm file
		@param path The path to the rearm file
	*/
	public void parseRearmFile(String path);
	
	/**
		Get the total entity count
		@return The entity count
	*/
	public int getEntCount();
	
	
	/**
		Get the count of restricted entities
		@return The count of restricted entites
	*/
	public int getRestrictedCount();
	
	/**
		Check if the script replaces the spawns
		@return true or false if spawns are replaced
	*/
	public boolean doesReplaceSpawns();
	
	/**
		Get the count of the placed spawns
		@return PlayerSpawn count
	*/
	public int getSpawnCount();
	
	/**
		Set the view
		@param view The view to use
	*/
	public void setView(ModelListener view);
	
	/**
		Get the counts of all entities
		@return A HashMap with the entity name as the key and its count as the value
	*/
	public HashMap<String, Integer> getCounts();
	
	/**
		Get the count of unknown entities
		@return The count of unknown entities
	*/
	public int getUnknownCount();
	
	/**
		Check if the entity is restricted
		@param entity, the entity to check
	*/
	public boolean isRestricted(String entity);
	
	/**
		Get the name of the entity
		@param name The entity name (example: weapon_ttt_sg552)
		@returns The name that people are used to for the entity (example: SG552)
	*/
	public String getName(String name);


}