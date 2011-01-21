/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author zerone
 */
public class BotBrain {

    private HashMap memory = new HashMap();
    private PersistanceHandler ph = new PersistanceHandler();
    private HashMap<String, BotState> states = null;
    private WeatherApiClient weatherClient = new WeatherApiClient();
    private int state = 1;

    public BotBrain(){
        this.states = ph.getStates();
    }

    public String speak(String message){
        return message;
    }

}
