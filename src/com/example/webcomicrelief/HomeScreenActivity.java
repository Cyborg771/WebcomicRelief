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
import android.view.View;
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
	private boolean justAdded = false;
	
	private ArrayList<ComicInfo> comicList = new ArrayList<ComicInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		setup();
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
			justAdded = true;
			DialogFragment newFragment = new AddDialogFragment();
			newFragment.show(getFragmentManager(), "DeleteFragment");
			break;
		case R.id.action_refresh:
			refresh();
			break;
		}
		return true;
	}
	
	private void setup() {
		context = getApplicationContext();
		comicListLayout = (LinearLayout)findViewById(R.id.ComicListLayout);
		
		loadList();
		rebuildList();
		//refresh();
	}
	
	private void rebuildList() {
		comicListLayout.removeAllViews();
		for (int i = 0; i < comicList.size(); i++) {
			comicListLayout.addView(new ComicListElement(this, comicList.get(i).getName(), comicList.get(i).getUrl(), null, comicList.get(i)));
			if (comicList.get(i).getFirstNew() != null) {
				getElement(comicList.get(i)).setUnread(comicList.get(i).getFirstNew());
			}
		}
	}
	
	private void refresh() {
		Log.w("MyTag", "REFRESHING");
		for (int i = 0; i < comicListLayout.getChildCount(); i++) {
			if (comicList.get(i).getFirstNew() == null) {
				GetRSSDataTask task = new GetRSSDataTask();
				task.execute(((ComicListElement)comicListLayout.getChildAt(i)).info.getRss(), ((ComicListElement)comicListLayout.getChildAt(i)).info.getName());
			}
		}
		saveList();
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
	
	public void saveList() {
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				ComicInfo newComic = (ComicInfo) data.getSerializableExtra("newComic");
				comicList.add(newComic);
				
				GetRSSDataTask task = new GetRSSDataTask();
				task.execute(newComic.getRss(), newComic.getName());
				
				saveList();
				rebuildList();
				refresh();
			}
		}
	}
	
	private class GetRSSDataTask extends AsyncTask<String, Void, ArrayList<RssItem>> {
		private String name;
		@Override
		protected ArrayList<RssItem> doInBackground(String... data) {
			try {
				RssReader rssReader = new RssReader(data[0]);
				name = data[1];
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
				checkMostRecent(name, null);
			} else {
				//for (int i = 0; i < result.size(); i++) {
					//Log.w("MyTag", result.get(i).getLink());
				//}
				checkMostRecent(name, result);
			}
		}
    }
	
	private void checkMostRecent(String name, ArrayList<RssItem> rssData) {
		for (int i = 0; i < comicList.size(); i++) {
			if (comicList.get(i).getName().equals(name)) {
				if (rssData == null) {
					getElement(comicList.get(i)).setBroken();
				}
				else {
					comicList.get(i).setMostRecent(rssData.get(0).getLink());
					if (comicList.get(i).getLastRead() == null || !comicList.get(i).getLastRead().equals(comicList.get(i).getMostRecent())) {
						String next = getNext(comicList.get(i).getLastRead(), rssData);
						getElement(comicList.get(i)).setUnread(next);
						comicList.get(i).setFirstNew(next);
						saveList();
					}
					else {
						getElement(comicList.get(i)).setUnread(null);
					}
				}
			}
		}

	}
	
	private ComicListElement getElement(ComicInfo info) {
		for (int i = 0; i < comicListLayout.getChildCount(); i++) {
			if (((ComicListElement)comicListLayout.getChildAt(i)).info == info) return (ComicListElement)comicListLayout.getChildAt(i); 
		}
		return null;
	}
	
	private String getNext(String lastRead, ArrayList<RssItem> rssData) {
		String retVal = lastRead;
		//Log.w("MyTag", rssData.toString());
		for (int i = 1; i < rssData.size(); i++) {
			if (rssData.get(i).getLink() != null && rssData.get(i).getLink().equals(lastRead)) {
				retVal = rssData.get(i-1).getLink();
			}
		}
		if (retVal == null) retVal = rssData.get(0).getLink();
		return retVal;
	}
	
	public void removeComic(ComicInfo info) {
		for (int i = 0; i < comicList.size(); i++) {
			if (comicList.get(i) == info) {
				comicList.remove(i);
				break;
			}
		}
		saveList();
		rebuildList();
	}

}
