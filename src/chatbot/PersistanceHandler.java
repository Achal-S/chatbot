/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author zerone
 */
public class PersistanceHandler {

    private String dbFilePath = "src/db.xml";
    private File file = new File(dbFilePath);
    private Document dom = null;
    private HashMap<String, BotState> states = new HashMap<String, BotState>();

    public PersistanceHandler() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(dbFilePath);
            dom.normalizeDocument();

            this.parseDocument();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseDocument() {
        NodeList nl = dom.getElementsByTagName("state");
        BotState tmp = null;

        for (int i = 0; i < nl.getLength(); i++) {
            Element state = (Element) nl.item(i);
            String stateValue = state.getAttribute("value");
            System.out.println("state value" + stateValue);
            String response = state.getElementsByTagName("response").item(0).getTextContent();
            NodeList keywords = state.getElementsByTagName("keyword");

            tmp = new BotState(stateValue, response, keywords);
            this.states.put(stateValue, tmp);
        }
    }

    public HashMap<String, BotState> getStates() {
        return this.states;
    }
}
