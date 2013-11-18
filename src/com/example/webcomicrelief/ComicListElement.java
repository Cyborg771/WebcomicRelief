package com.example.webcomicrelief;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.view.*;

public class ComicListElement extends LinearLayout {

	public ComicListElement(Context context, String title, String homepage, String unread) {
		super(context);
		
		int padding = (int)(10*getResources().getDisplayMetrics().density+0.5f);
		
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setBackgroundResource(R.drawable.element_background);
		setPadding(padding, padding, padding, padding);
		
		ImageView icon = new ImageView(context);
		icon.setImageResource(R.drawable.ic_launcher);
		icon.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		icon.setPadding(0, 0, padding, 0);
		addView(icon);
		
		LinearLayout textAndButtons = new LinearLayout(context);
		textAndButtons.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textAndButtons.setOrientation(LinearLayout.VERTICAL);
		addView(textAndButtons);
		
		TextView comicTitle = new TextView(context);
		comicTitle.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		comicTitle.setText(title);
		comicTitle.setPadding(5, 0, 0, 0);
		textAndButtons.addView(comicTitle);
		
		LinearLayout buttons = new LinearLayout(context);
		buttons.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textAndButtons.addView(buttons);
		
		Button homeButton = new Button(context, null, android.R.attr.buttonStyleSmall);
		homeButton.setText(R.string.button_homepage);
		homeButton.setOnClickListener(new OnClickListener() {
			private Context context;
			private String homepage;
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW);
				browserIntent.setData(Uri.parse(homepage));
				context.startActivity(browserIntent);
			}
			
			private OnClickListener init(Context context, String homepage) {
				this.context = context;
				this.homepage = homepage;
				return this;
			}
		}.init(context, homepage));
		buttons.addView(homeButton);
		
		Button unreadButton = new Button(context, null, android.R.attr.buttonStyleSmall);
		unreadButton.setText(R.string.button_first_unread);
		if (unread == null) {
			unreadButton.setEnabled(false);
		}
		else {
			setBackgroundResource(R.drawable.element_background_new);
			setPadding(padding, padding, padding, padding);
			unreadButton.setOnClickListener(new OnClickListener() {
				private Context context;
				private String unread;
				
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW);
					browserIntent.setData(Uri.parse(unread));
					context.startActivity(browserIntent);
				}
				
				private OnClickListener init(Context context, String unread) {
					this.context = context;
					this.unread = unread;
					return this;
				}
			}.init(context, unread));
		}
		buttons.addView(unreadButton);
	}

}
