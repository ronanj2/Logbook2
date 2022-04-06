package com.lyit.csd.tests.unit;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class PortfolioManager_GetAssetInformationUTest {

  @Test
  protected void getAssetInformation_ManySymbols_ReturnsData()  {
    // Arrange
    MarketClient marketClient = new MockClient();
    PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

    // Act
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("TSLA");
    assetSymbols.add("BTC-USD");
    List<AssetQuote> assetInformation = portfolioSystem.getAssetInformation(assetSymbols);

    // Assert
    assertEquals(2, assetInformation.size());
  }

  @Test
  protected void getAssetInformation_NoSymbols_ReturnsEmptyList()  {
    // Arrange
    MarketClient marketClient = new MockClient();
    PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

    // Act
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("");
    List<AssetQuote> assetInformation = portfolioSystem.getAssetInformation(assetSymbols);

    // Assert
    assertEquals(0, assetInformation.size());
  }
}
