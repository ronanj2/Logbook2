package com.lyit.csd.domain;

import com.lyit.csd.marketapi.abstraction.MarketClient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <code>PortfolioManager</code> adds the functionality outlined in the
 * {@link PortfolioSystem} interface The user can add funds, buy and sell stock, get stock
 * information, and list and sort assets.
 */
public class PortfolioManager implements PortfolioSystem {
  private double availableFunds;
  private final List<Asset> holdings;
  private final MarketClient marketClient;
  private List<String> knownGoodAssetSymbols;

  /**
   * Constructor for the {@link PortfolioManager} class.

   * @param marketClient is injected to ensure no coupling between our business layer and
   *                     any {@link MarketClient}. Being able to inject any {@link MarketClient}
   *                     at runtime means that we can easily switch in or out new providers
   *                     in future. It also makes this class easy to unit test as we can inject
   *                     a Mock Client.
   */
  public PortfolioManager(MarketClient marketClient) {
    this(marketClient, true);
  }

  /**
  * <code>PortfolioManager</code> Constructor.

  * @param marketClient -the client object to be used is passed in (Yahoo Finance API is
  *                     the only 'real' implemented market client currently)

  * @param loadPortfolio - the pre-made portfolio as outlined in the project
  *                      specifications is passed into the constructor
  */
  public PortfolioManager(MarketClient marketClient, Boolean loadPortfolio) {
    this.marketClient = marketClient;
    this.holdings = new ArrayList<>();
    initKnownGoodAssetSymbols();

    if (loadPortfolio) {
      this.loadClientPortfolio();
    }
  }

  /**
   * In this program, we call checkAssetSymbol() to validate that asset
   * symbols exist/are valid. However, calling this regularly means that
   * we often get rate limited by the exchange we're querying. For that
   * reason, we have created this method to initialize the program with
   * 'known' good asset symbols. We also add to this array in method
   * checkAssetSymbol (we store all successful lookups to further
   * reduce the chances of being rate limited).
   */
  private void initKnownGoodAssetSymbols() {
    this.knownGoodAssetSymbols = new ArrayList<>();
    this.knownGoodAssetSymbols.add("TSLA");
    this.knownGoodAssetSymbols.add("AAPL");
    this.knownGoodAssetSymbols.add("GME");
    this.knownGoodAssetSymbols.add("NVDA");
    this.knownGoodAssetSymbols.add("BTC-USD");
    this.knownGoodAssetSymbols.add("MSFT");
  }

  /**
   * Add the specified amount in USD to the total cash funds available within the portfolio system.
   *
   * @param amount - amount the amount of money in USD to add to the system.
   */
  @Override
  public void addFunds(double amount) {

    if (amount > 0) {
      availableFunds += amount;
    }
  }



  /**
   * Comparator used to sort the Assets by purchase price.
   */
  Comparator<Asset> compareByPurchasePrice = (o1, o2) -> {
    double delta = o1.getOriginalPurchasePrice() - o2.getOriginalPurchasePrice();
    if (delta > 0) {
      return 1;
    }
    if (delta < 0) {
      return -1;
    }
    return 0;
  };

  public double getAvailableFunds() {
    return this.availableFunds;
  }

  /**
   * Withdraw the specified amount in USD from the total cash funds available within the portfolio
   * management system.
   *
   * @param amount the amount of money in USD to withdraw from the system.
   * @return True if we have successfully withdrawn the funds (sufficient funds are available)
   *        otherwise false.
    */
  @Override
  public boolean withdrawFunds(double amount) {
    if (amount <= availableFunds) {
      availableFunds -= amount;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Record a purchase of the named asset if available funds >= the total value of the assets (stock
   * or cryptocurrency) being purchased. The price paid should be the real live price of the asset.
   *
   * @param assetSymbol the name of the asset (stock symbol or cryptocurrency) to purchase.
   * @param amount    the amount of the asset to purchase.
   * @return True if the asset is purchased successfully, otherwise False.
   */
  @Override
  public boolean purchaseAsset(String assetSymbol, double amount) {
    AssetQuote livePrice = getAssetQuote(assetSymbol);

    if (livePrice == null) {
      return false;
    }

    if (amount <= 0) {
      return false;
    }

    double priceItWouldCost = livePrice.getRegularMarketPrice() * amount;

    if (availableFunds < priceItWouldCost) {
      return false;
    }

    // assuming now they can afford it
    Asset newAsset = new Asset(assetSymbol, DateTimeHelper.getCurrentEpochTime(), amount,
                               livePrice.getRegularMarketPrice(), marketClient);
    holdings.add(newAsset);

    availableFunds -= priceItWouldCost;
    return true;
  }

  /**
   * Record a sale of the named asset (stock or cryptocurrency) at the current live market value if
   * we hold that asset. The sale price should be the real live price of the asset at the time of
   * sale retrieved from an appropriate web API. The revenue generated from the sale should be added
   * to the total funds available to the user.
   *
   * <p>Business logic: If we hold > 1 units of the specified asset (say 10 units of Microsoft stock
   * MSFT), and the parameter amount is less than total units of the stock, we should sell the
   * units that maximise our profit. Remember some of the stock could have been purchased on
   * different dates and therefore have been purchased at different price points.
   *
   * @param assetSymbol the name of the asset (stock symbol or cryptocurrency) to sell.
   * @param amount    the amount of the asset to sell.
   * @return True if the asset is sold successfully, otherwise false (we may not have that asset in
   *       our portfolio).
   */
  @Override
  public boolean sellAsset(String assetSymbol, double amount) {
    double amountLeftToSell = amount;

    //Checks if asset symbol is valid
    if (!checkAssetSymbol(assetSymbol)) {
      return false;
    }

    // 1. Get all the holdings with this assetSymbol.
    List<Asset> assetsHoldings = getAssetsWithHoldings(assetSymbol);
    if (assetsHoldings == null) {
      return false;
    }

    // 2. Check if there is sufficient balance across all of them.
    double currentUnitsBalance = assetsHoldings.stream()
        .mapToDouble(Asset::getCurrentUnitsBalance).sum();

    if (currentUnitsBalance < amountLeftToSell) {
      // we don't own enough of the asset to sell.
      return false;
    }

    // 3. Get the live price for this asset
    AssetQuote assetQuote = getAssetQuote(assetSymbol);
    //If no live price is available, we cannot sell the asset.
    if (assetQuote == null) {
      return false;
    }

    // 3. Work out which holdings will yield the best profit.
    // There are a few ways to do this. One such way is to sort the list by the purchase price.
    assetsHoldings.sort(compareByPurchasePrice);

    // 4. We will now iterate through the holdings to sell, up to the amount required.
    for (Asset asset : assetsHoldings) {
      // How much of this asset do we need to sell
      double assetUnitsBalance = asset.getCurrentUnitsBalance();

      if (assetUnitsBalance >= amountLeftToSell) {
        // This holding has enough units to fulfill the sell.
        asset.sellHolding(amountLeftToSell, assetQuote.getRegularMarketPrice());
        amountLeftToSell = 0;
      } else {
        asset.sellHolding(assetUnitsBalance, assetQuote.getRegularMarketPrice());
        amountLeftToSell -= assetUnitsBalance;
      }

      if (amountLeftToSell == 0) {
        // all sold.
        break;
      }
    }

    // 5. The revenue generated from the sale should be added to the total funds
    // available to the user. We know how many units we have sold, and we know the live price.
    double revenueGenerated = amount * assetQuote.getRegularMarketPrice();
    availableFunds += revenueGenerated;
    return true;
  }

  /**
   * Returns a list of trending stocks symbols, their current market price and the days gain or loss
   * in price and as a percentage. Yahoo finance provides this information for you.
   *
   * @param region a string country code specifying the region of interest. Examples include US, GB,
   *         FR, DE, HK.
   * @return a list of strings each representing trending stock symbols e.g. APPL, TSLA, BARC.
   */
  @Override
  public List<String> getTrendingStocks(String region) {
    List<String> trendingAssetsInfo = new ArrayList<>();
    try {
      trendingAssetsInfo = marketClient.getTrendingStocksForRegion(region);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return trendingAssetsInfo;
  }

  /**
   * Returns summary information on an exchange in the region specified.
   *
   * @param region   a string country code specifying the region of interest. Examples include US,
   *         GB, FR, DE, HK.
   * @param exchange a string specifying the exchange we want information on. Examples include FTSE,
   *         DOW, DASDAQ, DAX.
   * @return a String containing exchange summary information. Data includes at a minimum the
   *        exchange name, exchange symbol, previous closing value, opening value,
   *        gain/loss since opening. Add any additional data you feel is relevant.
   */
  @Override
  public String getExchangeSummary(String region, String exchange) {

    String exchangeSummary = null;
    try {
      exchangeSummary = marketClient.getExchangeInfo(region, exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return exchangeSummary;
  }

  /**
   * Retrieve a set of historic data points for the specified assets.
   *
   * @param assetSymbols a list of strings representing the symbols of the assets for which we need
   *           to obtain Historic data.
   * @param interval   a String representing the time interval between quotes. Valid values
   *           include 1m 5m 15m 1d 1wk 1mo.
   * @param range    a String representing the time range over which we should obtain historic
   *           data for the specified assets. Valid values include 1d 5d, 1mo, 3mo, 6mo,
   *           1y, 5y, max. Where max represents the maximum available duration (lifetime
   *           of the asset).
   * @return A list of assetQuotes objects.
   */
  @Override
  public List<AssetQuote> getHistoricalData(List<String> assetSymbols,
                                            String interval, String range) {
    return marketClient.getHistoricalInfo(assetSymbols, interval, range);
  }

  /**
  * Retrieve realtime quote data for the assets within the list assetNames from the online
  * exchange.
  *
  * @param assetNames a list of asset symbols for example, "Bitcoin-USD", "Appl", "TSLA".
  * @return A list of AssetQuote objects. Return an empty list if we have no assets in ourá
  *       portfolio.
  */
  @Override
    public List<AssetQuote> getAssetInformation(List<String> assetNames) {
    for (int i = 0; i < assetNames.size(); i++) {
      String assetSymbol = assetNames.get(i);
      if (!checkAssetSymbol(assetSymbol)) {
        assetNames.remove(i);
        i--; //decrement to account for the left shift when an element is removed
        System.out.println("Skipping invalid symbol..." + assetSymbol);
      }
    }

    //exit the method if no valid symbols were passed in
    if (assetNames.size() == 0) {
      return new ArrayList<>();
    }

    List<Asset> assetsWithHoldings = getAssetsWithHoldings();
    List<String> assetSymbolsHeld = getDistinctAssetSymbols(assetsWithHoldings);

    if (assetSymbolsHeld.size() == 0) {
      return new ArrayList<>();
    }

    List<String> assetSymbolsToLookup = assetNames.stream()
        .distinct()
        .filter(assetSymbolsHeld::contains)
        .collect(Collectors.toList());

    if (assetSymbolsToLookup.size() == 0) {
      return new ArrayList<>();
    }

    return marketClient.getQuote(assetSymbolsToLookup);
  }

  /**
   * Retrieve realtime quote data for the assets within the list assetNames from the online
   * exchange.
   *
   * @param assetSymbols a list of asset symbols for example, "Bitcoin-USD", "Appl", "TSLA"
   * @return A list of AssetQuote objects.
   */
  @Override
    public List<AssetQuote> getAssetQuotes(List<String> assetSymbols) {
    return marketClient.getQuote(assetSymbols);
  }

  /**
   * Retrieve realtime quote data from the online exchange.
   *
   * @param assetSymbol an asset symbol for example, "Bitcoin-USD", "Appl", "TSLA".
   * @return An AssetQuote object.
   */
  @Override
  public AssetQuote getAssetQuote(String assetSymbol) {
    return marketClient.getQuote(assetSymbol);
  }

  /** Gets the assets which still have holdings.

   * @return an array of the assets which have units owned.
   */
  private List<Asset> getAssetsWithHoldings() {
    return getAssetsWithHoldings("");
  }

  /** Gets the assets which still have holdings.

   * @param assetSymbol the assetSymbol to search for holdings of.
   * @return an array of the assets which have units owned.
   */
  @Override
  public List<Asset> getAssetsWithHoldings(String assetSymbol) {
    Predicate<Asset> withHoldings = asset -> asset.getCurrentUnitsBalance() > 0;

    if (!assetSymbol.isEmpty()) {
      withHoldings = withHoldings.and(asset -> asset.getAssetSymbol().equals(assetSymbol));
    }

    return holdings.stream().filter(withHoldings).collect(Collectors.toList());
  }

  /** Gets the distinct assetSymbols from an array of assets.
   *
   * @param assets a list of assets.
   * @return an array of distinct assetSymbols.
   */
  private List<String> getDistinctAssetSymbols(List<Asset> assets) {
    return assets.stream()
      .map(Asset::getAssetSymbol).distinct()
      .collect(Collectors.toList());
  }

  /**
   * Retrieve the current value of all of the assets in the portfolio based on the current live
   * value of each asset.
   *
   * @return a double representing the value of the portfolio in USD.
   */
  @Override
  public double getPortfolioValue() {
    // First, we will get a list of the holdings which still have owned units.
    List<Asset> assetsWithHoldings = getAssetsWithHoldings();

    // Now get a distinct list of the assetSymbols so that we can look up the live prices with
    // a single request. This greatly increases the speed of this method versus looking them up
    // one by one.
    List<String> assetSymbols = getDistinctAssetSymbols(assetsWithHoldings);

    // Retrieve the current live price for each of our owned assetSymbols.
    List<AssetQuote> assetQuotes = this.getAssetQuotes(assetSymbols);

    double portfolioValue = 0;

    // We now iterate through the assets we have holdings in and multiply the remaining units
    // by the live price.
    for (Asset asset : assetsWithHoldings) {
      AssetQuote assetQuote = assetQuotes.stream()
          .filter(a -> a.getAssetSymbol().equals(asset.getAssetSymbol()))
          .findFirst().orElse(null);

      portfolioValue += asset.getCurrentValue(assetQuote);
    }

    return portfolioValue;
  }


  /** This method checks that an <code>assetSymbol</code> is valid. In order to speed up checking
   * and to reduce the number of calls we make to the {@link MarketClient}, we store/cache known
   * good asset symbols after lookup.
   *
   * @param assetSymbol The assetSymbol to check is valid.
   * @return a boolean indicating whether or not the provided assetSymbol is valid.
   */
  public boolean checkAssetSymbol(String assetSymbol) {
    boolean containsSearchStr = knownGoodAssetSymbols.stream()
        .anyMatch(assetSymbol::equalsIgnoreCase);

    if (containsSearchStr) {
      return true;
    }

    AssetQuote assetQuote = getAssetQuote(assetSymbol);
    if (assetQuote != null) {
      knownGoodAssetSymbols.add(assetSymbol);
    }

    return assetQuote != null;
  }

  /**
   * Returns a formatted string detailing the name, symbol, average purchase price, current value
   * and amount of each asset within the portfolio. The difference in average purchase price and
   * current price should also be displayed in both USD and as a percentage.
   *
   * @return a String containing summary information on the assets in the portfolio.
   */
  @Override
  public String listAllInvestments() {
    StringBuilder result = new StringBuilder("PERSONAL ASSETS: \n");
    for (Asset asset : holdings) {
      double averagePrice = getAverageInvestmentPrice(asset.getAssetSymbol());
      result.append(asset).append(" - AverageUnitPrice: ").append(averagePrice).append("\n");
    }
    return result.toString();
  }

  /**
   * This method establishes the average price paid for all units of a specific asset,
   * including those bought at different dates/prices.
   *
   * @param assetSymbol is the asset the user wishes to calculate the average investment price of.
   * @return double - average price.
   */
  public double getAverageInvestmentPrice(String assetSymbol) {
    double counter = 0;
    double price = 0;
    for (Asset holding : holdings) {
      if (holding.getAssetSymbol().equals(assetSymbol)) {
        counter += holding.getOriginalPurchaseQty();
        price += holding.getOriginalPurchasePrice() * holding.getOriginalPurchaseQty();
      }
    }
    return price / counter;
  }

  /**
   * Retrieve a formatted string containing all of the assets within the portfolio of the specified
   * asset type ("stock" or "cryptocurrencies"). String contains the name, symbol, average purchase
   * price, current value and amount of each asset within the portfolio. The difference in average
   * purchase price and current price are displayed in USD and as a percentage.
   *
   * @param assetType a string specifying the asset type. Valid values are "stock" or "crypto"
   * @return a formatted String containing summary of all of the investments within the portfolio.
   *        Return an empty string if we have no assets within our portfolio.
   */
  @Override
  public String listPortfolioAssetsByType(String assetType) {
    //local variable for return value
    StringBuilder returnString = new StringBuilder();

    //assetType changed from valid user inputs to quoteType returned by the API
    if (assetType.equalsIgnoreCase("stock")) {
      assetType = "EQUITY";
    } else if (assetType.equalsIgnoreCase("crypto")) {
      assetType = "CRYPTOCURRENCY";
    } else {
      return "Invalid Input, enter 'stock' or 'crypto'";
    }

    //use the symbols to get an assetQuote list (assetQuote class is where the quoteType is held)
    List<AssetQuote> typeList = getAssetQuotes(getDistinctAssetSymbols(holdings));
    //An Asset list is used to get the current units
    List<Asset> assetList = getAssetsWithHoldings();

    //the returnString is concatenated within the for loop
    //if the assetType passed in matches the quoteType from the API
    for (int i = 0; i < typeList.size(); i++) {
      if (typeList.get(i).getQuoteType().equals(assetType)) {
        returnString.append("Name: ").append(typeList.get(i).getFullExchangeName())
          .append("\t Symbol: ").append(typeList.get(i))
          .append("\t\t Average Investment Price: ")
          .append(getAverageInvestmentPrice(typeList.get(i).getAssetSymbol()))
          .append("\t\tCurrent Units: ").append(assetList.get(i).getCurrentUnitsBalance())
          .append("\n");
      }
    }
    return returnString.toString();
  }

  /**
   * Retrieve a formatted String containing details on all of the assets within the portfolio
   * matching the assetName in full or partially. String contains the name, symbol, average purchase
   * price, current value and amount of each asset within the portfolio. The difference in average
   * purchase price and current price are displayed in USD and as a percentage.
   *
   * @param assetNames a list of Strings containing asset symbols such as "MSFT" or "BTC-USD" or
   *           full name "Bitcoin USD" or partial string "Bitco".
   * @return A formatted String containing summary information for the assetNames provided in the
   *        list. Return an empty string if we have no matching assets.
   */
  @Override
  public String listPortfolioAssetsByName(List<String> assetNames) {
    StringBuilder returnString = new StringBuilder();

    // The asset symbols of the holdings are used to get the AssetQuotes
    List<AssetQuote> assetList = getAssetQuotes(getDistinctAssetSymbols(holdings));
    List<Asset> unitsList = getAssetsWithHoldings();

    // Nested for loop checks each string against each AssetQuote
    for (String string : assetNames) {
      for (AssetQuote quote : assetList) {

        if (string.toLowerCase().contains(quote.getFullExchangeName().toLowerCase())
            || string.toLowerCase().contains(quote.getAssetSymbol().toLowerCase())) {
          returnString.append(" Name: \t").append(quote.getFullExchangeName())
            .append("\tSymbol: ").append(quote).append("\n")
            .append("\t  Average price ").append(getAverageInvestmentPrice(string))
            .append("\t\tCurrent Units: ").append(unitsList.get(assetList.indexOf(quote))
            .getCurrentUnitsBalance()).append("\n");
        }
      }
    }

    return returnString.toString();
  }

  /**
   * Retrieve a formatted String containing summary information for all assets within the portfolio
   * purchased between the dates startTimeStamp and endTimeStamp. Summary information contains the
   * purchase price, current price, difference between the purchase and sale price (in USD and as a
   * percentage).
   *
   * <p>If the several units of the asset have been purchased at different time points between the
   * startTimeStamp and endTimeStamp, list each asset purchase separately by date (oldest to most
   * recent).
   *
   * @param startTimeStamp a UNIX timestamp representing the start range date.
   * @param endTimeStamp   a UNIX timestamp representing the end range date.
   * @return A formatted String containing summary information for all of the assets purchased
   *        between the startTimeStamp and endTimeStamp. Return an empty string if we have
   *        no matching assets in our portfolio.
   */
  @Override
  public String listPortfolioPurchasesInRange(long startTimeStamp, long endTimeStamp) {
    StringBuilder returnThisString = new StringBuilder();
    if (holdings != null) {
      for (Asset asset : holdings) {
        if (DateTimeHelper.isEpochInDateRange(asset.getOriginalPurchaseDateTime(),
                startTimeStamp, endTimeStamp)) {
          returnThisString.append(asset.getAssetSymbol()).append(" purchased ")
            .append(asset.getOriginalPurchaseQty()).append(" on ")
            .append(asset.getOriginalPurchaseDateAsHumanReadable()).append(" at $")
            .append(asset.getOriginalPurchasePrice()).append(" each \n");
        }
      }
    }
    return returnThisString.toString();
  }


  /**
   * Retrieve a formatted string containing a summary of all the assets sales between the dates
   * startTimeStamp and endTimeStamp. Summary information contains the average purchase price for
   * each asset, the sale price and the profit or loss (in USD and as a percentage).
   *
   * <p>If the several units of the asset have been sold at different time points between the
   * startTimeStamp and endTimeStamp, list by date (oldest to most recent) each of those individual
   * sales.
   *
   * @param startTimeStamp a UNIX timestamp representing the start range date.
   * @param endTimeStamp   a UNIX timestamp representing the end range date.
   * @return A formatted String containing summary information for all the assets sold between
   *        the startTimeStamp and endTimeStamp. Return an empty string if we have no
   *        matching assets in our portfolio.
   */
  @Override
  public String listPortfolioSalesInRange(long startTimeStamp, long endTimeStamp) {
    StringBuilder returnThisString = new StringBuilder(); //return empty string if no match
    if (holdings != null) {
      for (Asset asset : holdings) {
        List<SellTransaction> sellTransactions = asset.getSellTransactions(); //gets the sales
        if (sellTransactions != null) {
          for (SellTransaction sellTransaction : sellTransactions) {
            if (DateTimeHelper.isEpochInDateRange(sellTransaction.getSellDate(),
                    startTimeStamp, endTimeStamp)) {
              //if sales were within ranges
              returnThisString.append(asset.getAssetSymbol()).append(" sold ")
                  .append(sellTransaction.getUnits()).append(" on ")
                  .append(sellTransaction.getSellDateAsHumanReadable()).append(" at $")
                  .append(sellTransaction.getSellPrice());
            }
          }
        }
      }
    }
    return returnThisString.toString();
  }

  /**
   * This method loads the portfolio client existing of all owned assets,
   * displaying the assetname, purchasedate, purchase units (quantity and purchase price).
   */
  private void loadClientPortfolio() {
    // Set up the assets already owned. These are specified in the assignment.
    holdings.add(new Asset(
        "TSLA", DateTimeHelper.getEpochMillisecondsForDate(2021, 10, 1),
        10, 775.22, marketClient));

    holdings.add(new Asset(
        "AAPL", DateTimeHelper.getEpochMillisecondsForDate(2021, 7, 5),
        20, 139.96, marketClient));

    holdings.add(new Asset(
        "NVDA", DateTimeHelper.getEpochMillisecondsForDate(2021, 4, 14),
        12, 152.77, marketClient));

    holdings.add(new Asset(
        "BTC-USD", DateTimeHelper.getEpochMillisecondsForDate(2021, 2, 9),
        0.0445881, 44854.95, marketClient));
  }
}
