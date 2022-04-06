package com.lyit.csd.tests.unit;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortfolioManager_PurchaseAssetUTest {

  @Test
  protected void purchaseAsset_singleUnit_happyPath()  {
    // Arrange
    MarketClient marketClient = new MockClient();
    PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

    // Act
    portfolioSystem.addFunds(500);
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 1);

    // Assert
    Assert.assertTrue(isSuccess);
  }

  @Test
  protected void purchaseAsset_zeroUnit_expectFail()  {
    // Arrange
    MarketClient marketClient = new MockClient();
    PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 0);

    // Assert
    Assert.assertFalse(isSuccess);
  }

  @Test
  protected void purchaseAsset_insufficientFunds_expectFail()  {
    // Arrange
    MarketClient marketClient = new MockClient();
    PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

    // Act
    boolean isSuccess = portfolioSystem.purchaseAsset("TSLA", 100000);

    // Assert
    Assert.assertFalse(isSuccess);
  }
}
