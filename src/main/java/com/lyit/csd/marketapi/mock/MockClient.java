package com.lyit.csd.marketapi.mock;

import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link MockClient} is used to Mock {@link MarketClient} for unit testing.
 */
public class MockClient implements MarketClient {

  /**
   * This function gives a quote of the asset.

   * @param assetSymbol indicates the symbol or icon that a specific asset is identified with.
   * @return returns either the asset value or null value if none.
   */
  public AssetQuote getQuote(String assetSymbol) {
    if (assetSymbol == null || assetSymbol.trim().length() == 0) {
      return null;
    }

    return new AssetQuote(assetSymbol,
        "Asset" + " " + assetSymbol,
        0,
        123.45,
        1.234,
        1.234,
            "Quote Type",
            1.234,
            1.234);
  }

  /**
   * This function gives a quote of the asset.

   * @param assetSymbols indicates the symbol or icon that a specific asset is identified with.
   * @return returns either the asset value or null value if none.
   */
  public List<AssetQuote> getQuote(List<String> assetSymbols) {
    List<AssetQuote> assetQuotes = new ArrayList<>();

    for (String assetSymbol : assetSymbols) {
      AssetQuote assetQuote = getQuote(assetSymbol);
      if (assetQuote != null) {
        assetQuotes.add(assetQuote);
      }
    }

    return assetQuotes;
  }

  /**
   * This function provides the stocks that are the most trending within a specific region.

   * @param region place or location that a user enters in the program.
   * @return Returns the most trending / in demand stocks for the specified region.
   */
  public ArrayList<String> getTrendingStocksForRegion(String region) {
    ArrayList<String> trendingStocks = new ArrayList<>();
    trendingStocks.add("TSLA");
    trendingStocks.add("AAPL");
    return trendingStocks;
  }


  /**
   * <code>getHistoricalInfo</code> returns historical data on Assets.

   * @param assetSymbols the symbol which a specific asset is identified by.
   * @param interval timely intervals during which asset values change, in order to
   *                 provide an accurate history during a specific timespan.
   * @param range the minimum and maximum values that a specific was worth
   *              during the selected time period.
   * @return Either the historical data of the Asset or null.
   */
  @Override
  public List<AssetQuote> getHistoricalInfo(List<String> assetSymbols,
                                            String interval, String range) {
    return null;
  }

  /**
   * <code>getExchangeInfo</code> returns the exchange information of the Asset.

   * @param region the place or location of asset exchange.
   * @param exchange value for which the asset has been traded
   * @return Information of which asset, where, when and for how much has been exchanged.
   */
  @Override
  public String getExchangeInfo(String region, String exchange) {
    AssetQuote assetQuote = new AssetQuote(
            "TSLA", "NYSE",
            0,
            123.45,
            1.234,
            1.234,
            "Quote Type",
            1.234,
            1.234);
    return assetQuote.exchangeToString();
  }

  /**
   * <code>checkAssetSymbol</code> returns the smbol by which a asset is identified.

   * @param assetSymbol shortcut or icon to address / indentify a specific asset.
   * @return Returns the asset name is abbreviated format.
   */
  @Override
  public boolean checkAssetSymbol(String assetSymbol) {
    return false;
  }
}
