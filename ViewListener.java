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


}