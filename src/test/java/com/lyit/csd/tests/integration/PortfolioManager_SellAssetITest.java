package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.Asset;

import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class PortfolioManager_SellAssetITest {
  @Test
  protected void sellAsset_singleUnit_happyPath()  {
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

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

  @Test
  protected void sellAsset_SellMoreThanOwned_ReturnFalse(){
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    List<Asset> assetsHoldings = portfolioSystem.getAssetsWithHoldings("TSLA");

    double beforeUnitsBalance = assetsHoldings.stream()
            .mapToDouble(Asset::getCurrentUnitsBalance).sum();

    double beforeAvailableFunds = portfolioSystem.getAvailableFunds();

    boolean isSold = portfolioSystem.sellAsset("TSLA", 11);

    double afterUnitsBalance = assetsHoldings.stream()
            .mapToDouble(Asset::getCurrentUnitsBalance).sum();

    double afterAvailableFunds = portfolioSystem.getAvailableFunds();

    assertFalse(isSold);
  }
}

