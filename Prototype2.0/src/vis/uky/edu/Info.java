package vis.uky.edu;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class Info extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info);
		
		TextView t = (TextView)findViewById(R.id.info);
		
		t.setText("This feature is not available");
		
	}
	
	
}
