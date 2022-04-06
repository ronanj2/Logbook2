package com.lyit.csd.tests.integration;

import static org.testng.Assert.assertEquals;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PortfolioManager_GetAssetInformationITest {
  protected PortfolioSystem portfolioSystem;

  @BeforeTest
  public void setup(){
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    portfolioSystem = new PortfolioManager(client,true);
  }

  @Test
  protected void getAssetInformation_ManySymbols_ReturnsData()  {
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

    // Act
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("");
    List<AssetQuote> assetInformation = portfolioSystem.getAssetInformation(assetSymbols);

    // Assert
    assertEquals(0, assetInformation.size());
  }

  @Test
  protected void getAssetInformation_InvalidSymbol_ReturnsEmptyList()  {
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    // Act
    List<AssetQuote> assetInformation = portfolioSystem.getAssetInformation(assetSymbols);

    // Assert
    assertEquals(0, assetInformation.size());
  }

  @Test
  protected void getAssetInformation_ValidAndInvalidSymbols_IgnoresInvalid()  {
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // Invalid, expected ignore
    assetSymbols.add("TSLA"); // Valid, expecting info

    // Act
    List<AssetQuote> assetInformation = portfolioSystem.getAssetInformation(assetSymbols);

    // Assert
    assertEquals(1, assetInformation.size());
  }

  @Test
  protected void getAssetInformation_GivenEmptyPortfolio_ReturnsEmptyArrayList(){
    MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
    PortfolioSystem emptyManager = new PortfolioManager(client, false);
    List<String> input = new ArrayList<>();
    input.add("TSLA");
    input.add("MSFT");
    List<AssetQuote> result = emptyManager.getAssetInformation(input);

    int expectedArraySize = 0;
    Assert.assertEquals(result.size(), expectedArraySize);
  }
}
