package com.example.webcomicrelief;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddListActivity extends Activity {
	
	private ArrayList<ComicInfo> presetList = new ArrayList<ComicInfo>();
	private Context context;
	private LinearLayout buttonListLayout;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_add_list);
	    
	    context = getApplicationContext();
	    buttonListLayout = (LinearLayout) findViewById(R.id.buttonListLayout);
	    
	    initList();
	    
	    //Intent returnIntent = new Intent();
		//setResult(RESULT_CANCELED, returnIntent);
		//finish();
	}
	
	private void initList() {
		presetList.add(new ComicInfo("MS Paint Adventures", "http://mspaintadventures.com/", "http://www.mspaintadventures.com/rss/rss.xml"));
		presetList.add(new ComicInfo("xkcd", "http://xkcd.com", "http://xkcd.com/rss.xml"));
		presetList.add(new ComicInfo("smbc", "http://www.smbc-comics.com/#comic", "http://feeds.feedburner.com/smbc-comics/PvLb"));
		presetList.add(new ComicInfo("Questionable Content", "http://www.questionablecontent.net/", "http://www.questionablecontent.net/QCRSS.xml"));
		presetList.add(new ComicInfo("Cyanide and Happiness", "http://www.explosm.net/comics/", "http://feeds.feedburner.com/Explosm"));
		presetList.add(new ComicInfo("A Softer World", "http://www.asofterworld.com/", "http://www.rsspect.com/rss/asw.xml"));
		presetList.add(new ComicInfo("Least I Could Do", "http://www.leasticoulddo.com/", "http://feeds.feedburner.com/LICD"));
		presetList.add(new ComicInfo("VGCats", "http://www.vgcats.com/", "http://www.vgcats.com/vgcats.rdf.xml"));
		presetList.add(new ComicInfo("Penny Arcade", "http://www.penny-arcade.com/", "http://penny-arcade.com/feed"));
		presetList.add(new ComicInfo("Commissioned", "http://www.commissionedcomic.com/", "http://www.commissionedcomic.com/?feed=rss2"));
		presetList.add(new ComicInfo("Subnormality", "http://www.viruscomix.com/subnormality.html", "http://www.viruscomix.com/rss.xml"));
		presetList.add(new ComicInfo("Allan", "http://www.allancomic.com/", "http://www.allancomic.com/rss.php"));
		presetList.add(new ComicInfo("Sore Thumbs", "http://sorethumbs.keenspot.com/", "http://sorethumbs.keenspot.com/comic.rss"));
		presetList.add(new ComicInfo("Rock Paper Cynic", "http://www.rockpapercynic.com/index.php", "http://www.rsspect.com/rss/rockpapercynic.xml"));
		presetList.add(new ComicInfo("Manly Guys Doing Manly Things", "http://thepunchlineismachismo.com/", "http://thepunchlineismachismo.com/feed"));
		presetList.add(new ComicInfo("Nuclear Delight", "http://www.nucleardelight.com/index.php", "http://www.nucleardelight.com/rss.php"));
		presetList.add(new ComicInfo("Three Word Phrase", "http://threewordphrase.com/index.htm", "http://www.threewordphrase.com/rss.xml"));
		presetList.add(new ComicInfo("Gunshow", "http://gunshowcomic.com/", "http://www.rsspect.com/rss/gunshowcomic.xml"));
		presetList.add(new ComicInfo("Dark Legacy", "http://www.darklegacycomics.com/", "http://www.darklegacycomics.com/feed.xml"));
		presetList.add(new ComicInfo("Fanboys", "http://fanboys-online.com/", "http://fanboys-online.com/rss.php"));
		presetList.add(new ComicInfo("Junior Scientist Power Hour", "http://www.jspowerhour.com/", "http://www.jspowerhour.com/comics.rss"));
		presetList.add(new ComicInfo("For Lack of a Better Comic", "http://forlackofabettercomic.com/", "http://feedity.com/forlackofabettercomic-com/UFZaW1dQ.rss"));
		presetList.add(new ComicInfo("Eat That Toast", "http://eatthattoast.com/", "http://eatthattoast.com/feed/"));
		presetList.add(new ComicInfo("Dinosaur Comics", "http://www.qwantz.com/index.php", "http://www.rsspect.com/rss/qwantz.xml"));
		
		for (int i = 0; i < presetList.size(); i++) {
			Button newButton = new Button(context);
			newButton.setOnClickListener(new OnClickListener() {
				
				private int i;
				
				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("newComic", new ComicInfo(presetList.get(i).getName(), presetList.get(i).getUrl(), presetList.get(i).getRss()));
					setResult(RESULT_OK, returnIntent);
					finish();
				}
				
				public OnClickListener init(int i) {
					this.i = i;
					return this;
				}
				
			}.init(i));
			newButton.setText(presetList.get(i).getName());
			buttonListLayout.addView(newButton);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_cancel:
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
			break;
		}
		return true;
	}
}
