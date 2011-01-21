/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author zerone
 */
public class BotState {

    private String response = "";
    private HashMap<String, String> keywords = new HashMap<String, String>();
    private String value;

    public BotState(String value, String response, NodeList keywords) {
        this.value = value;
        this.response = response;

        for (int i = 0; i < keywords.getLength(); i++) {
            Node e =  keywords.item(i);
            String targetState = e.getAttributes().getNamedItem("target-state").getNodeValue();
            String keyword = e.getTextContent();

            this.keywords.put(keyword, targetState);
        }
    }

    public HashMap<String, String> getKeywords(){
        return this.keywords;
    }

    public String getResponse(){
        return this.response;
    }

    public String getValue(){
        return this.value;
    }
}
