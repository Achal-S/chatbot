/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author zerone
 */
public class BotBrain {

    private HashMap<String, String> memory = new HashMap<String, String>();
    private PersistanceHandler ph = new PersistanceHandler();
    private HashMap<String, BotState> states = new HashMap<String, BotState>();
    private WeatherApiClient weatherClient = new WeatherApiClient();
    private String stateValue = "8";
    private BotState current = null;
    private String message = "";
    private String response = "";
    private boolean continueTalk = true;

    public BotBrain() {
        this.states = ph.getStates();
        this.memory.put("chatter", "stranger");


        selectState(stateValue);
    }

    public String getResponse() {
        return this.response;
    }

    public void process(String message) {
        if (this.stateValue.equals("0")) {
            return; // session ended...
        }
        if (this.stateValue.equals("-1")) //reset to starting state....
        {
            selectState("1");
        }


        this.message = message;

        HashMap<String, String> keywords = this.current.getKeywords();

        boolean matched = false;
        boolean functionCall = false;
        String targetState = "-1";


        for (String keyword : keywords.keySet()) {
            targetState = keywords.get(keyword);
            
            if (keyword.equals("*")) {
                matched = true;
            } else if(keyword.indexOf("(") != -1){
                String key = null;
                if(keyword.indexOf("#")!= -1){
                    int start = keyword.indexOf("#");
                    int end = keyword.indexOf("#",start+1);
                    key = keyword.substring(start + 1, end);
                }
                keyword = keyword.replaceAll("#.*#", "");
                if(message.matches(keyword)){
                    matched = true;
                    Pattern p = Pattern.compile(keyword);
                    Matcher m = p.matcher(message);
                    if(m.matches()){
                        this.memory.put(key, m.group(0));
                    }
                }
                
            } else if(keyword.startsWith("/") && keyword.endsWith("/")){
                keyword = keyword.substring(1,keyword.length()-1);
                if(message.matches(keyword))
                    matched = true;
            } else if (message.indexOf(keyword) >= 0) {
                try {
                    Integer.parseInt(targetState);
                } catch (Exception e) {
                    functionCall = true;
                }

                if (functionCall) {
                    callFunction(targetState);
                } else {
                    selectState(targetState);
                }
                matched = true;
            }

            if(matched)break;
        }

        if (matched)
            selectState(targetState);
        else
            selectState("-1");
    }

    private void selectState(String state) {
        BotState s = null;
        if (this.states.containsKey(state)) {
            s = this.states.get(state);
            this.stateValue = s.getValue();
            this.response = s.getResponse();
            this.current = s;
        }

        if (this.response.indexOf("{") != -1) {
            int start = this.response.indexOf("{");
            int end = this.response.indexOf("}");
            String value = this.response.substring(start + 1, end);
            System.out.println(value);

            this.response = this.response.replaceAll("\\{.*\\}", this.memory.get(value));
        }
    }

    private void callFunction(String functionName) {
        if (functionName.equals("forecast")) {
            this.response = this.weatherClient.getTomorrowForecast();
            this.stateValue = "1";
        }
    }
}
