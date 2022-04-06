package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortfolioManager_PurchaseAssetITest {

  @Test
  protected void purchaseAsset_singleUnit_happyPath()  {
    // Arrange
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    // Act
    portfolioSystem.addFunds(50000);
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 1);

    // Assert
    Assert.assertTrue(isSuccess);
  }

  @Test
  protected void purchaseAsset_zeroUnit_expectFail()  {
    // Arrange
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 0);

    // Assert
    Assert.assertFalse(isSuccess);
  }

  @Test
  protected void purchaseAsset_minusUnit_expectFail()  {
    // Arrange
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", -1);

    // Assert
    Assert.assertFalse(isSuccess);
  }

  @Test
  protected void purchaseAsset_insufficientFunds_expectFail()  {
    // Arrange
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 1000000);

    // Assert
    Assert.assertFalse(isSuccess);
  }

  @Test
  protected void purchaseAsset_invalidSymbol_expectFail()  {
    // Arrange
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem portfolioSystem = new PortfolioManager(client);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("XYZ1234", 1);

    // Assert
    Assert.assertFalse(isSuccess);
  }
}

