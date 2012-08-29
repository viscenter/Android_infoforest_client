package vis.uky.edu;

import java.util.concurrent.locks.Lock;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

public class ManuscriptView extends Activity implements OnClickListener, OnTouchListener, OnSeekBarChangeListener{

	private ArtWork[] manuscripts;//define an array of manuscripts
	
	private int page, identifier, count, requestCount = 1, senti = 0;//int variables to hold the page number, the clicked manuscript which is identifier requestCount which is the page of urn requested
	//senti which is a sentinal variable
	
	private Bitmap b;//defining a Bitmap variable to hold fetched images
	
	private TextView pageNumber;//defining a textview which will display the pagenumber
	
	private LinearLayout upper, lower, main;//defining the upper menu, the main layout that will hold the content and the lower menu
	
	private Parser par;//defining the parser
	
	private float x1, x2, dx;//defining the first and second x value and the dx(change in x) so we can use with the swipe gesture
	
	private LayoutParams params;//defining a new LayoutParams object that lets you modify the properties of an UI Element
	
	private TableLayout table;//defining a table layout used to organize the content
	
	private TextView translation;//defining a textView that will display the text corresponding to the image fetched
	
	private ImageView pages;//defining a ImageView that will display the fetched image 
	
	private ImageCache ic;//defining a new image cache object
	
	private int vis = 0, width, height;//defining vis which is a sentinel variable and width and height that are going to store the size of the screen
	
	private String config;//defining a string variable that will hold the content of the configuration file
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());//allowing the main thread to do http request
        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());//allowing the main thread to do http request
		
		super.onCreate(savedInstanceState);//creates the view
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);//requests for no title window
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//sets the fullscreen
		
		setContentView(R.layout.manuscript);//sets the content of the View to be the layout on the manuscript xml file
		
		identifier = getIntent().getIntExtra("identifier", -1);//getting the clicked button id from the previous activity
		
		config = getIntent().getStringExtra("configfile");//getting the content of the configuration file from the previous activity
		
		ic = new ImageCache();//initializing the ImageCache object
		
		table = (TableLayout)findViewById(R.id.manuslay);//getting the Table Layout element with id manuslay
		
        Display display = getWindowManager().getDefaultDisplay();//getting the display as an object
        
        Point size = new Point();//creating a Point object
        
        display.getSize(size);//getting the size of the screen as a Point object and storing it on the Point object named size
        
        width = size.x;//setting a variable for the width of the screen
        
        height = size.y;//setting a variable for the height of the screen
        
        page = 1;//initialize the page to be the first
        
        par = new Parser();//initialize the parser object
        		
        ic.setCache(page);//set the cache object to be ready to start(Load next image)
        
        b = ic.getCurrent();//get the current image in the cache(first page)
                
        par.setStr(config);//set the config content to the string used to parse in the parser object
        
        manuscripts = par.FillStructures();//fill the manuscripts array with the configuration file content
        
        
        //************* Upper Menu *************//
        
        
		upper = (LinearLayout)findViewById(R.id.upper);//getting the upper menu from the xml layout
        
		params = new LayoutParams();//initializing the parameters object
		
        params.height = height/12;//setting the height of the upper menu to the height of the screen devided by 12
        
        params.width = LayoutParams.MATCH_PARENT;//setting the width of the upper menu to be the width of the screen
        
        upper.setLayoutParams(params);//set these parameters to be the parameters of the upper menu
        
        upper.setBackgroundResource(R.raw.navigation);//set the background of the upper menu to be the graphic designed for the upper menu
        
        Button tmp;//defining a temporary Button variable
        
        String menu[] = {"About", "Credits", "FoLIO"};//string array holding the text for the initial upper menu
        
		params = new LayoutParams();//initializing the parameters object
        
        for(int i=0; i<menu.length; i++){//going over all the elements on the 
        	
        	params.width = (width/9)+10;//set the width of the button to be the width of the screen devided by 9 + 10 
        	
        	params.height = LayoutParams.MATCH_PARENT;//set the height to be the same as the parent
        	
        	tmp = new Button(this);//initializing the temporary button object 
        	
        	tmp.setBackgroundResource(R.raw.button);//setting the background of the button to be the graphic designed for the buttons
        	
        	tmp.setTextColor(Color.BLUE);//set the color text to be blue
        	
        	tmp.setText(menu[i]);//set the text to be the corresponding text of the button stored in the array
        	
        	tmp.setOnClickListener(this);//set the on click listener(is listening or waiting to be clicked)
        	
        	tmp.setOnTouchListener(this);//set the on touch listener(is listening or waiting to be touched)
        	
        	tmp.setId((i+100));//set the id of the button the current index + 100
        	
        	tmp.setLayoutParams(params);//set the layout params to be the layout params 
        	
        	upper.addView(tmp);//add the button to the upper menu
        	
        }
        
        
        //*************** Upper Menu ****************//
        
        
        //**************** Main **************//
        
        
        main = (LinearLayout)findViewById(R.id.main);//getting the main layout from the xml layout
        
		params = new LayoutParams();//initialize the parameters object
        
        params.height = height-(height/6);//setting the height of the main layout to be the height of the screen minus the height of the screen divided by 6
        
        params.width = LayoutParams.MATCH_PARENT;//setting the width of the main layout to be the same of the same as the parent
        
        main.setLayoutParams(params);//set the parameters of the main layout to be the parameters already set
        
        translation = (TextView)findViewById(R.id.translation);//getting the translation view from the xml layout
        
		params = new LayoutParams();//initialize the params object
        
        params.height = LayoutParams.MATCH_PARENT;//set the height of the translation view to match 
        
        params.width = width/2;//set the width of the translation view to be half of the screen widths
  
        translation.setLineSpacing(5, 1);//set the line spacing for the translation view to be 5 px
            
        translation.setLayoutParams(params);//set the parameters of the translation view
        
        translation.setBackgroundColor(Color.WHITE);//set the background of the translation view to be white
        
        translation.setText(R.string.about);//set the text to be a string stored on the string xml
        
        translation.setTextColor(Color.BLACK);//set text color to black
        
        pages = (ImageView)findViewById(R.id.pages);//get the image view from the layout xml
        
		params = new LayoutParams();//initialize the params object
        
        params.height = LayoutParams.MATCH_PARENT;//setting the height of the image view to be the same as the parent
        
        params.width = width/2;//set the width to be half of the screen width
        
        pages.setLayoutParams(params);// setting the parameters of the image view 
        
        pages.setImageResource(R.raw.chadd1);//set the image on the image view to be the cover book image
        
        //************** Main ******************//
        
        
        //************* Lower Menu *************//
        
        
		lower = (LinearLayout)findViewById(R.id.lower);//getting the lower menu from the xml layout
        
		params = new LayoutParams();//initialize the parameters object
		
        params.height = height/12;//setting the height of the lower menu to be the height of the screen devided by 12
        
        params.width = width;// set the width of the lower menu to be the width of the screen
        
        lower.setLayoutParams(params);//set the parameters of the lower menu
        
        Button home = (Button)findViewById(R.id.home);//get the button from the home button from the xml layout
        
        home.setBackgroundResource(R.raw.home_button);//set the background of the home button to be the graphic designed for the home button
        
        home.setOnClickListener(this);//set the on click listener(its listening or waiting to be clicked
        
        home.setId(7);//set the id of the home button to be 7
        
		params = new LayoutParams();//initialize the params object
        
        params.gravity = Gravity.CENTER_VERTICAL;// setting the home button on the middle of the lower menu (Vertically)
        
        params.width = LayoutParams.WRAP_CONTENT;//set the width of the home button to be the same as the image
        
        params.height = (height/12) - 10;//set the height of the button to be the height of the screen divided by 12
        
        home.setLayoutParams(params);//setting the parameters of the home button
        
        home.setPadding(40,0,40,0);//setting the padding of the home button
        
        Button toc = (Button)findViewById(R.id.toc);//getting the table of contents button from the xml layout
        
        toc.setBackgroundResource(R.raw.table_of_contents);//setting the background of the toc to be the graphic designed for the toc button
        
        toc.setId(8);//set the id to be 8
        
		params = new LayoutParams();//initialize the parameters object
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = (height/12)-10;
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        toc.setLayoutParams(params);
        
        toc.setPadding(40,0,40,0);
        
        toc.setOnClickListener(this);
        
        toc.setId(9);
        
        SeekBar seekbar = (SeekBar)findViewById(R.id.seekbar);
        
        seekbar.setKeyProgressIncrement(1);
        
        seekbar.setMax(200);
        
        seekbar.setOnSeekBarChangeListener(this);
        
		params = new LayoutParams();
        
        params.height = LayoutParams.MATCH_PARENT;
        
        params.width = width-450;
        
        params.height = (height/12)-10;
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        seekbar.setLayoutParams(params);
        
        seekbar.setPadding(40,0,20,0);
        
        pageNumber = (TextView)findViewById(R.id.pageNum);
        
        pageNumber.setText("Book Cover");
        
		params = new LayoutParams();
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = 50;
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        pageNumber.setPadding(0, 0, 20, 0);
        
        pageNumber.setLayoutParams(params);
        
        Button info = (Button)findViewById(R.id.info);
        
        info.setBackgroundResource(R.raw.info);
        
        info.setId(8);
        
        info.setOnClickListener(this);
        
		params = new LayoutParams();
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = (height/12)-10;
        
        params.gravity = Gravity.CENTER_VERTICAL & Gravity.RIGHT;
        
        params.rightMargin = -50;
        
        info.setLayoutParams(params);
        
        lower.setBackgroundResource(R.raw.navigation);
        
        
        //**************** Lower Menu ************//
		        
	}

	//**************Handling the click event *****************//
	
	/*Intent is an object that lets you start another activity
	 * put extra sends the data to the other activity
	 * startActivity starts the activity
	 * depending on the id of the button clicked an activity is started
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * the last three are the buttons for the initial upper menu
	 * they change the text on the cover book view
	 * 
	 */
	
	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		
		case 0:
			Intent i = new Intent(this, HiResViewer.class);
			i.putExtra("identifier", identifier);
			i.putExtra("page", page);
			i.putExtra("config", config);
			startActivity(i);
			break;
			
		case 1:
			Intent j = new Intent(this, MultiSpecViewer.class);
			j.putExtra("identifier", identifier);
			j.putExtra("page", page);
			j.putExtra("config", config);
			startActivity(j);
			break;
			
		case 2:
			Intent k = new Intent(this, ThreedDViewer.class);
			k.putExtra("identifier", identifier);
			k.putExtra("page", page);
			k.putExtra("config", config);
			startActivity(k);
			break;
			
		case 3:
			Intent l = new Intent(this, LegacyViewer.class);
			l.putExtra("identifier", identifier);
			l.putExtra("page", page);
			l.putExtra("config", config);
			startActivity(l);
			break;
			
		case 4:
			Intent m = new Intent(this, TranslationViewer.class);
			m.putExtra("identifier", identifier);
			m.putExtra("page", page);
			m.putExtra("config", config);
			startActivity(m);
			break;
			
		case 5:
			Intent n = new Intent(this, ExtrasViewer.class);
			n.putExtra("identifier", identifier);
			n.putExtra("page", page);
			n.putExtra("config", config);
			startActivity(n);			
			break; 
			
		case 7:
			
			Intent o = new Intent(this, Prototype2.class);
			startActivity(o);
			break;
			
		case 8:
			Intent r = new Intent(this, Info.class);
			startActivity(r);
			break;
			
		case 9:
			Intent y = new Intent(this, ManuscriptView.class);
			y.putExtra("identifier", identifier);//send the Id of the button to the Manuscript View so it knows what manuscript got pressed
			y.putExtra("configfile", config);//send the configuration file so it doesnt have to fetch it from the server again
			startActivity(y);
			break;
			
		case 100:
			translation.setText(R.string.about);
			break;
		
		case 101:
			translation.setText(R.string.credits);
			break;
			
		case 102:
			translation.setText(R.string.folio);			
			break;
			
		default:
			
			break;
		
		}
		
	}
	
	//********** Handling the on Click event*****************//
	

	
	//****************** Handling the on Touch event ************//
	
	/*Action Down = finger on screen
	 * 
	 * Action up = taking finger of screen
	 * 
	 * Basically this part of the code gets the x value when the user puts a finger on the screen
	 * and when it takes it off, calculates the change in x and gets the next or the prevoius page
	 * depending on which gesture was made
	 * 
	 * Also it evaluates if the user touched once and quickly the screen and if the user did
	 * it will hide the menu bars
	 * 
	 * 
	 * 	 * \
	 * (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	
	
	@Override
	public boolean onTouchEvent(MotionEvent arg1) {
		
		switch(arg1.getAction()){//evaluate the event

		case(MotionEvent.ACTION_DOWN):
			
			x1 = arg1.getRawX();
		
			break;
		
		case(MotionEvent.ACTION_UP):{
			
			x2 = arg1.getRawX();
			
			dx = x2 - x1;
			
			if(x1 == x2){
				
				if(vis == 0){
				
					upper.setVisibility(View.GONE);
					
					lower.setVisibility(View.GONE);
					
					params = new LayoutParams();
					
					params.height = height;
					
					main.setLayoutParams(params);
				
					vis = 1;
					
				}
				
				else{
					
					upper.setVisibility(View.VISIBLE);
					
					lower.setVisibility(View.VISIBLE);
					
					LayoutParams lp = new LayoutParams();
					
					lp.height = height-(height/6);
					
					main.setLayoutParams(lp);
					
					vis = 0;
					
				}
				
				
			}
							
			if(dx<0){
								
				if(senti == 1){
				
					page += 1;
				
					b = ic.getNext();
					
					requestCount += 1;
						
					par.requestDocument(par.getTranslationRequestUrl());
			               
			        par.pageForward();
			        
			        par.initString(0);
			        
			        translation.setText(par.getTranslation(requestCount));
					
				}
				
				else{
					
					setMenu();
					
					par.setSal(1);
					
			        par.requestDocument(par.getTranslationRequestUrl());
			        
			        par.createDomElement();
			        
			        par.setPage(1);
			        
			        par.setSide('r');
			        
			        par.initString(1);
			        
			        translation.setText(par.getTranslation(requestCount));
					
					b = ic.getCurrent();
					
				}
				
				senti = 1;
				
				pages.setImageBitmap(b);
				
				pages.setId(page);
				
				pageNumber.setText("Page: " + Integer.toString(page));					
						
				ic.cacheForward(page);
					
			}
			
			if(dx>0 && page != 1){
				
				page -= 1;
				
				b = ic.getPrev();
				
				pages.setImageBitmap(b);
						
				pages.setId(page);
				
				pageNumber.setText("Page: " + Integer.toString(page));
						
				ic.cacheRewind(page);
				
				if(count == 5){
					
					requestCount -= 1;
				
					par.requestDocument("http://lagrange.ccom.uprrp.edu/~esantos/Matthew-Latin-Latin.xml");
		        
					count = 0;
					
				}
					
		        par.pageBackward();
		        
		        if((page + 1) == 2){
		        	
		        	par.initString(1);
		        	
		        }
		        
		        else{
		        
		        	par.initString(0);
		        
		        }
		        	
		        translation.setText(par.getTranslation(requestCount));
		        
		        count--;
							
			}
		
		}
		
		}
		return super.onTouchEvent(arg1);
	}

	
	//***************Handling the onTouch Event ***********************//
	
	
	//************** getting the pressed effect on the upper menu buttons *****************//
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			
			v.setBackgroundColor(Color.LTGRAY);
			
			break;
			
		case MotionEvent.ACTION_UP:
			
			v.setBackgroundResource(R.raw.button);
			
			break;
		
		
		}
		
		return super.onTouchEvent(event);
	}
	
	//************** getting the pressed effect on the upper menu buttons *****************//

	
	
	//*********************** Handling the slider ******************//
	
	
	/*This code gets the value of the slider and once it is stable it loads the requested image
	 * Note: this doesnt incorporate the image caching
	 * 
	 * 
	 * (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
	 */
	
	
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		
		page = arg1+1;
		
		pageNumber.setText("Page: " + Integer.toString(page));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	
		par.requestImage(par.getRequestUrl(page, "RGB", "small"));
		
		b = par.getBitmap();
	
		pages.setImageBitmap(b);
				
		pages.setId(page);
		
		
	}
	
	//**************** handling the slider ************************//
	
	
	//********************* setting the menu of the viewers **************//
	
	/*This code removes the old buttons and 
	 * adds the new buttons to the upper menu
	 * adds the search bar to the upper menu 
	 * 
	 */
	
	public void setMenu(){
		
		upper.removeAllViews();
		
        Button tmp;
        
        String menu[] = {"Hi-Res", "Multispectral", "3D", "Legacy", "Translation", "Extras"};
        
		params = new LayoutParams();
        
        for(int i=0; i<menu.length; i++){
        	
        	params.width = (width/9)+10;
        	
        	params.height = LayoutParams.MATCH_PARENT;
        	
        	tmp = new Button(this);
        	
        	tmp.setBackgroundResource(R.raw.button);
        	
        	tmp.setTextColor(Color.BLUE);
        	
        	tmp.setText(menu[i]);
        	
        	tmp.setOnClickListener(this);
        	
        	tmp.setOnTouchListener(this);
        	
        	tmp.setId(i);
        	
        	tmp.setLayoutParams(params);
        	
        	upper.addView(tmp);
        	
        }
        
        params = new LayoutParams();
        
        SearchView search = new SearchView(this);
    	
    	params.width = width - (6*(width/10)+10);
    	
    	params.height = 60;
    	
    	params.gravity = Gravity.CENTER_VERTICAL;
    	
    	search.setLayoutParams(params);
    	
    	upper.addView(search);
		
	}
	
	//********************* setting the menu of the viewers **************//
	
	
}
