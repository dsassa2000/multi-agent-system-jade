package de.unihildesheim.iis.jadedemo;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Jade Agent template
 *
 * @author Viktor Eisenstadt
 */
public class AgentOne extends Agent {
  private static final long serialVersionUID = 1L;
  private ACLMessage message;

  public AgentOne(ACLMessage message) {
      this.message = message;
  }

  protected void setup() {
    send(message);
    // Define the behaviour
    CyclicBehaviour loop = new CyclicBehaviour(this) {
      private static final long serialVersionUID = 1L;
     
      @Override
      public void action() {
        ACLMessage aclMsg = receive();

        // Interpret the message
        if (aclMsg != null) {
          // do something
          System.out
              .println(myAgent.getLocalName() + "> Received message from: " + aclMsg.getSender());
          System.out.println("Received solution: " + aclMsg.getContent());
          ACLMessage newMsg = new ACLMessage(ACLMessage.REQUEST);
          newMsg.addReceiver(new AID("AgentTwo", AID.ISLOCALNAME));
          newMsg.setContent(aclMsg.getContent());
          send(newMsg);
        }

        block(); // Stop the behaviour until next message is received
      }
    };
    addBehaviour(loop);
  }
}
