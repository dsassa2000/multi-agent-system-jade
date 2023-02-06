package de.unihildesheim.iis.jadedemo;

import java.io.File;
import java.io.FileFilter;

public class textFileFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {

		if (!pathname.exists()) {
		    System.out.println(pathname + " does not exist.");
		}
		    String filename = pathname.getName().toLowerCase();
		    if(filename.endsWith(".txt"))return filename.endsWith(".txt");
		    if(filename.endsWith(".html"))return filename.endsWith(".html");
		    if(filename.endsWith(".xml"))return filename.endsWith(".xml");
		    if(filename.endsWith(".pdf"))return filename.endsWith(".pdf");
		    else {
			System.out.println("Skipped " + filename);
		    }
		    return false;
	}
	
}
