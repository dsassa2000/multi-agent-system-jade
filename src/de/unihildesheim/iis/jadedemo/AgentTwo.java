package de.unihildesheim.iis.jadedemo;

import jade.core.Agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class AgentTwo extends Agent {
  private static final long serialVersionUID = 1L;
  
  String indexDir = "C:\\Users\\HP\\Documents\\INDEX";
  String dataDir =   "C:\\Users\\HP\\Documents\\JAVA";                           
  indexer indexer;
  searcher searcher;
  
  // create indexing
  private void createIndex() throws IOException {
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
          
        // Interpret the message
        if (aclMsg != null) {
          System.out.println(myAgent.getLocalName()
              + "> Received message from: " + aclMsg.getSender());
          System.out.println("Message content: " + aclMsg.getContent());
          // TODO Aufgabe 1
          try {
        	  createIndex();
        	  search(aclMsg.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
	         e.printStackTrace();
	     }
          // searching word
        }
        block(); // Stop the behaviour until next message is received
      }
    };
    addBehaviour(loop);
  }
}
