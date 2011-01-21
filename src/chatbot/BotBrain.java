/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.util.HashMap;

/**
 *
 * @author zerone
 */
public class BotBrain {

    private HashMap<String,String> memory = new HashMap<String,String>();
    private PersistanceHandler ph = new PersistanceHandler();
    private HashMap<String, BotState> states = new HashMap<String, BotState>();
    private WeatherApiClient weatherClient = new WeatherApiClient();
    private String stateValue = "1";
    private BotState current = null;
    private String message = "";
    private String response = "";
    private boolean continueTalk = true;

    public BotBrain() {
        this.states = ph.getStates();
        this.memory.put("chatter", "stranger");


        selectState(stateValue);
    }

    public String getResponse(){
        return this.response;
    }

    public void process(String message) {
        if(this.stateValue.equals("0")) return; // session ended...

        if(this.stateValue.equals("-1")) //reset to starting state....
            selectState("1");

        String tmp = "";
        this.message = message;

        HashMap<String, String> keywords = this.current.getKeywords();

        boolean matched = false;
        boolean functionCall = false;

        for(String keyword: keywords.keySet()){
            if(message.indexOf(keyword)>=0){
                String targetState = keywords.get(keyword);
                try{ Integer.parseInt(targetState); } catch(Exception e){ functionCall = true;}

                if(functionCall)
                    callFunction(targetState);
                else
                    selectState(targetState);
                matched = true;
            }
        }

        if(!matched){
            selectState("-1");
        }
    }

    private void selectState(String state){
        BotState s  = null;
        if(this.states.containsKey(state)){
            s = this.states.get(state);
            this.stateValue = s.getValue();
            this.response = s.getResponse();
            this.current = s;
        }
    }

    private void callFunction(String functionName){
        if(functionName.equals("forecast")){
            this.response = this.weatherClient.getTomorrowForecast();
            this.stateValue = "1";
        }
    }
}
