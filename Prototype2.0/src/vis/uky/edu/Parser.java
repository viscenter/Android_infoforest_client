package vis.uky.edu;



import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Parser {
		
	//************ Needed Variables ****************//
	
	private Bitmap bm;//bitmap for when we fetch an image
	
	private String str;//string to hold the content of the fetched file
	
	private Document doc;//doc to create the tree structor out of the xml
	
	private int page, ppage;// ints to hold the page and the previous fetched text page
	
	private int sal;//sentinel variable
	
	private char side, pside;//char to now which side of the page is fetched
	
	private String p, pp;//string with the page and previous complete label
	
	int isize, vsize;
	
	private int Ssize;
	
	//**************** Needed variables *******************//
	
	
	
	//Constructor: initializes main variables to null
	
	public Parser(){
		
		bm = null;
		
		str = null;
		
		doc = null;
		
	}
	
	//constructor
	
	
	
	//set the side of the page to a given value
	public void setSide(char s){
		
		side = s;
		
	}
	
	//get the page label
	public String getP(){
		
		return p;
		
	}
	
	
	//get the previous page label
	public String getPP(){
		
		return pp;
		
	}
	
	//set the page number to a given value
	public void setPage(int p){
		
		page = p;
		
	}
	
	
	//set the previous page to a given value
	public void setPPage(int p){
		
		ppage = p;
		
	}
	
	
	//set the Previous side to a given value
	public void setPSide(char s){
		
		pside = s;
		
	}
	
	
	//get the previous page value
	public int getPPage(){
		
		return ppage;
		
	}
	
	
	//get the previous page side value
	public char getPSide(){
	
		return pside;
		
	}
	
	//get the page value
	public int getPage(){
		
		return page;
		
	}
	
	
	//get the side of the page value
	public char getSide(){
	
		return side;
		
	}
	
	//initialize the page and previous page label
	/*
	 * it has a sentinel to know when to initialize as in the start
	 * and how to initialize as in running
	 * 
	 * sent =1 means start
	 *  
	 */
	
	
	public void initString(int sent){
		
		p = (page+""+side);
		
		if(sent == 1){
			
			pp = "";
			
		}
		
		else{
			
			pp = (ppage+""+pside);
			
		}
		
	}

	
	/*
	 * Sets the labels so they would be ready for the next
	 * fetch of the translation
	 */
	
	public String pageForward(){

		ppage = page;
		
		pside = side;
		
		if(side == 'r'){
			
			side = 'v';
			
		}
		
		else{
			
			page += 1;
			
			side = 'r';
			
		}
		
		return (page+""+side);
		
	}
	
	
	
	/*
	 * Sets the labels so they would be ready for the previous
	 * fetch of the translation
	 */
	
	public String pageBackward(){
		
		page = ppage;
		
		side = pside;
		
			if(pside == 'v'){
				
				pside = 'r';
				
			}
			
			else{
				
				ppage -= 1;
				
				ppage = 'v';
				
			}
		
		return (page+""+side);
		
	}
	
	//set the sentinel variable
	
	public void setSal(int s){
		
		sal =s;
		
	}
	//set the bitmap variable
	
	public void setBitmap(Bitmap b){
		
		bm = b;
				
	}
	
	
	//get the bitmap value
	public Bitmap getBitmap(){
		
		return bm;
		
	}
	
	
	// get the content of the last fetched file
	public void setStr(String s){
		
		str = s;
		
	}
	
	
	//get the content of the last fetched file
	public String getStr(){
		
		return str;
		
	}
	
	
	//set the Dom document based on the fetched files(tree structure)
	public void setDomObject(Document d){
		
		doc = d;
		
	}
	
	//get the document or tree
	public Document getDomObject(){
		
		return doc;
		
	}
	
	/*
	 * gets an image from the server
	 * it requires a request url
	 * stores the content on bm 
	 * (variable that holds fetched image)
	 */
    public void requestImage(String request){
    	
        try{
        	
    		HttpClient hc = new DefaultHttpClient();
    	
    		HttpGet get = new HttpGet(request);
    	
    		HttpResponse rp = hc.execute(get);
    		
    		HttpEntity entity = rp.getEntity();
    		
    		BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
    		
    		InputStream iStream = buffer.getContent();
    		
    		bm = BitmapFactory.decodeStream(iStream);

    	}catch(IOException e){System.err.print(e);}
    	
    }	
    
	/*
	 * gets a file from the server
	 * it requires a request url
	 * stores the content on str 
	 * (variable that holds the content
	 * of fetched files)
	 */
    
    public void requestDocument(String request){
    	
        try{
        	
    		HttpClient hc = new DefaultHttpClient();
    	
    		HttpGet get = new HttpGet(request);
    	
    		HttpResponse rp = hc.execute(get);
    		
    		if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
    		
    			str = EntityUtils.toString(rp.getEntity());
    	
    		}

    	}catch(IOException e){System.err.print(e);}
    	
    }
    
    /*
     * Creates the dom element or tree structure
     * based on the content of the last fetched file
     * stored on the str variable by parsing it
     * 
     */
    public void createDomElement(){
    	
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	
    	try {
    		
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
    		
            is.setCharacterStream(new StringReader(str));
            
            doc = db.parse(is); 
 
    	} catch (ParserConfigurationException e) {
        
    		Log.e("Error: ", e.getMessage());
        
    	} catch (SAXException e) {
        
    		Log.e("Error: ", e.getMessage());
                        
    	} catch (IOException e) {
        
    		Log.e("Error: ", e.getMessage());
                     
    	}
    
    }
    
    
    /*
     * gets the tree structure 
     * fills the array of manuscripts
     * with the content
     * and returns an array of manuscripts
     */
    
    public ArtWork[] FillStructures(){
    	
    	createDomElement();
    	
    	NodeList man = null;
    	
    	man = doc.getElementsByTagName("manuscript");
    	
    	ArtWork[] manuscripts = new ArtWork[man.getLength()];
    	
    	Ssize = man.getLength();
    	
    	NodeList childs = null, nchilds = null, echilds =  null;
    	
    	int count;
    	
    	for(int i=0; i<man.getLength();i++){
    		
    		childs = man.item(i).getChildNodes();
    		 		
    		manuscripts[i] = new ArtWork();
    		
    		manuscripts[i].setName(childs.item(1).getTextContent().toString());
    		
    		manuscripts[i].setPath(childs.item(3).getTextContent().toString());
    		
    		nchilds = childs.item(5).getChildNodes();

    		count = 0;
    		
    		manuscripts[i].initializeMedia(nchilds.getLength()/2);
    		
    		Media m[] = null;
    		
			for(int j=1; j<nchilds.getLength();j += 2){//cambiar O(n^2)
				
    			echilds = nchilds.item(j).getChildNodes();
    			
    			if(j == 1){
    				
    				m = new Media[nchilds.getLength()];
    				
    			}
    			
    			m[count] = new Media();
    		   			
    			manuscripts[i].media[count] = new Media();
    			
    			m[count].setType(echilds.item(1).getTextContent());
    			
    			m[count].setSpecificSource(echilds.item(3).getTextContent());
    			
    			manuscripts[i].media[count] = m[count];
    			
    			count++;

    		}
    		
    	}
    	
    	return manuscripts;
    	    	    	
    }
    
    
    //set structure size
    
    public void setStructureSize(int s){
    	
    	Ssize = s;
    	
    }
    
    //get Structure size
    public int getStructureSize(){
    	
    	return Ssize;
    	
    }
    
    
    //generates the url for the translation request//needs to be fixed
    public String getTranslationRequestUrl(){
    	
    	
    	String u = "";
    	
    	u = "http://furman-folio.appspot.com/CTS?withXSLT=chs-gp&request=GetPassagePlus&urn=urn:cts:greekLit:tlg0031.tlg001.lichfield00";
    	
    	u += "1:"+sal+"&inv=inventory.xml";
    	
    	return u;
    	
    }
    
    
    //generates and returns the request url for the inages
    public String getRequestUrl(int page, String viewer, String size){
    	
    	
    	String u = "";
    	
    	u = "http://amphoreus.hpcc.uh.edu/tomcat/chsimg/Img?&request=GetBinaryImage&urn=urn:cite:fufolioimg:ChadRGB.Chad";
    	
    	if(page-10 < 0){
    		
    		u += "00";
    		
    	}
    	
    	else if(page-100 < 0){
    		u += "0";
    	
    	}
    	
    	u += page;
    	
    	u += ":0.0,0.147,0.84,0.72";
    	
    	return u;
    	
    }
    
    
    /*
     * gets the translation of the 
     * current page by getting all the 
     * text between the previous page break
     * and the requested page, page break
     * using a tree structure
     * 
     * Note: It is buggy :(
     */
    
    public String getTranslation(int requestCount){
    	
    	String start = pp;
    	
    	String end = p;
    			
    	String trans = "";
    	
    	System.out.println("Start: "+start+" | End: "+end);
    	
    	int ok = 0;
    	
    	NodeList s = doc.getElementsByTagName("p"), d;
    	
    	NamedNodeMap n, m;
    	
    	for(int i=0;i<s.getLength();i++){  
    	
    		d = s.item(i).getChildNodes();
    		
    		for(int j=0;j<d.getLength();j++){
    			
    			if(d.item(j).hasAttributes()){
    				
    				n = d.item(j).getAttributes();
	  			
    				String att = n.item(0).getTextContent().trim();
    				
    				System.out.println(att);
    					
    				if(att.contains(end)){
    						
    					System.out.println("Entreee: "+d.item(j).getNodeName());
    					
    					return trans ;
    						
    				}
    				
    				if(att.contains(start)){
    					
    					ok = 1;
    					
    				}
    					
    			}
    			
    			if(ok == 1 || start == ""){
    				
    				trans += d.item(j).getTextContent();
    				
    				System.out.println(d.item(j).getTextContent());
    				
    			}
    				
    		}
    	
    	}
    	
    	sal++;
    	
    	requestDocument(getTranslationRequestUrl());
    	
    	createDomElement();
    	
    	NodeList v = doc.getElementsByTagName("p");
    	
    	System.out.println(v.item(1).getTextContent());
    	
    	for(int i=0;i<s.getLength();i++){  
        	
    		d = v.item(i).getChildNodes();
    		
    		for(int j=0;j<d.getLength();j++){
    			
    			if(d.item(j).hasAttributes()){
    				
    				n = d.item(j).getAttributes();
	  			
    				String att = n.item(0).getTextContent().trim();
    					
    				if(att.contains(end)){
    						
    					return trans ;
    						
    				}
    				
    				if(att.contains(start)){
    					
    					ok = 1;
    					
    				}
    					
    			}
    			
    			if(ok == 1 || start == ""){

    				trans += d.item(j).getTextContent();
    				
    			}
    				
    		}
    	
    	}
    	
    	return trans;
    		
    }   

}
