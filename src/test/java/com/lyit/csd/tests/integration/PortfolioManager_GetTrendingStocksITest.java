package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

//@RunWith(Theories.class)
public class PortfolioManager_GetTrendingStocksITest {

  private PortfolioSystem portfolioSystem;

  @BeforeClass
  public void setup() {
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    portfolioSystem = new PortfolioManager(client,true);
  }

  @Test
  public void getTrendingStocks_ReturnsData() {
    String region = "US";
    List<String> trendingStocksForRegion = portfolioSystem.getTrendingStocks(region);
    assertNotNull(trendingStocksForRegion);
    assertFalse(trendingStocksForRegion.isEmpty());
  }

}