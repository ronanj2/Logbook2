package com.lyit.csd.app;

import com.lyit.csd.domain.Asset;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.domain.DateTimeHelper;
import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.MarketClientFactory.MarketClientTypes;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The {@link Main} class provides the UI and entry point for the investment portfolio project.
 *
 * <p>init(), UI and data retrieval from the user is contained in the main class.
 */
public class Main {
  private static PortfolioSystem portfolioSystem;

  /**
   * The main method displays the command line interface of this application.

   * @param args Displays User interface as a command line.
   */
  public static void main(String[] args) {
    init();
    displayUi();
  }

  /**
   * This method displays market client information.
   */
  private static void init() {
    // We use and inject a MarketClient interface into PortfolioManager.
    // This will allow us to pass in a 'Mock' client for unit testing.
    // We are using a factory pattern to get the market client. This creational pattern creates
    // a MarketClient object without exposing the creation logic to this client ui. It also reduces
    // coupling in that we do not need to bind the UI or the PortfolioSystem to the YahooClient.
    MarketClientFactory marketClientFactory = new MarketClientFactory();
    MarketClient marketClient = marketClientFactory.getMarketClient(MarketClientTypes.Yahoo);
    portfolioSystem = new PortfolioManager(marketClient);
  }

  /**
   * This method displays the user interface of the program, whereby user
   * input is requested to make a selection.
   */
  public static void displayUi() {
    boolean exitCondition;

    do {
      int userSelection;
      printMenu();
      userSelection = getUserInput("Enter your selection", Integer::parseInt);
      exitCondition = processUserSelection(userSelection);
    } while (!exitCondition);
  }

  /**
   * This method displays the full menu of choices that can be selected by a user
   * that runs the program. The main PORTFOLIO MANAGEMENT SYSTEM options are shown.
   */
  private static void printMenu() {
    System.out.println("\t" + ConsoleColors.GREEN_UNDERLINED
            + "PORTFOLIO MANAGEMENT SYSTEM" + ConsoleColors.RESET);

    String menu = """
            Please make one selection from the given options:
            1. Add Funds
            2. Withdraw Funds
            3. Get Assets w/ Holdings
            4. Display Available Funds
            5. Purchase Asset
            6. Sell Asset
            7. Get Trending Stocks
            8. Get Historical Data
            9. Get Exchange Summary
            10. Get Asset Information
            11. Get Asset Quotes
            12. Get Asset Quote
            13. Get Portfolio Value
            14. List All Investments
            15. List Portfolio Assets by Type
            16. List Portfolio Assets by Name
            17. List Portfolio Purchases in Range
            18. List Portfolio Sales in Range
            19. Exit Menu""";
    System.out.println(ConsoleColors.YELLOW_BOLD + menu + ConsoleColors.RESET);
  }

  /**
   * This method shows the title menu of the application.

   * @param menuTitle displays the heading title of the menu.
   */
  private static void printMenuTitle(String menuTitle) {
    System.out.println(ConsoleColors.BLUE_UNDERLINED + menuTitle + ConsoleColors.RESET);
  }

  /**
   * This method prints an error message, in red color font to the console.

   * @param message The message to print to the console.
   */
  private static void printError(String message) {
    System.out.println(ConsoleColors.RED_BOLD + message
            + ConsoleColors.RESET);
  }

  /**
   * This method requests the user to enter data that will be processed by the program.

   * @param prompt select asset type.

   * @param function what needs to be done.

   * @param <T> object for the input provided by the user
   * @return programs outcome.
   */
  private static <T> T getUserInput(String prompt, Function<String, T> function) {
    System.out.println(ConsoleColors.PURPLE_BOLD + prompt + ConsoleColors.RESET);
    return ScannerInput.getParsed(prompt, function);
  }

  /**
   * This method request the user to type specific details.

   * @param prompt enter asset value.
   * @return The users input will be returned.
   */
  private static String getStringInput(String prompt) {
    System.out.println(ConsoleColors.PURPLE_BOLD + prompt + ConsoleColors.RESET);
    return ScannerInput.getLine();
  }

  /**
   * This method processes the user input based on selection made,
   * there are eighteen options to be chosen from.

   * @param userSelection Any of the 18 options as indicated below
   * @return Logging of.
   */
  private static Boolean processUserSelection(int userSelection) {
    switch (userSelection) {
      case 1 -> processAddFunds();
      case 2 -> processWithdrawFunds();
      case 3 -> processViewAssetsWithHoldings();
      case 4 -> processViewAvailableFunds();
      case 5 -> processPurchaseAssets();
      case 6 -> processSellAssets();
      case 7 -> processViewTrendingStocksByRegion();
      case 8 -> processViewHistoricalData();
      case 9 -> processViewExchangeSummary();
      case 10 -> processViewAssetInformation();
      case 11, 12 -> processViewAssetQuotes();
      case 13 -> processViewPortfolioValue();
      case 14 -> processListAllInvestments();
      case 15 -> processListPortfolioAssetsByType();
      case 16 -> processListPortfolioAssetsByName();
      case 17 -> processListPortfolioPurchasesInRange();
      case 18 -> processListPortfolioSalesInRange();
      case 19 -> {
        System.out.println("Logging off...");
        return true;
      }
      default -> printError("Invalid Selection. Please try again:");
    }
    return false;
  }

  /**
  *<code>processListPortfolioSalesInRange</code> returns portfolio sales within
  *a specific range as defined by the user.
  */
  private static void processListPortfolioSalesInRange() {
    printMenuTitle("***VIEW PORTFOLIO SALES IN RANGE***");

    LocalDate startDate = getUserInput("Enter start date in format "
            + "'yyyy-MM-dd':", LocalDate::parse);
    long startTimeStamp = DateTimeHelper.convertToEpochMilliseconds(startDate);

    LocalDate endDate = getUserInput("Enter end date in format "
            + "'yyyy-MM-dd':", LocalDate::parse);
    long endTimeStamp = DateTimeHelper.convertToEpochMilliseconds(endDate) + (86400000 - 1);

    String result = portfolioSystem.listPortfolioSalesInRange(startTimeStamp, endTimeStamp);
    if (result.isEmpty()) {
      System.out.println("No records to show.");
    } else {
      System.out.println(result);
    }
    endProcess();
  }

  /**
   * <code>processListPortfolioPurchasesInRange</code> returns portfolio
   * purchases within a specific range as defined by the user.
   */
  private static void processListPortfolioPurchasesInRange() {
    printMenuTitle("***VIEW PORTFOLIO PURCHASES IN RANGE***");

    LocalDate startDate = getUserInput("Enter start date in format "
            + "'yyyy-MM-dd':", LocalDate::parse);
    long startTimeStamp = DateTimeHelper.convertToEpochMilliseconds(startDate);

    LocalDate endDate = getUserInput("Enter end date in format "
            + "'yyyy-MM-dd':", LocalDate::parse);
    long endTimeStamp = DateTimeHelper.convertToEpochMilliseconds(endDate) + (86400000 - 1);

    String result = portfolioSystem.listPortfolioPurchasesInRange(startTimeStamp, endTimeStamp);
    if (result.isEmpty()) {
      System.out.println("No records to show.");
    } else {
      System.out.println(result);
    }
    endProcess();
  }

  /**
   * <code>processListPortfolioAssetsByName</code> returns portfolio asset sorted by name.
   */
  private static void processListPortfolioAssetsByName() {
    printMenuTitle("***VIEW ASSETS BY NAME***");
    List<String> assetSymbols = new ArrayList<>();
    while (true) {
      String input = getStringInput("Enter symbols of the assets you wish to retrieve data "
          + "for, one at a time:\nEnter 'N' to stop input...");
      if (input.equalsIgnoreCase("N")) {
        break;
      } else {
        assetSymbols.add(input);
      }
    }
    String result = portfolioSystem.listPortfolioAssetsByName(assetSymbols);
    System.out.println(result);
    endProcess();
  }

  /**
   * <code>processListPortfolioAssetsByType</code> returns portfolio asset sorted by type.
   */
  private static void processListPortfolioAssetsByType() {
    printMenuTitle("***VIEW ASSETS BY TYPE***");
    String assetType = getStringInput("Enter asset type:");
    String result = portfolioSystem.listPortfolioAssetsByType(assetType);
    System.out.println(result);
    endProcess();
  }

  /**
   * <code>processListAllInvestments</code> returns all investments made.
   */
  private static void processListAllInvestments() {
    printMenuTitle("***VIEW CURRENT INVESTMENTS");
    String result = portfolioSystem.listAllInvestments();
    System.out.println(result);
    endProcess();
  }

  /**
   * <code>processViewPortfolioValue</code> returns the full portfolio value.
   */
  private static void processViewPortfolioValue() {
    printMenuTitle("***VIEW PORTFOLIO VALUE***");
    double portfolioValue = portfolioSystem.getPortfolioValue();
    System.out.println(portfolioValue);
    endProcess();
  }

  /**
   * This method displays asset quotes.
   */
  private static void processViewAssetQuotes() {
    printMenuTitle("***VIEW ASSET QUOTES***");
    List<String> inputList = new ArrayList<>();
    while (true) {
      String input = getStringInput("Enter symbols of the assets you wish to "
          + "retrieve historical data one at a time:\nEnter 'N' to stop input...");
      if (input.equalsIgnoreCase("N")) {
        break;
      } else {
        inputList.add(input);
      }
    }
    List<AssetQuote> outputList = portfolioSystem.getAssetQuotes(inputList);
    for (AssetQuote assetQuote : outputList) {
      System.out.println(assetQuote.toString());
    }
    endProcess();
  }

  /**
   * This method displays asset information that the user desires to see.
   */
  private static void processViewAssetInformation() {
    printMenuTitle("***VIEW ASSET INFORMATION***");
    List<String> inputList = new ArrayList<>();
    List<AssetQuote> outputList;

    while (true) {
      String input = getStringInput("Enter symbols of the assets you wish to "
          + "retrieve data for, one at a time:\nEnter 'N' to stop input...");
      if (input.equalsIgnoreCase("N")) {
        break;
      } else {
        inputList.add(input);
      }
    }

    outputList = portfolioSystem.getAssetInformation(inputList);
    for (AssetQuote assetQuote : outputList) {
      System.out.println(assetQuote.toString());
    }
    endProcess();
  }

  /**
   * This method shows the exchange summary, based on region and asset.
   */
  private static void processViewExchangeSummary() {
    printMenuTitle("***VIEW EXCHANGE SUMMARY***");
    String region = getStringInput("Enter region");
    String symbol = getStringInput("Enter exchange symbol:");
    System.out.println(portfolioSystem.getExchangeSummary(region, symbol));
    endProcess();
  }

  /**
   * This method provides historical data, which can help with the purchase decision making process.
   */
  private static void processViewHistoricalData() {
    MarketClient client =
        new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    printMenuTitle("***VIEW HISTORICAL DATA***");
    List<String> inputList = new ArrayList<>();
    while (true) {
      String input = getStringInput("Enter symbols of the assets you wish to retrieve "
          + "historical data one at a time:\nEnter 'N' to stop input...");
      if (input.equalsIgnoreCase("N")) {
        break;
      } else {
        if (client.checkAssetSymbol(input)) {
          inputList.add(input);
        } else {
          printError("Invalid asset symbol...");
        }
      }
    }

    String interval = getStringInput("Enter data interval: (1m, 5m, 15m, 1d, 1wk, 1mo)");
    String range = getStringInput("Enter the range of the data you require: (1d, 5d, 1mo, "
        + "3mo, 6mo, 1y, 5y, max)");
    List<AssetQuote> historicalData = portfolioSystem.getHistoricalData(inputList, interval, range);
    for (AssetQuote assetQuote : historicalData) {
      assetQuote.historicDataToString();
    }
    endProcess();
  }

  /**
   * This method processes the purchased assets.
   */
  private static void processPurchaseAssets() {
    printMenuTitle("***PURCHASE ASSET(s)***");
    String input = getStringInput("Please enter symbol of asset you wish to purchase:");
    double amount = getUserInput("Please enter amount you wish to purchase:", Double::parseDouble);
    if (portfolioSystem.purchaseAsset(input, amount)) {
      System.out.println("You have purchased " + amount + " of " + input);
    } else {
      printError("Invalid asset symbol...");
    }
    endProcess();
  }

  /**
   * This method performs the processing of sold assets.
   */
  private static void processSellAssets() {
    printMenuTitle("***SELL ASSET(s)***");
    String input = getStringInput("Please enter symbol of asset you wish to sell:");
    double amount = getUserInput("Please enter amount you wish to sell:", Double::parseDouble);
    boolean success = portfolioSystem.sellAsset(input, amount);

    if (success) {
      System.out.println(amount + " of " + input + " sold.");
    } else {
      printError("Unable to process asset sale. Please check inputs and try again.");
    }
    endProcess();
  }

  /**
   * This method adds funds to the portofolio.
   */
  private static void processAddFunds() {
    printMenuTitle("***ADD FUNDS***");
    double amount = getUserInput("Enter deposit amount:", Double::parseDouble);
    portfolioSystem.addFunds(amount);
    System.out.println("Funds added.");
    System.out.println("Current account balance: " + portfolioSystem.getAvailableFunds());
    endProcess();
  }

  /**
   * This method withdraws funds from the portofolio.
   */
  private static void processWithdrawFunds() {
    printMenuTitle("***WITHDRAW FUNDS***");
    double amount = getUserInput("Enter withdrawal amount:", Double::parseDouble);

    boolean success = portfolioSystem.withdrawFunds(amount);
    if (success) {
      System.out.println("Funds successfully withdrawn.");
      System.out.println("Current account balance: " + portfolioSystem.getAvailableFunds());
    } else {
      printError("Unable to withdraw funds. Insufficient funds.");
    }
    endProcess();
  }

  /**
   * This method views all assets that contain withholding.
   */
  private static void processViewAssetsWithHoldings() {
    printMenuTitle("***VIEW ASSETS WITH HOLDINGS***");
    String inputSymbol = getStringInput("Please enter stock symbol:");
    List<Asset> tempList = portfolioSystem.getAssetsWithHoldings(inputSymbol.toUpperCase());
    for (Asset asset : tempList) {
      System.out.println(asset.toString());
    }
    System.out.println();
    endProcess();
  }

  /**
   * This method shows the available funds that can be used to invest in stocks.
   */
  private static void processViewAvailableFunds() {
    printMenuTitle("***VIEW AVAILABLE FUNDS***");
    System.out.println(portfolioSystem.getAvailableFunds());
    endProcess();
  }

  /**
   * This method ends the current process and gives the user
   * the possibility to return to the main menu.
   */
  private static void endProcess() {
    System.out.print("Press any key to return to the menu...");
    ScannerInput.getLine();
  }

  /**
   * This method shows all trending stocks sorted by region.
   * User enters a specific region, based on which the trending stocks are displayed.
   */
  private static void processViewTrendingStocksByRegion() {
    printMenuTitle("***VIEW TRENDING STOCKS***");
    String input = getStringInput("Enter region:");
    List<String> trendingStocks = portfolioSystem.getTrendingStocks(input);
    for (String assetSymbol : trendingStocks) {
      System.out.println(assetSymbol);
    }
    endProcess();
  }
}