package de.unihildesheim.iis.jadedemo;

import jade.core.Agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class AgentTwo extends Agent {
  private static final long serialVersionUID = 1L;
  
  String indexDir = "C:\\Users\\HP\\Documents\\INDEX";                           
  indexer indexer;
  searcher searcher;
  
  // create indexing
  private void createIndex(String dataDir) throws IOException {
      indexer = new indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      long endTime = System.currentTimeMillis();      
   // File object
      File maindir = new File(dataDir);

      if (maindir.exists() && maindir.isDirectory()) {
          // array for files and sub-directories
          // of directory pointed by maindir
          File arr[] = maindir.listFiles();

          System.out.println(
              "**********************************************");
          System.out.println(
              "Files from main directory : " + maindir);
          System.out.println(
              "**********************************************");

          // Calling recursive method
          numIndexed = indexer.RecursiveCreateIndex(arr, 0,new textFileFilter());
          indexer.close();
          System.out.println(numIndexed+" File indexed, time taken: "
             +(endTime-startTime)+" ms");
      }
   }
  // searching 
  private void search(String searchQuery) throws IOException, ParseException {
      searcher = new searcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         GUIinterface.updateUI(doc.get(LuceneConstants.FILE_PATH));
      }
      searcher.close();
   }

  public void setup() {
    // Define the behaviour
    CyclicBehaviour loop = new CyclicBehaviour(this) {
      private static final long serialVersionUID = 1L;

      @Override
      public void action() {
          
        // Receive the incoming message
        ACLMessage aclMsg = receive();
        if (aclMsg != null) {
            try {
                Serializable content = aclMsg.getContentObject();
                if (content instanceof Object) {
                    Object myObject = (Object) content;
                    // do something with myObject
                    try {
                  	  createIndex(myObject.getPath()); //C:\\Users\\HP\\Documents\\JAVA
                  	  search(myObject.getQuery());
          		} catch (IOException e) {
          			// TODO Auto-generated catch block
          			e.printStackTrace();
          		}catch (ParseException e) {
          	         e.printStackTrace();
          	     }
                }
            } catch (UnreadableException e) {
                // handle the exception
            }
        }
        block(); // Stop the behaviour until next message is received
      }
    };
    addBehaviour(loop);
  }
}
