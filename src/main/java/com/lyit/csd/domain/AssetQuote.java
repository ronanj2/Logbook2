package com.lyit.csd.domain;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The {@link AssetQuote} class used to hold pricing information of Assets, this includes.
 * MarketPrice, MarketChangePercentage, MarketChange and MarketPreviousClose values.
 */
public class AssetQuote {
  /* You should implement relevant methods for the class. You can add additional attributes to
   * store additional information on each asset if you wish. Carefully consider the information that
   * you can retrieve from the finance API that you use and what information the user would like to
   * view or may find useful */

  private final List<Long> timestamp = new ArrayList<>();

  private final List<Double> close = new ArrayList<>();

  /**
   * The symbol of the asset e.g. APPL, TSLA, BARC or BTC-USD.
   */
  private final String assetSymbol;

  /**
   * The UNIX timestamp of the asset's quoted value. Using long instead of int to avoid the year
   * 2038 problem.
   */
  private long timeStamp;

  /**
   * The value in USD of the named asset at this point in time.
   */
  private double regularMarketPrice;

  /**
   * The change in price expressed as a percentage.
   */
  private double regularMarketChangePercent;

  /**
   * The price at which the asset closed the previous trading day.
   */
  private double regularMarketPreviousClose;

  /**
   * The change in price expressed as USD.
   */
  private double regularMarketChange;

  /**
   * Price of asset on previous market open.
   */
  private double regularMarketOpen;

  /**
   * Type of asset quote.
   */
  private String quoteType;

  /**
   * full name of the specified Asset that is being exchanged.
   */
  private String fullExchangeName;

  /** Type of asset quote.
   *
   * @param assetSymbol symbol of the asset that is being requested.
   */
  public AssetQuote(String assetSymbol) {
    this.assetSymbol = assetSymbol;
  }

  /**
   * AssetQuote Constructor.
   *
   * @param assetSymbol Stock ticker of AssetQuote object
   * @param fullExchangeName Name of exchange
   * @param timeStamp Epoch timestamp representing date
   * @param regularMarketPrice Current market price
   * @param regularMarketChangePercent Change in price since market open %
   * @param regularMarketChange Change in market price in $
   * @param quoteType Type of asset
   * @param regularMarketPreviousClose Price of asset at previous days close
   * @param regularMarketOpen Price of asset at market open
   */
  public AssetQuote(String assetSymbol, String fullExchangeName, int timeStamp,
      double regularMarketPrice, double regularMarketChangePercent,
      double regularMarketChange, String quoteType, double regularMarketPreviousClose,
      double regularMarketOpen) {
    this.assetSymbol = assetSymbol;
    this.fullExchangeName = fullExchangeName;
    this.timeStamp = timeStamp;
    this.regularMarketPrice = regularMarketPrice;
    this.regularMarketChangePercent = regularMarketChangePercent;
    this.regularMarketChange = regularMarketChange;
    this.quoteType = quoteType;
    this.regularMarketPreviousClose = regularMarketPreviousClose;
    this.regularMarketOpen = regularMarketOpen;
  }

  /**
   * Gives the symbol of the specific Asset.

   * @return a <code>string</code> with the asset symbol.
   */
  public String getAssetSymbol() {
    return this.assetSymbol;
  }

  /**
   * Gives the exchange name providing the quote.

   * @return a <code>string</code> with the exchange name providing the quote.
   */
  public String getFullExchangeName() {
    return fullExchangeName;
  }

  /** Gives the type of quote that is being requested for the Asset.

   * @return a <code>string</code> indicating the quote type.
   */
  public String getQuoteType() {
    return this.quoteType;
  }

  /** Gives the regular market price of the Asset.

   * @return a <code>double</code> with the market price for the AssetQuote.
   */
  public double getRegularMarketPrice() {
    return this.regularMarketPrice;
  }

  /** This method add the proper date and time of any asset exchange.
   *
   * @param timeStamp The exact time and date when a specific activity or event took place.
   */
  public void addTimeStampToList(long timeStamp) {
    timestamp.add(timeStamp);
  }

  /** This method adds the asset closing price to the list.
   *
   * @param closingPrice The last transacted price of an asset.
   */
  public void addClosingPriceToList(double closingPrice) {
    close.add(closingPrice);
  }

  /** This method converts data to string values.
   *
   * @return Returning asset symbol, market price, market change percent and market change values.
   */

  @Override
  public String toString() {
    return assetSymbol + "\t\t - Current Price (USD): " + regularMarketPrice
        + "\t\t - % change: " + regularMarketChangePercent + "\t\t - $ change: "
        + regularMarketChange;
  }

  /** This method converts data into string values as output.
   *
   * @return Returning the exchange name, asset symbol, market price, market change percent
   *        market change, quote type, market previous close and market open values.
   */
  public String exchangeToString() {
    return fullExchangeName + " - " + assetSymbol + "\t || Current Price (USD): "
        + regularMarketPrice + "\t || % change: " + regularMarketChangePercent + "\t || $ change: "
            + regularMarketChange + "\t || Quote Type: " + quoteType
        + "\t || Previous Close: (USD) " + regularMarketPreviousClose
            + "\t || Market Open: (USD) " + regularMarketOpen;
  }

  /**
   *This method converts historical asset data into string values.
   */
  public void historicDataToString() {
    System.out.println("HISTORIC DATA: " + assetSymbol);
    for (int i = 0; i < close.size(); i++) {
      System.out.println("Date: " + convertLongToDate(timestamp.get(i))
              + " - Closing Price (USD): " + close.get(i));
      System.out.println();
    }
  }

  /**
   * This method converts the long timestamp values into date(s).
   */
  private static String convertLongToDate(long timeStamp) {
    Date date = new Date(timeStamp * 1000);
    Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return format.format(date);
  }
}
