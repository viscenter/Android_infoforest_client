package vis.uky.edu;

public class Media {

	/*
	 *variables: 
	 * type : holds the media type
	 * sources : holds the source of the media
	 * 
	 */
	
	private String type;
	
	private String sources;
	
	
	/*Constructor
	 * set type and sources to null
	 * 
	 */
	public Media(){
		
		type = null;
		
		sources = null;
		
	}
	
	//get the type value
	
	public String getType(){
		
		if(type != null){
				
			return type;
		
		}
		
		return "Is Null";
			
	}
	
	
	//set the value of type to given value
	
	public void setType(String t){
		
		type = t;
		
	}
	
	//get a specified source
	
	public String getSpecificSource(){
			
		if(sources != null){
		
			return sources;
			
		}
		
		return "Is Null";
	}
	
	//set specified source
	
	public void setSpecificSource(String s){
		
		sources = s;
		
	}
	
	//inititalizes the variables to null
	
	public void initialize(){
		
		sources = null;
		
		type = null;
		
	}
	
}
