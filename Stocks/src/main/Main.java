package main;

import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;
import result.Allocations;
import result.CalculationResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import JSON.StockList;

import java.math.BigDecimal;
import java.math.RoundingMode;
public class Main {
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	 static ArrayList<String> symbols = new ArrayList<String>();
	 static ArrayList<Integer> volumes = new ArrayList<Integer>();
	 static ArrayList<Allocations> allocations = new ArrayList<Allocations>();
	
	public static void main(String[] args) throws JSONException, FileNotFoundException, IOException { 
				
			StringBuilder sb = new StringBuilder();
	        try (BufferedReader in = new BufferedReader(new FileReader("D:\\stocks.json"))) { //В файле лежит текст в формате JSON из задания
	            String line;
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	        }
	        String json = sb.toString();
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	        	StockList rjs = mapper.readValue(json, StockList.class);
	        	for (int i = 0; i < (rjs.getStocks().size()); i++) {  //Пришедший JSON парсится и данные добавляются в соответствующие коллекции
	        		symbols.add(rjs.getStocks().get(i).getSymbol());
	        		volumes.add(rjs.getStocks().get(i).getVolume());
	        	}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	       
	       makeObject();
	       
	}
	
	public static void makeObject() throws JSONException {
		 
		int value = 0;
		final IEXTradingClient iexTradingClient = IEXTradingClient.create();
        for (int i = 0; i < symbols.size(); i++) {
        	Allocations allocation = new Allocations();
        	int assetValue;
        	double latestPrice;
        	String sector;
        	final Quote quote = iexTradingClient.executeRequest(new QuoteRequestBuilder() //Создание запроса к API
                .withSymbol(symbols.get(i))
                .build());
        	JSONObject jsonObject = new JSONObject(quote);
        	latestPrice = Double.parseDouble(jsonObject.getString("latestPrice"));
        	sector = jsonObject.getString("sector");
        	assetValue = (int) (volumes.get(i) * latestPrice);
        	allocation.setAssetValue(assetValue);
        	allocation.setAssetValue(assetValue);
        	allocation.setSector(sector);
        	allocations.add(allocation);
        	value = value + allocations.get(i).getAssetValue();
        }
        for (int i = 0; i < allocations.size(); i++) {
        	double tempProportion;
        	double proportion;
        	tempProportion = (double) allocations.get(i).getAssetValue()/value;
        	proportion = new BigDecimal(tempProportion).setScale(3, RoundingMode.UP).doubleValue(); //Округление proportion
        	allocations.get(i).setProportion(proportion);
        }
        CalculationResult calculationResult = new CalculationResult(value, allocations);
        String resultJson =  GSON.toJson(calculationResult);
        System.out.println(resultJson);
	}
	
}
