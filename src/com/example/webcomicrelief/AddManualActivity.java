package com.example.webcomicrelief;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class AddManualActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_add_manual);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_manual, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_confirm:
			String nameString = ((EditText) findViewById(R.id.nameField)).getText().toString();
			String urlString = ((EditText) findViewById(R.id.urlField)).getText().toString();
		    String rssString = ((EditText) findViewById(R.id.rssField)).getText().toString();
			if (nameString.length() != 0 && urlString.length() != 0 && rssString.length() != 0) {
				if (urlString.indexOf("http://") != 0) urlString = "http://"+urlString;
				if (rssString.indexOf("http://") != 0) rssString = "http://"+rssString;
				Intent returnIntent = new Intent();
				returnIntent.putExtra("newComic", new ComicInfo(nameString, urlString, rssString));
				setResult(RESULT_OK, returnIntent);
				finish();
			}
			else {
				Toast invalidToast = Toast.makeText(getApplicationContext(), "Name or RSS Feed Invalid", Toast.LENGTH_LONG);
				invalidToast.setGravity(Gravity.CENTER, 0, 0);
				invalidToast.show();
			}
			break;
		case R.id.action_cancel:
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
			break;
		}
		
		return true;
	}
}
