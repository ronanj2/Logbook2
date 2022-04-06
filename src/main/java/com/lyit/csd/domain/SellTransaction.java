package com.lyit.csd.domain;

/**
 * Class added to hold all sale information from asset objects.
 */
public class SellTransaction {
  private final double units;
  private final double sellPrice;
  private final long sellDate;

  /**
   * Method to create sell transaction.
   *
   * @param units the amount of units sold.
   * @param sellPrice the price at time of sale.
   */
  public SellTransaction(double units, double sellPrice) {
    this.units = units;
    this.sellPrice = sellPrice;
    this.sellDate = DateTimeHelper.getCurrentEpochTime();
  }

  /**
   * Method to returns the number of units involved in transaction.
   *
   * @return gets the units value from the object. Returns a double.
   */
  public double getUnits() {
    return units;
  }

  /**
   * Method to retrieve the date of the sell transaction.
   *
   * @return gets the date of the sale. Returns a long.
   */
  public long getSellDate() {
    return sellDate;
  }

  /** This method contains the Purchase date of when asset was bought.
   *
   * @return date and time of asset purchase.
   */
  public String getSellDateAsHumanReadable() {
    return DateTimeHelper.getEpochAsHumanReadable(this.getSellDate());
  }

  /**
   * Method to retrieve the price at which the transaction occurred.
   *
   * @return Gets the price received at the time of sale. Returns a double.
   */
  public double getSellPrice() {
    return sellPrice;
  }
}
