package com.lyit.csd.marketapi.abstraction;

import com.lyit.csd.domain.AssetQuote;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface displays the asset quote and symbol, including which stock are
 * trending segmented by region. Asset history, including exchange information
 * the current and past values.
 */
public interface MarketClient {
  /** Get an {@link AssetQuote} from an exchange.

   * @param assetSymbol is the asset symbol to get a quote for.

   * @return an {@link AssetQuote}.
   */
  AssetQuote getQuote(String assetSymbol);

  /** Gets a list of {@link AssetQuote} from an exchange.

   * @param assetSymbols is a list of asset symbols to get quotes for.

   * @return a list of {@link AssetQuote}.
   */
  List<AssetQuote> getQuote(List<String> assetSymbols);

  /** Gets a list of trending stocks for a region from an exchange.

   * @param region is the region to look up.

   * @return an array of asset symbols.
   */
  ArrayList<String> getTrendingStocksForRegion(String region);

  /**
   * Method to retrieve historic data regarding provided
   * stock tickers between predefined time interval and range.
   *
   * @param assetSymbols list of stock tickers
   * @param interval granularity of the returned data.
   * @param range time period of which data is to be gathered.
   * @return list of {@link AssetQuote} objects containing individual stock histories.
   */
  List<AssetQuote> getHistoricalInfo(List<String> assetSymbols, String interval,
                                     String range);

  /**
   * Returns information regarding the specified exchange and region.
   *
   * @param region region where exchange is located.
   * @param exchange the exchange to return data on.
   * @return A string containing relevant information
   */
  String getExchangeInfo(String region, String exchange);

  /** Checks if an asset symbol is valid or real or exists in an exchange.

   * @param assetSymbol is the ass

   * @return a boolean indicating whether asset is valid.
   */
  boolean checkAssetSymbol(String assetSymbol);
}
