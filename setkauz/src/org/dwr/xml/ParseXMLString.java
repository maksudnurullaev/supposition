package org.dwr.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dwr.threads.WeatherService;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ParseXMLString {
	public static final String FORECAST 		= "FORECAST";	// информация о сроке прогнозирования
	public static final String FORECAST_day 	= "day";
	public static final String FORECAST_month 	= "month";
	public static final String FORECAST_year 	= "year";
	public static final String FORECAST_hour 	= "hour";
	public static final String FORECAST_tod 	= "tod"; 		// время суток, для которого составлен прогноз: 0 - ночь 1 - утро, 2 - день, 3 - вечер
	public static final String FORECAST_predict = "predict";	// заблаговременность прогноза в часах
	public static final String FORECAST_weekday = "weekday";	// день недели, 1 - воскресенье, 2 - понедельник, и т.д.
	
	public static final String PHENOMENA 				= "PHENOMENA";		// атмосферные явления
	public static final String PHENOMENA_cloudiness 	= "cloudiness";		// облачность по градациям:  0 - ясно, 1- малооблачно, 2 - облачно, 3 - пасмурно
	public static final String PHENOMENA_precipitation 	= "precipitation";	// тип осадков: 4 - дождь, 5 - ливень, 6,7 – снег, 8 - гроза, 9 - нет данных, 10 - без осадков
	public static final String PHENOMENA_rpower 		= "rpower";			// интенсивность осадков, если они есть. 0 - возможен дождь/снег, 1 - дождь/снег
	public static final String PHENOMENA_spower			= "spower";			// вероятность грозы, если прогнозируется: 0 - возможна гроза, 1 - гроза
	
	public static final String PRESSURE 	= "PRESSURE";		// атмосферное давление, в мм.рт.ст.
	public static final String PRESSURE_min	= "min";		
	public static final String PRESSURE_max	= "max";

	public static final String TEMPERATURE 	= "TEMPERATURE";	// температура воздуха, в градусах Цельсия
	public static final String TEMPERATURE_min	= "min";		
	public static final String TEMPERATURE_max	= "max";
	
	public static final String WIND 			= "WIND";		// приземный ветер м/с
	public static final String WIND_min			= "min";		
	public static final String WIND_max			= "max";	
	public static final String WIND_direction	= "direction";	// направление ветра в румбах, 0 - северный, 1 - северо-восточный,  и т.д.
	
	public static final String RELWET 		= "RELWET";			// относительная влажность воздуха, в %
	public static final String RELWET_min	= "min";		
	public static final String RELWET_max	= "max";	
	
	private static Log _log = LogFactory.getLog(ParseXMLString.class);	

	public String parse(String inXMLString) {
		// String xmlRecords =
		// "<data>" +
		// " <employee>" +
		// "   <name>John</name>" +
		// "   <title>Manager</title>" +
		// " </employee>" +
		// " <employee>" +
		// "   <name>Sara</name>" +
		// "   <title>Clerk</title>" +
		// " </employee>" +
		// "</data>";
		
		_log.debug("start parsing XML string...");
		String result = "";

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(inXMLString));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName(FORECAST);

			// iterate the employees
			_log.debug("Found "  + nodes.getLength() + " FORECAST node(s)");
			String tempString = "";
			NodeList nodeList = null;
			Element tempElement = null;

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				
				// Start weather DIV
				tempString =  "<div class=\"weather\">";
				// 1. Get date
				tempString += String.format("<strong>text.Forecast.for %s:</strong><br />", 
						String.format("%s.%s.%s text.at %s:00 (text.tod.%s)",
							element.getAttribute(FORECAST_day),
							element.getAttribute(FORECAST_month),
							element.getAttribute(FORECAST_year),
							element.getAttribute(FORECAST_hour),
							element.getAttribute(FORECAST_tod))
							);
				nodeList = element.getElementsByTagName(TEMPERATURE);
				// 2. Get temperature
				if(nodeList.getLength() == 1){
					tempElement = (Element) nodeList.item(0);
					tempString += String.format("<u>text.temperature %s(&#8451;)</u>",
							String.format("%s/%s", 
									tempElement.getAttribute(TEMPERATURE_min),
									tempElement.getAttribute(TEMPERATURE_max)));
				}
				// 3. Get pressure				
				nodeList = element.getElementsByTagName(PRESSURE);
				if(nodeList.getLength() == 1){
					tempElement = (Element) nodeList.item(0);
					tempString += String.format(", text.pressure %s(text.pressure.measure)",
							String.format("%s/%s", 
									tempElement.getAttribute(PRESSURE_min),
									tempElement.getAttribute(PRESSURE_max)));
				}
				// 4. Get relwet				
				nodeList = element.getElementsByTagName(RELWET);
				if(nodeList.getLength() == 1){
					tempElement = (Element) nodeList.item(0);
					tempString += String.format(", text.relwet %s%%",
							String.format("%s/%s", 
									tempElement.getAttribute(RELWET_min),
									tempElement.getAttribute(RELWET_max)));
				}				
				
				// 5. Get phenomena				
				nodeList = element.getElementsByTagName(PHENOMENA);
				if(nodeList.getLength() == 1){
					tempElement = (Element) nodeList.item(0);
					tempString += String.format(", <u>text.cloudiness.%s</u>",tempElement.getAttribute(PHENOMENA_cloudiness));
					if(!tempElement.getAttribute(PHENOMENA_precipitation).equals("0") 
							&& tempElement.getAttribute(PHENOMENA_precipitation).equals("9")
							&& tempElement.getAttribute(PHENOMENA_precipitation).equals("10")){
						tempString += String.format(", <u>text.precipitation.%s</u>",tempElement.getAttribute(PHENOMENA_precipitation));
					}
				}						
				
				
				// End  weather DIV
				tempString +="</div>";
				result += tempString;
			}
		} catch (Exception e) {
			result = WeatherService.formateErrorData(e.getMessage());
		}
		
		return result;

	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
