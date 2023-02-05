package de.unihildesheim.iis.jadedemo;

import java.io.File;
import java.io.FileFilter;

public class textFileFilter implements FileFilter{

	@Override
	   public boolean accept(File pathname) {
	      return pathname.getName().toLowerCase().endsWith(".txt");
	   }
	
}
