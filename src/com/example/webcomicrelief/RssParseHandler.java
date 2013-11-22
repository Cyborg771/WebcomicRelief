package com.example.webcomicrelief;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class RssParseHandler extends DefaultHandler {
	private ArrayList<RssItem> rssItems;
	private RssItem currentItem;
	private boolean parsingTitle;
	private StringBuffer currentLinkSb;
	private boolean parsingLink;
	
	public RssParseHandler() {
		rssItems = new ArrayList<RssItem>();
	}
	
	public ArrayList<RssItem> getItems() {
		return rssItems;
	}
	
	@Override
	public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("item".equals(qName)) {
			currentItem = new RssItem();
		} else if ("title".equals(qName)) {
			parsingTitle = true;
		} else if ("link".equals(qName)) {
			parsingLink = true;
			currentLinkSb = new StringBuffer();
		}
	}
	
	@Override
	public void endElement (String uri, String localName, String qName) {
		if ("item".equals(qName)) {
			rssItems.add(currentItem);
		} else if ("title".equals(qName)) {
			parsingTitle = false;
		} else if ("link".equals (qName)) {
			parsingLink = false;
			if (currentItem != null) {
				currentItem.setLink(currentLinkSb.toString());
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (parsingTitle) {
			if (currentItem != null) {
				currentItem.setTitle(new String(ch, start, length));
			}
		} else if (parsingLink) {
			if (currentItem != null) {
				//currentItem.setLink(new String(ch, start, length));
				currentLinkSb.append(new String(ch, start, length));
			}
			//parsingLink = false;
		}
	}
}
