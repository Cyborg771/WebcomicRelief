package com.example.webcomicrelief;

import java.io.Serializable;

public class ComicInfo implements Serializable {

	private static final long serialVersionUID = 3L;
	
	private String name;
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	
	private String url;
	public void setUrl(String url) {this.url = url;}
	public String getUrl() {return url;}
	
	private String rss;
	public void setRss(String rss) {this.rss = rss;}
	public String getRss() {return rss;}
	
	private String rssData;
	public void setRssData(String rssData) {this.rssData = rssData;}
	public String getRssData() {return rssData;}
	
	private String lastRead;
	public void setLastRead(String lastViewed) {this.lastRead = lastViewed;}
	public String getLastRead() {return lastRead;}
	
	private String firstNew;
	public void setFirstNew(String firstNew) {this.firstNew = firstNew;}
	public String getFirstNew() {return firstNew;}
	
	private String mostRecent;
	public void setMostRecent(String mostRecent) {this.mostRecent = mostRecent;}
	public String getMostRecent() {return mostRecent;}
	
	public ComicInfo(String name, String url, String rss){
		this.name = name;
		this.url = url;
		this.rss = rss;
	}
	
}
