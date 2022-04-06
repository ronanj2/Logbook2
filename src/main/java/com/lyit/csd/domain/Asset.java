package com.lyit.csd.domain;

import com.lyit.csd.marketapi.abstraction.MarketClient;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Asset} class to store relevant market info on held assets.
 */
public class Asset {
  private final String assetSymbol;
  private final long originalPurchaseDateTime;
  private final double originalPurchaseUnits;
  private final double originalPurchasePrice;
  private final List<SellTransaction> sellTransactions;
  private final MarketClient marketClient;

  /**
   * Asset constructor.
   *
   * @param assetSymbol Stock sticker of asset
   * @param purchaseDateTime Date asset was purchased
   * @param purchaseUnits Number of units purchased in order
   * @param purchasePrice Price units were purchased at
   * @param marketClient Market client to be used during purchase.
   */
  public Asset(String assetSymbol, long purchaseDateTime, double purchaseUnits,
               double purchasePrice, MarketClient marketClient) {
    this.assetSymbol = assetSymbol;
    this.originalPurchaseDateTime = purchaseDateTime;
    this.originalPurchaseUnits = purchaseUnits;
    this.originalPurchasePrice = purchasePrice;
    this.sellTransactions = new ArrayList<>();
    this.marketClient = marketClient;
  }

  /**
   * A getter for the {@link SellTransaction} of sold assets.
   *
   * @return a list of {@link SellTransaction}.
   */
  public List<SellTransaction> getSellTransactions() {
    return sellTransactions;
  }

  /**
   * A getter for <code>assetSymbol</code>.
   *
   * @return the symbol of asset.
   */
  public String getAssetSymbol() {
    return this.assetSymbol;
  }

  /**
   * A getter for the <code>originalPurchaseDate</code>.
   * This variable holds the purchase date of the asset.
   *
   * @return date and time of asset purchase.
   */
  public long getOriginalPurchaseDateTime() {
    return this.originalPurchaseDateTime;
  }

  /** This method contains the Purchase date of when asset was bought.
   *
   * @return date and time of asset purchase.
   */
  public String getOriginalPurchaseDateAsHumanReadable() {
    return DateTimeHelper.getEpochAsHumanReadable(this.getOriginalPurchaseDateTime());
  }

  /**
   * This method contains the Purchase quantity of when asset was bought.
   *
   * @return quantity amount of asset purchased.
   */
  public double getOriginalPurchaseQty() {
    return this.originalPurchaseUnits;
  }

  /**
   * This method contains the original price that has been for the asset during purchase.
   *
   * @return amount paid for the Asset.
   */
  public double getOriginalPurchasePrice() {
    return this.originalPurchasePrice;
  }

  /**
   * This method contains the summary of asset units sold.
   *
   * @return number of individual asset units sold.
   */
  public double getSumOfUnitsSold() {

    return sellTransactions.stream().mapToDouble(SellTransaction::getUnits).sum();
  }

  /**
   * This method contains the balance of asset units currently held.
   *
   * @return Balance of currently held asset units.
   */
  public double getCurrentUnitsBalance() {

    return originalPurchaseUnits - getSumOfUnitsSold();
  }

  /**
   * This method contains the value of current assets.
   *
   * @param assetQuote quote of assets.
   * @return Current value of assets.
   */
  public double getCurrentValue(AssetQuote assetQuote) {
    double currentUnitsBalance = getCurrentUnitsBalance();
    if (currentUnitsBalance <= 0) {
      return 0;
    }
    return assetQuote.getRegularMarketPrice() * currentUnitsBalance;
  }

  /**
   * This method contains the assets that are hold for sale.
   *
   * @param unitsToSell asset unit to be sold.
   * @param sellPrice Sale price of the asset.
   */
  public void sellHolding(double unitsToSell, double sellPrice) {
    SellTransaction sellTransaction = new SellTransaction(unitsToSell, sellPrice);
    sellTransactions.add(sellTransaction);
  }

  /**
   * This method contains the current asset price.
   *
   * @return price of current asset value.
   */
  public double getCurrentAssetPrice() {
    AssetQuote temp = marketClient.getQuote(this.assetSymbol);
    return temp.getRegularMarketPrice();
  }

  /**
   * This method contains the current asset value changed unto percentage.
   *
   * @param currentAssetPrice price of current asset value.
   * @return the value change percentage.
   */
  public double getValueChangePercent(double currentAssetPrice) {
    double difference = Math.max(currentAssetPrice,
            this.originalPurchasePrice) - Math.min(currentAssetPrice, this.originalPurchasePrice);
    double percentChange = (difference / originalPurchasePrice) * 100;
    if (Math.max(currentAssetPrice, this.originalPurchasePrice) == this.originalPurchasePrice) {
      percentChange = percentChange * -1;
    }
    return percentChange;
  }

  /**
   * This method coverts the asset value into US Dollar.
   *
   * @return asset value in United States Dollars amount.
   */
  public double getValueChangeUsd() {
    double currentAssetPrice = this.getCurrentAssetPrice();

    double difference = Math.max(currentAssetPrice,
            this.originalPurchasePrice) - Math.min(currentAssetPrice, this.originalPurchasePrice);

    if (Math.max(currentAssetPrice, this.originalPurchasePrice) == this.originalPurchasePrice) {
      difference = difference * -1;
    }
    return difference;
  }

  /**
   * This method converts to string value.
   *
   * @return string value of input data.
   */
  public String toString() {
    double currentAssetPrice = this.getCurrentAssetPrice();
    return this.assetSymbol + " - |Purchase Date: " + getOriginalPurchaseDateAsHumanReadable()
            + " - |UnitPrice@Purchase: (USD)" + this.originalPurchasePrice
            + "\n|CurrentUnitPrice: " + currentAssetPrice
            + " |Change: (USD) " + this.getValueChangeUsd()
            + " - |Change: (%) " + this.getValueChangePercent(currentAssetPrice)
            + "\n|Units Purchased: " + this.originalPurchaseUnits
            + " less sold: " + this.getSumOfUnitsSold()
            + " = Current Units Balance:" +  getCurrentUnitsBalance()
            + "\n";
  }
}