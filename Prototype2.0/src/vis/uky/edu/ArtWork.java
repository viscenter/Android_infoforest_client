package vis.uky.edu;

public class ArtWork {
	
	/*Variables name, path and media of the manuscript
	 * It has an Array of media for the different media types
	 * 
	 */
	
	private String name;
	
	private String path;
	
	private int mediaSize;
	
	public Media media[];
	
	
	/*
	 * Constructor:
	 * set variables to null
	 * 
	 */
	public ArtWork(){
		
		name = null;
		
		path = null;
		
		media = null;
		
	}
	
	/*
	 * Constructor:
	 * set variables to null
	 * and initializes the array of media
	 * 
	 */
	public ArtWork(int totalMedia){
		
		name = null;
		
		path = null;
		
		media = new Media[totalMedia];
		
	}
	
	
	/*
	 *
	 * set variable path to a given value
	 * 
	 */
	
	public void setPath(String p){
		
		path = p;
		
	}
	
	
	//get the path value
	public String getPath(){
		
		return path;
		
	}
	
	
	/*
	 *
	 * set variable name to a given value
	 * 
	 */
	
	public void setName(String n){
		
		name = n;
		
	}
	
	//get the value of the name variable
	
	public String getName(){
		
		return name;
		
	}

	
	//get the mediasize value 
	public int getMediaSize(){
		
		return mediaSize;
		
	}
	
	
	/*
	 *set the media array size to a given value
	 * 
	 */
	
	public void setMediaSize(int size){
		
		mediaSize = size;
		
	}
	
	
	
	/*
	 *
	 * Initialize media array
	 * 
	 */
	
	public void initializeMedia(int size){
		
		media = new Media[size];
		
		mediaSize = size;
		
	}

}
