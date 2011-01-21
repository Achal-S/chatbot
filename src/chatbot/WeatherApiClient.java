/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author zerone
 */
public class WeatherApiClient {

    private String woeid = "44418"; //woeid for london
    private String requestUri = "http://weather.yahooapis.com/forecastrss?"; //url to rss feed for weather forcast
    private String unit = "c";
    private Node today = null;
    private Node tomorrow = null;

    public WeatherApiClient() {
        String requestUrl = requestUri + "w=" + woeid + "&u=" + unit;

        Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(requestUrl);

            NodeList forcast = dom.getElementsByTagName("yweather:forecast");


            today = forcast.item(0);
            tomorrow = forcast.item(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTodayForecast() {
        String result = "";
        String low = today.getAttributes().getNamedItem("low").getNodeValue();
        String high = today.getAttributes().getNamedItem("high").getNodeValue();
        String text = today.getAttributes().getNamedItem("text").getNodeValue();

        result = "Today is " + text + " outside and is " + low + " based on yahoo weather.";
        return result;
    }

    public String getTomorrowForecast() {
        String result = "";
        String low = tomorrow.getAttributes().getNamedItem("low").getNodeValue();
        String high = tomorrow.getAttributes().getNamedItem("high").getNodeValue();
        String text = tomorrow.getAttributes().getNamedItem("text").getNodeValue();

        result = "Tomorrow will be low of " + low + " celsius, high of " + high + " celsius and would be " + text;
        return result;
    }
}
