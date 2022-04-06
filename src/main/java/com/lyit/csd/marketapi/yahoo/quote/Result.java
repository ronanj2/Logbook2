package com.lyit.csd.marketapi.yahoo.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple POJO class used for deserializing responses from yahoo finance api.
 * This class provides the outcome result with the following information, MarketTime,
 * MarketPrice, MarketChange, MarketChangePercent, MarketPreviousClose,
 * MarketOpen, fullExchangeName, quoteType, displayName, symbol, shortName,
 * region Plain java required by the yahoo finance api.
 * Holds the data returned by the api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
  public int regularMarketTime;
  public double regularMarketPrice;
  public double regularMarketChange;
  public double regularMarketChangePercent;
  public double regularMarketPreviousClose;
  public double regularMarketOpen;
  public String fullExchangeName;
  public String quoteType;
  public String displayName;
  public String symbol;
  public String shortName;
  public String region;
}