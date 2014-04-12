public interface ModelListener {

	/**
		FCalled when the model is finished parsing the ArmFile
	*/
	public void doneParsing();
	
	/**
		Called when the model encounters an error while parsing
	*/
	public void error();
	
	/**
		Set the model to use
		@param model The model to use
	*/
	public void setModel(ViewListener model);	


}