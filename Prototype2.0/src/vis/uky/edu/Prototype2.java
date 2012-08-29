package vis.uky.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

public class Prototype2 extends Activity implements OnClickListener {
	
	private Parser par;//define the parser
	
	private ArtWork manuscripts[];//define the array of manuscripts
	
	private Button b;//define a button type variable
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());//allowing the main thread to do http request
        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());//allowing the main thread to do http request
        
    	super.onCreate(savedInstanceState);//Saves the instance state of the view
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);//requests for no title window
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//sets the fullscreen
        
        setContentView(R.layout.main);//set the content of the view to the xml layout called main
        
        Display display = getWindowManager().getDefaultDisplay();//getting the display as an object
        
        Point size = new Point();//creating a Point object
        
        display.getSize(size);//getting the size of the screen as a Point object and storing it on the Point object named size
        
        int width = size.x;//setting a variable for the width of the screen
        
        int height = size.y;//setting a variable for the height of the screen
        
        LinearLayout ly = (LinearLayout)findViewById(R.id.layout);//get the element LinearLayout with id 'layout'
       
        ly.setBackgroundResource(R.raw.infof);//set the background of the element to the infof background image in the raw file
        
        par = new Parser();//initializing the parser to a new parser
        
        par.requestDocument("http://infoforest.vis.uky.edu/InfoForest/Krystel_web/config1.xml");//requesting the configuration file from the server
        
        manuscripts = par.FillStructures();//storing the data of the configuration file data to the instances of manuscripts
        
        TableLayout table = new TableLayout(this);//setting a TableLayout
        
        LayoutParams params = new LayoutParams();//defining and initializing the parameters object
        
        params.height = 90;//setting the height for the buttons
        
        for(int i=0; i<manuscripts.length; i++){//for loop to read every manuscript from the array of manuscripts
        	
        	b = new Button(this);//initializing b to be a new button
        	
        	b.setText(manuscripts[i].getName());//setting the text of the button to be the name of the current manuscript
                    	
        	b.setId(i);//set the id of the button to the same value of the index of that manuscript in the array
        	
        	b.setOnClickListener(this);//set the onclick listener to that button so that when the user clicks it an action can be done 
        			
        	b.setLayoutParams(params);//set the parameters to the button
        	
        	table.addView(b);//adding the valid button to the table  
        			
        }
        
        //**************Setting the properties of the table****************//
        
        
        params = new LayoutParams();//defining and initializing the parameters object
        
        params.height = (90*(manuscripts.length+3));//setting the height of the table to be 410 pixels less than the actual height of the screen
        
        params.width = (width/3);//setting the width of the table to be 1/3 of the actual height of the screen
        
        params.topMargin = (height/4);//setting the top margin of the table to be 1/4 of the actual screen height
        
        params.gravity = Gravity.CENTER_HORIZONTAL;//setting the left margin of the table to be 1/3 pixels plus 620 pixels less than the actual screen width
             
        table.setLayoutParams(params);//set the layout parameters of the table to be these that I have declared
        
        ScrollView sv = new ScrollView(this);//define and Initialize the scrollView
        
        sv.setLayoutParams(params);//set the same properties to the ScrollView
        
        sv.addView(table);//add the table to the scrollview
          
        ly.addView(sv);//add the scrollView to the Linear Layout or Main Layout
        
        
        //**************Setting the properties of the table****************//
        
    }

	@Override
	public void onClick(View v) {//onClick defines what to do when a button gets pressed
		
		Intent i = new Intent(this, ManuscriptView.class);//create an intent object for the Manuscript View
		
		i.putExtra("identifier", v.getId());//send the Id of the button to the Manuscript View so it knows what manuscript got pressed
		
		i.putExtra("configfile", par.getStr());//send the configuration file so it doesnt have to fetch it from the server again
		
		startActivity(i);//Finally create and display the Manuscript View
		
	}
    
}