package de.unihildesheim.iis.jadedemo;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class indexer {

	private IndexWriter writer;

	   public indexer(String indexDirectoryPath) throws IOException {
	      //this directory will contain the indexes
	      Directory indexDirectory = 
	         FSDirectory.open(new File(indexDirectoryPath));

	      //create the indexer
	      writer = new IndexWriter(indexDirectory, 
	         new StandardAnalyzer(Version.LUCENE_36),true, 
	         IndexWriter.MaxFieldLength.UNLIMITED);
	      /*Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	      IndexWriterConfig iwc = new IndexWriterConfig(null, analyzer);
	      writer = new IndexWriter(indexDirectory, iwc);*/
	   }
	   public void close() throws CorruptIndexException, IOException {
		      writer.close();
		   }
	   private Document getDocument(File file) throws IOException {
		      Document document = new Document();

		      //index file contents
		      Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));
		      //index file name
		      Field fileNameField = new Field(LuceneConstants.FILE_NAME,
		         file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		      //index file path
		      Field filePathField = new Field(LuceneConstants.FILE_PATH,
		         file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

		      document.add(contentField);
		      document.add(fileNameField);
		      document.add(filePathField);

		      return document;
		   }   

		   private void indexFile(File file) throws IOException {
		      System.out.println("Indexing "+file.getCanonicalPath());
		      Document document = getDocument(file);
		      writer.addDocument(document);
		   }

		   public int RecursiveCreateIndex(File[] arr, int level,FileFilter filter) throws IOException
		    {
		        // for-each loop for main directory files
		        for (File f : arr) {
		            // tabs for internal levels
		            for (int i = 0; i < level; i++)
		                System.out.print("\t");
		 
		            if (f.isFile()&& !f.isHidden()
				            && f.exists()
				            && f.canRead()
				            && filter.accept(f)) {
		                System.out.println(f.getName());
		                indexFile(f);
		            }
		 
		            else if (f.isDirectory()) {
		                System.out.println("[" + f.getName() + "]");
		 
		                // recursion for sub-directories
		                RecursiveCreateIndex(f.listFiles(), level + 1,filter);
		            }
		        }
		        return writer.numDocs();
		    }
	
	
}
