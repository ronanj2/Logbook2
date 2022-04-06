package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PortfolioManager_GetPortfolioValueITest {

  private PortfolioSystem portfolioSystem;

  @BeforeClass
  public void setup() {
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    portfolioSystem = new PortfolioManager(client,true);
  }

  @Test
  protected void getPortfolioValue_ReturnsData()  {
    double portfolioValue = portfolioSystem.getPortfolioValue();
    assertTrue(portfolioValue > 0);
  }


}
