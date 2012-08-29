package vis.uky.edu;

import android.graphics.Bitmap;

public class ImageCache {
	
	/*
	 * cacheIndex: to now the current page in the cache array
	 * cache[]: an array of bitmaps to store the previous, current and next image
	 * parser: to get the images from the server
	 * next and prev: index of the next and previous images in the cache array
	 */
	
	//***************Needed Variables *****************//
	
	private int cacheIndex;
	
	private Bitmap cache[];
	
	private Parser parser;
	
	private int next, prev;
	
	//**************Needed Variables***************//
	
	
	//*****Constructor*****//
	/*
	 * cacheIndex= 1(the one in the middle, current)
	 * initializing the cache to be an array of three images
	 * initializing the parser
	 */
	public ImageCache(){
		
		cacheIndex = 1;
		
		cache = new Bitmap[3];
		
		parser = new Parser();
		
	}
	
	//******** Constructor ***********//
	
	
	//Setting the cache for the first time//
	
	/*
	 * Loads the current and the next image into the cahce array
	 * 
	 */
	
	public void setCache(int pagen){
			
		parser.requestImage(parser.getRequestUrl(pagen, "RGB", "small"));
		
		cache[cacheIndex] = parser.getBitmap();
		
		next = cacheIndex+1;
		
		prev = cacheIndex-1;
		
		parser.requestImage(parser.getRequestUrl(pagen+1, "RGB", "small"));
		
		cache[next] = parser.getBitmap();
		
		if(pagen != 1){
			
			parser.requestImage(parser.getRequestUrl(pagen-1, "RGB", "small"));
			
			cache[prev] = parser.getBitmap();
			
		}
		
	}
	
	//gets current image from the cache array
	
	public Bitmap getCurrent(){
		
		return cache[cacheIndex];
		
	}
	
	
	//get the next image from the cache array
	public Bitmap getNext(){
		
		return cache[next];
		
	}
	
	
	//get the previous image from the cache array
	public Bitmap getPrev(){
		
		return cache[prev];
		
	}
	
	
	/*
	 * moves the current to be the previous
	 * moves the next to be the current and
	 * fetches the next one and stores it
	 * on the next index of the cahce array
	 */
	public void cacheForward(int pagen){
		
		cache[0] = cache[1];
		
		cache[1] = cache[2];
		
		parser.requestImage(parser.getRequestUrl(pagen+1, "RGB", "small"));
		
		cache[2] = parser.getBitmap();
				
	}
	
	
	/*
	 * moves the current to be the next
	 * moves the previous to be the current and
	 * fetches the previous one and stores it
	 * on the previous index of the cahce array
	 */
	public void cacheRewind(int pagen){
		
		cache[2] = cache[1];
		
		cache[1] = cache[0];
		
		parser.requestImage(parser.getRequestUrl(pagen-1, "RGB", "small"));
		
		cache[0] = parser.getBitmap();
				
	}
	
}