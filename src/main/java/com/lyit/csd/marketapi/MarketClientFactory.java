package com.lyit.csd.marketapi;

import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import com.lyit.csd.marketapi.yahoo.YahooClient;
import java.util.ArrayList;
import java.util.List;

/**
 * MarketClientFactory constructor.
 */
public class MarketClientFactory {

  /**
   * Enumeration containing both types of market client
   * implemented in our code.
   */
  public enum MarketClientTypes {
    /**
     * Mock Exchange Client. This is used for offline testing and unit testing.
     */
    Mock,
    /**
     * Yahoo Finance API.
     */
    Yahoo
  }

  /**
   * Method to provide the required client functionality.
   *
   * @param marketClientType specifies the type of client needed.
   * @return MarketClient object
   */
  public MarketClient getMarketClient(MarketClientTypes marketClientType) {
    switch (marketClientType) {
      case Mock:
        return new MockClient();
      default:  // Yahoo Finance API is the only 'real' implemented market client currently.

        List<String> apiKeys = new ArrayList<>();
        apiKeys.add("0KSRf9fOdQ6qxxcs2cY226dacPNjUZ3b9ORYSz65");  // Ronan's
        apiKeys.add("WPAkfZt5wl9cPKqdiet7Q1Oj67hE7kmE9lR0hJbR");  // Ronan's second
        apiKeys.add("2KApt0ym7P6FRThB8yve58ccdQGCSHpS2yQsiiXo");  // Ronan's third
        apiKeys.add("yTuGh6Z2pC7rkxfqQPbxN9xYaMsW9uxS2f5YJp58");  // Oisin's first
        apiKeys.add("FF07orAfkV2MsM6f5JKJH3EwqP3NLc3zxv8dYOB3");
        apiKeys.add("wlIJ3asMpl8NtD6HqHORb8UmR2S63rceak4croaJ");
        apiKeys.add("7CQPby8LuTaFsQmetJEZ08zZG2wOioag7W0fakck");
        apiKeys.add("yRKL2PLRfO9sOqGVPYQPr8xfBlSY4jP11ShHrO5J");
        apiKeys.add("TmZqfcWwLu1sLAd4woanf1dFVTVNByUy2BcmwuO3");
        apiKeys.add("gXCyOcYyWM4EhoiFA4Lpf5tv3tXhbS5n1ipYm9qn");
        apiKeys.add("OUqHr23YpF6EwzbxWb7YY7h1x04wGg3X45ij0aDL");
        apiKeys.add("FgBl3v0E8vUVvla56f7M7pSIoZHNkt736GkLxlrc");
        apiKeys.add("L1SRk5Dc3H4c5gXOpIQLI1r4SGNqL9rs7bSDoTpD"); // David's first
        apiKeys.add("GcKyhr8jjgaFj7btqFtNW8lpVWOfSRR37lztxZRA"); // David's second
        apiKeys.add("NBowNWDYQL3oDQRU8C9cJ2CHSBY0sMGU6UlzjEDo"); // David's third
        
        String baseUrl = "https://yfapi.net";
        return new YahooClient(baseUrl, apiKeys);
    }
  }
}
