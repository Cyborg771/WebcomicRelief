package com.example.webcomicrelief;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HomeScreenActivity extends Activity implements AddDialogFragment.DeleteDialogListener {
	
	private LinearLayout comicListLayout;
	private Context context;
	
	private ArrayList<ComicInfo> comicList = new ArrayList<ComicInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		//GetRSSDataTask task = new GetRSSDataTask();
        //task.execute("http://xkcd.com/rss.xml");
		
		setup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_add:
			DialogFragment newFragment = new AddDialogFragment();
			newFragment.show(getFragmentManager(), "DeleteFragment");
			break;
		}
		return true;
	}
	
	private void setup() {
		context = getApplicationContext();
		comicListLayout = (LinearLayout)findViewById(R.id.ComicListLayout);
		
		loadList();
		refresh();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				ComicInfo newComic = (ComicInfo) data.getSerializableExtra("newComic");
				comicList.add(newComic);
				saveList();
				refresh();
			}
		}
	}
	
	private void refresh() {
		comicListLayout.removeAllViews();
		for (int i = 0; i < comicList.size(); i++) {
			comicListLayout.addView(new ComicListElement(this, comicList.get(i).getName(), comicList.get(i).getUrl(), "http://www.google.com/"));
		}
	}
	
	private void loadList() {
		try {
			FileInputStream fileIn = new FileInputStream(context.getFilesDir().getPath().toString()+"/ComicsList.dat");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try {
				int size = in.readInt();
				for (int i = 0; i < size; i++) {
					comicList.add((ComicInfo) in.readObject());
				}
			} catch (Exception e) {
			}
			in.close();
		} catch (IOException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void saveList() {
		try {
			FileOutputStream fileOut = new FileOutputStream(context.getFilesDir().getPath().toString()+"/ComicsList.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeInt(comicList.size());
			for (int i = 0; i < comicList.size(); i++) {
				out.writeObject(comicList.get(i));
			}
			out.close();
		} catch (IOException e) {
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		Intent addIntent = new Intent(context, AddManualActivity.class);
		startActivityForResult(addIntent, 1);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		Intent addIntent = new Intent(context, AddListActivity.class);
		startActivityForResult(addIntent, 1);
	}
	
	private class GetRSSDataTask extends AsyncTask<String, Void, ArrayList<RssItem>> {
		@Override
		protected ArrayList<RssItem> doInBackground(String... urls) {
			try {
				RssReader rssReader = new RssReader(urls[0]);
				return rssReader.getItems();
			} catch (Exception e) {
				Log.w("MyTag", e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<RssItem> result) {
			if (result == null) {
				Log.w("MyTag", "Result is null"); 
			} else {
				for (int i = 0; i < result.size(); i++) {
					Log.w("MyTag", result.get(i).getTitle());
				}
			}
		}
		
    }

}
