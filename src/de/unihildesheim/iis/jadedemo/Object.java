package de.unihildesheim.iis.jadedemo;

import java.io.Serializable;

public class Object implements Serializable{
	
	private String path;
	private String query;
	
	
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public Object(String path,String query) {
		this.path = path;
		this.query = query;		
	}

}
