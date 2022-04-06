package com.lyit.csd.tests.unit;


import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import org.testng.annotations.BeforeClass;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

//@RunWith(Theories.class)
public class PortfolioManager_GetTrendingStocksUTest{

  protected PortfolioSystem portfolioSystem;

  @BeforeClass
  public void setup() {
    MarketClient marketClient = new MockClient();
    portfolioSystem = new PortfolioManager(marketClient);
  }

  public static String[] regions() {
    return new String[]{"US","GB","FR","DE","HK"};
  }

  //@Test
  public void getTrendingStocks_ReturnsData(String region) {
    List<String> trendingStocksForRegion = portfolioSystem.getTrendingStocks(region);
    assertNotNull(trendingStocksForRegion);
    assertFalse(trendingStocksForRegion.isEmpty());
    }
}
