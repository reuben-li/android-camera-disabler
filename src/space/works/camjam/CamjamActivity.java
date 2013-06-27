package space.works.camjam;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appbrain.AppBrain;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.localytics.android.LocalyticsSession;

public class CamjamActivity extends Activity {

	private Camera camera;
	private LocalyticsSession localyticsSession;
	Button buttonClick;
	Boolean lock = false;
	private final static String TAG_JAM = "jam";
	private final static String TAG_UNJAM = "unjam";
	
	public void onBackPressed() {
	    AppBrain.getAds().showInterstitial(this);
	}

	public void onResume(){
		super.onResume();
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AppBrain.init(this);
		this.localyticsSession = new LocalyticsSession(
		this.getApplicationContext(),  // Context used to access device resources
                "43e240dc80b2355cae50b5a-00d20e48-9e53-11e1-39a1-00ef75f32667");       // Key generated on the webservice

		this.localyticsSession.open();                // open the session
		this.localyticsSession.upload();  
		AdView adView = (AdView) this.findViewById(R.id.abc);
		adView.loadAd(new AdRequest());

		buttonClick = (Button) findViewById(R.id.jam);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if (!getPackageManager()
						.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
					Toast.makeText(CamjamActivity.this, "No camera on this device", Toast.LENGTH_LONG)
							.show();
				} else {
					try{
					camera = Camera.open(); // very important	
					}
					catch(Exception e){Toast.makeText(CamjamActivity.this, "Camera already locked", Toast.LENGTH_LONG)
						.show();}
				}	
							
				localyticsSession.tagEvent(CamjamActivity.TAG_JAM);
				lock = true;
				TextView t = (TextView) findViewById(R.id.state);
				t.setText("CAMERA LOCKED");
			}
		});

		buttonClick = (Button) findViewById(R.id.unjam);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View p) {
				// camera.takePicture(null,null,null);
				lock = false;
				localyticsSession.tagEvent(CamjamActivity.TAG_UNJAM);
				// camera.release();
				TextView t = (TextView) findViewById(R.id.state);
				t.setText("UNLOCKED");
				Toast.makeText(CamjamActivity.this, "Camera already locked", Toast.LENGTH_LONG)
				.show();
				finish();
				System.exit(0);
			}
		});
	}


}
