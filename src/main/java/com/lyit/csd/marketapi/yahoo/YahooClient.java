package com.lyit.csd.marketapi.yahoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.yahoo.quote.Result;
import com.lyit.csd.marketapi.yahoo.quote.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * YahooClient class implements MarketClient interface to retrieve data from Yahoo Finance API.
 */
public class YahooClient implements MarketClient {
  private String baseUrl;
  private List<String> apiKeys;
  private final int maxCounter = 4;

  /**
   * Constructor for the {@link YahooClient} class.

   * @param url URL for the Yahoo Finance API.
   * @param apiKeys apiKeys to use for authenticating with Yahoo Finance API.
   */
  public YahooClient(String url, List<String> apiKeys) {
    this.baseUrl = url;
    this.apiKeys = apiKeys;
  }

  /**
   * Checks whether the symbol passed in returns data.
   *
   * @param assetSymbol symbol to be verified.
   * @return a boolean - true if the symbol returns usable data
   */
  public boolean checkAssetSymbol(String assetSymbol) {
    AssetQuote assetQuote = getQuote(assetSymbol);
    return assetQuote != null;
  }

  /**
   * Method to retrieve historic data regarding provided
   * stock tickers between predefined time interval and range.
   *
   * @param assetSymbols list of stock tickers
   * @param interval granularity of the returned data.
   * @param range time period of which data is to be gathered.
   * @return list of AssetQuote objects containing individual stock histories.
   */
  public List<AssetQuote> getHistoricalInfo(List<String> assetSymbols,
                                            String interval, String range) {
    //Remove invalid symbols
    assetSymbols.removeIf(s -> !checkAssetSymbol(s));
    //Construct URL
    String assetSymbolsSeparated = String.join(",", assetSymbols);
    String operation = "/v8/finance/spark";
    String parameters = "interval=" + interval + "&range="
            + range + "&symbols=" + assetSymbolsSeparated;
    String url = baseUrl + operation + "?" + parameters;

    //Parse response & map onto AssetQuote object
    List<AssetQuote> assetQuotes = new ArrayList<>();

    // Parse the response and return a quote
    try {
      for (String s : assetSymbols) {
        Response response = makeGetRequest(url);
        ResponseBody responseBody = response.body();
        AssetQuote assetQuote = new AssetQuote(s);

        //Convert json response into JsonElement -> JsonObject -> JsonArray
        JsonElement jsonElement = new JsonParser().parse(responseBody.string());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject(s);
        JsonArray timestampArray = jsonObject.getAsJsonArray("timestamp");

        //Extract data from JsonArray containing timestamp info
        for (int i = 0; i < timestampArray.size(); i++) {
          JsonElement temp = timestampArray.get(i);
          long value = temp.getAsLong();
          assetQuote.addTimeStampToList(value);
        }

        JsonArray closeArray = jsonObject.getAsJsonArray("close");

        //Extract data from JsonArray containing closing price info
        for (int i = 0; i < closeArray.size(); i++) {
          JsonElement temp = closeArray.get(i);
          double value = temp.getAsDouble();
          assetQuote.addClosingPriceToList(value);
        }
        //Add assetQuote object to list
        assetQuotes.add(assetQuote);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return assetQuotes;
  }

  /**
   * Queries the API for live information about the specified stock ticker.
   *
   * @param assetSymbol symbol to retrieve information about.
   * @return an AssetQuote object containing up-to-date market info
   */
  public AssetQuote getQuote(String assetSymbol) {
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add(assetSymbol);
    List<AssetQuote> assetQuotes = getQuote(assetSymbols);

    if (assetQuotes.size() == 0) {
      return null;
    }
    return assetQuotes.get(0);
  }

  /**
   * Fetches live market data on each element in list of stock tickers.
   *
   * @param assetSymbols list of stock symbols
   * @return list of AssetQuote objects containing current market data.
   */
  public List<AssetQuote> getQuote(List<String> assetSymbols) {
    String assetSymbolsSeparated = String.join(",", assetSymbols);
    String operation = "/v6/finance/quote";
    String parameters = "region=US&lang=en&symbols=" + assetSymbolsSeparated;
    String url = baseUrl + operation + "?" + parameters;
    Response response = makeGetRequest(url);
    ResponseBody responseBody = response.body();

    // Parse the response and return a quote
    ObjectMapper om = new ObjectMapper();
    List<AssetQuote> assetQuotes = new ArrayList<>();

    try {
      Root root = om.readValue(responseBody.string(), Root.class);

      if (root.quoteResponse != null) {
        for (Result result : root.quoteResponse.result) {
          String assetQuoteName = result.displayName;
          if (assetQuoteName == null || assetQuoteName.length() == 0) {
            assetQuoteName = result.shortName;
          }

          AssetQuote assetQuote = new AssetQuote(result.symbol,
              assetQuoteName,
              result.regularMarketTime,
              result.regularMarketPrice,
              result.regularMarketChangePercent,
              result.regularMarketChange,
                  result.quoteType,
                  result.regularMarketPreviousClose,
                  result.regularMarketOpen);

          assetQuotes.add(assetQuote);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return assetQuotes;
  }

  /**
   * Returns information regarding the specified exchange and region.
   *
   * @param region region where exchange is located.
   * @param exchange the exchange to return data on.
   * @return A string containing relevant information
   */
  public String getExchangeInfo(String region, String exchange) {

    String operation = "/v6/finance/quote";
    String parameters = "region=" + region + "&lang=en&symbols=" + exchange;
    String url = baseUrl + operation + "?" + parameters;
    Response response = makeGetRequest(url);
    ResponseBody responseBody = response.body();
    AssetQuote assetQuote = null;

    // Parse the response and return a quote
    ObjectMapper om = new ObjectMapper();
    try {
      Root root = om.readValue(responseBody.string(), Root.class);

      if (root.quoteResponse != null) {
        for (Result result : root.quoteResponse.result) {
          assetQuote = new AssetQuote(result.symbol,
                  result.fullExchangeName,
                  result.regularMarketTime,
                  result.regularMarketPrice,
                  result.regularMarketChangePercent,
                  result.regularMarketChange,
                  result.quoteType,
                  result.regularMarketPreviousClose,
                  result.regularMarketOpen);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (assetQuote != null) {
      return assetQuote.exchangeToString();
    } else {
      return null;
    }
  }

  /**
   * Returns a list of popular stocks by region.
   *
   * @param region region for which trending stock data is to be retrieved.
   * @return An ArrayList of strings, containing the most popular stock tickers.
   */
  public ArrayList<String> getTrendingStocksForRegion(String region) {
    String operation = "/v1/finance/trending/";
    String url = baseUrl + operation + region;
    Response response = makeGetRequest(url);
    ResponseBody responseBody = response.body();

    // Parse the response and return a quote
    ObjectMapper om = new ObjectMapper();
    ArrayList<String> trendingAssetsList = new ArrayList<>();

    try {
      com.lyit.csd.marketapi.yahoo.trending.Root root = om.readValue(responseBody.string(),
          com.lyit.csd.marketapi.yahoo.trending.Root.class);

      for (var result : root.finance.result) {
        for (var quote : result.quotes) {
          trendingAssetsList.add(removeNonAlphanumeric(quote.symbol));
        }
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return trendingAssetsList;
  }

  /**
   * Removes special characters from given string.
   *
   * @param str string of mixed characters
   * @return string containing only alphanumerics.
   */
  public static String removeNonAlphanumeric(String str) {
    // replace the given string with empty string except the pattern "[^a-zA-Z0-9]"
    str = str.replaceAll(
        "[^a-zA-Z0-9]", "");

    return str;
  }

  /**
   * Method to make get request from API.
   *
   * @param url target url containing operation to be performed.
   * @return a http response object
   */
  public Response makeGetRequest(String url) {
    return makeGetRequest(url, 1, "");
  }

  /**
   * Method to make get request from API.
   *
   * @param url target url containing operation to be performed.
   * @param counter a counter to track the number of attempts made..
   * @return a http response object
   */
  public Response makeGetRequest(String url, int counter, String lastKeyUsed) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    String apiKey = getRandomApiKey(lastKeyUsed);
    Request request = new Request.Builder()
        .url(url)
        .method("GET", null)
        .addHeader("X-API-KEY", apiKey)
        .build();
    try {
      Response response = client.newCall(request).execute();

      int responseCode = response.code();
      if (responseCode != 200 && counter < maxCounter) {
        // try again!
        return makeGetRequest(url, counter++, apiKey);
      }
      return response;

    } catch (IOException e) {
      e.printStackTrace();

      // try again
      if (counter < maxCounter) {
        return makeGetRequest(url, counter++, apiKey);
      }
    }
    return null;
  }

  /** This method gets element randomly based in input.

   * @return a random ApiKey to use for authenticating with Yahoo Finance.
   */
  private String getRandomApiKey(String lastKeyUsed) {
    Random rand = new Random();
    String keyToUse = apiKeys.get(rand.nextInt(apiKeys.size()));

    // If we've just used this key, lets try another.
    if (keyToUse == lastKeyUsed) {
      return getRandomApiKey(lastKeyUsed);
    }
    return keyToUse;
  }
}