package vis.uky.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class ExtrasViewer extends Activity implements OnClickListener, OnTouchListener, OnSeekBarChangeListener{

	private ArtWork[] manuscripts;
	
	private int page, identifier;
	
	private Bitmap b;
	
	private String config;
	
	private TextView pageNumber;
	
	private LinearLayout upper, lower, main;
	
	private Parser par;
	
	private float x1, x2, dx;
	
	private LayoutParams params;
	
	private TableLayout table;
	
	private TextView translation;
	
	private ImageView highRes;
	
	private ImageCache ic;
	
	private int vis = 0, width, height;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());//allowing the main thread to do http request
        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());//allowing the main thread to do http request
		
		super.onCreate(savedInstanceState);//creates the view
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);//requests for no title window
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//sets the fullscreen
		
		setContentView(R.layout.viewer);//sets the content of the View to be the layout on the manuscript xml file
		
		identifier = getIntent().getIntExtra("identifier", -1);
		
		config = getIntent().getStringExtra("config");
		
		ic = new ImageCache();
		
		table = (TableLayout)findViewById(R.id.manuslay);//getting the Table Layout element with id manuslay
		
        Display display = getWindowManager().getDefaultDisplay();//getting the display as an object
        
        Point size = new Point();//creating a Point object
        
        display.getSize(size);//getting the size of the screen as a Point object and storing it on the Point object named size
        
        width = size.x;//setting a variable for the width of the screen
        
        height = size.y;//setting a variable for the height of the screen
        
        page = getIntent().getIntExtra("page", -1);
        
        System.out.println(config);
        
        par = new Parser();
        		
        ic.setCache(page);
        
        b = ic.getCurrent();
                
        par.setStr(config);
        
        manuscripts = par.FillStructures();
        
        
        //************* Upper Menu *************//
        
        
		upper = (LinearLayout)findViewById(R.id.upper);
        
		params = new LayoutParams();
		
        params.height = height/12;
        
        params.width = LayoutParams.MATCH_PARENT;
        
        upper.setLayoutParams(params);
        
        upper.setBackgroundResource(R.raw.navigation);
        
        Button tmp;
        
        String menu[] = {"Hi-Res", "Multispectral", "3D", "Legacy", "Translation", "Extras"};
        
		params = new LayoutParams();
        
        for(int i=0; i<6; i++){
        	
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
        
        
        //*************** Upper Menu ****************//
        
        
        //**************** Main **************//
        
        
        main = (LinearLayout)findViewById(R.id.main);
        
		params = new LayoutParams();
        
        params.height = height-(height/6);
        
        params.width = LayoutParams.MATCH_PARENT;
        
        main.setLayoutParams(params);
        
        WebView w = new WebView(this);
        
        w.loadUrl("http://www.youtube.com/watch?v=jpN-NziGOoM&feature=player_embedded");
        
        main.addView(w);
        
        //************** Main ******************//
        
        
        //************* Lower Menu *************//
        
        
		lower = (LinearLayout)findViewById(R.id.lower);
        
		params = new LayoutParams();
		
        params.height = height/12;
        
        params.width = width;
        
        lower.setLayoutParams(params);
        
        Button home = (Button)findViewById(R.id.home);
        
        home.setBackgroundResource(R.raw.home_button);
        
        home.setOnClickListener(this);
        
        home.setId(7);
        
		params = new LayoutParams();
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = (height/12) - 10;
        
        home.setLayoutParams(params);
        
        home.setPadding(40,0,40,0);
        
        Button toc = (Button)findViewById(R.id.toc);
        
        toc.setBackgroundResource(R.raw.table_of_contents);
        
        toc.setOnClickListener(this);
        
        toc.setId(9);
        
		params = new LayoutParams();
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = (height/12)-10;
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        toc.setLayoutParams(params);
        
        toc.setPadding(40,0,40,0);
        
        SeekBar seekbar = (SeekBar)findViewById(R.id.seekbar);
        
        seekbar.setKeyProgressIncrement(1);
        
        seekbar.setMax(200);
        
        seekbar.setOnSeekBarChangeListener(this);
        
		params = new LayoutParams();
        
        params.height = LayoutParams.MATCH_PARENT;
        
        params.width = width-400;
        
        params.height = (height/12)-10;
        
        params.gravity = Gravity.CENTER_VERTICAL;
        
        seekbar.setLayoutParams(params);
        
        seekbar.setPadding(40,0,40,0);
        
        pageNumber = (TextView)findViewById(R.id.pageNum);
        
        pageNumber.setText("Page: "+ page);
        
		params = new LayoutParams();
        
        params.width = LayoutParams.WRAP_CONTENT;
        
        params.height = -50;
        
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
        
        params.gravity = Gravity.RIGHT;
        
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
		
		switch(arg1.getAction()){

		case(MotionEvent.ACTION_DOWN):
			
			x1 = arg1.getRawX();
		
			System.out.println("Down: "+x1);
		
			break;
		
		case(MotionEvent.ACTION_UP):{
			
			x2 = arg1.getRawX();
			
			System.out.println("Up: "+x2);
			
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
				
				page += 1;
				
				b = ic.getNext();
				
				highRes.setImageBitmap(b);
				
				highRes.setId(page);
				
				pageNumber.setText("Page: " + Integer.toString(page));
				
				Thread thread = new Thread(){
					
					public void run(){
						
						synchronized(this){						
						
							ic.cacheForward(page);
						
						}
							
					}
					
				};
				
				thread.start();
					
			}
			
			if(dx>0 && page != 1){
				
				page -= 1;
				
				b = ic.getPrev();
				
				highRes.setImageBitmap(b);
						
				highRes.setId(page);
				
				pageNumber.setText("Page: " + Integer.toString(page));
						
				ic.cacheRewind(page);
							
			}
		
		}
		
		}
		return super.onTouchEvent(arg1);
		
	}
	
	
	
	//***************Handling the onTouch Event ***********************//
	
	
	//************** getting the pressed effect on the upper menu buttons *****************//

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		
		case MotionEvent.ACTION_POINTER_DOWN:
			
			System.out.println(event.getX());
		
		case MotionEvent.ACTION_DOWN:
			
			v.setBackgroundColor(Color.LTGRAY);
			
			System.out.println(event.getX());
			
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
	
		highRes.setImageBitmap(b);
				
		highRes.setId(page);
		
		
	}
	
	
}
