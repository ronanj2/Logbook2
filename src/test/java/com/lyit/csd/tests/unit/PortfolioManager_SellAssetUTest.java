package com.lyit.csd.tests.unit;


import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.Asset;

import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PortfolioManager_SellAssetUTest {

  protected PortfolioSystem portfolioSystem;

  @BeforeClass
  public void setup() {
    MarketClient marketClient = new MockClient();
    portfolioSystem = new PortfolioManager(marketClient);
  }

  @Test
  protected void sellAsset_singleUnit_happyPath()  {
    List<Asset> assetsHoldings = portfolioSystem.getAssetsWithHoldings("TSLA");

    double beforeUnitsBalance = assetsHoldings.stream()
            .mapToDouble(Asset::getCurrentUnitsBalance).sum();

    double beforeAvailableFunds = portfolioSystem.getAvailableFunds();

    boolean isSold = portfolioSystem.sellAsset("TSLA", 1);

    double afterUnitsBalance = assetsHoldings.stream()
            .mapToDouble(Asset::getCurrentUnitsBalance).sum();

    double afterAvailableFunds = portfolioSystem.getAvailableFunds();

    assertTrue(isSold);
    assertEquals(afterUnitsBalance, beforeUnitsBalance - 1);
    assertTrue(afterAvailableFunds > beforeAvailableFunds);
  }
}
