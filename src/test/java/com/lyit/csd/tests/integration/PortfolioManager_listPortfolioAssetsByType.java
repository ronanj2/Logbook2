package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PortfolioManager_listPortfolioAssetsByType {

    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client);
    }

    @Test
    public void listPortfolioAssetsByType_GivenStockParameter_ReturnsNotNull(){
        String expectedAssets = "stock";
        String result = portfolioSystem.listPortfolioAssetsByType(expectedAssets);
        System.out.println(result);
        Assert.assertNotEquals(result,expectedAssets);
    }

    @Test
    public void listPortfolioAssetsByType_GivenCryptoParameter_ReturnsNotNull(){
        String expectedAssets = "crypto";
        String result = portfolioSystem.listPortfolioAssetsByType(expectedAssets);
        System.out.println(result);
        Assert.assertNotEquals(result,expectedAssets);
    }

    @Test
    public void listPortfolioAssetsByType_GivenMixedCaseParameter_ReturnsNotNull(){
        String expectedAssets = "StOcK";
        String result = portfolioSystem.listPortfolioAssetsByType(expectedAssets);
        System.out.println(result);
        Assert.assertNotEquals(result,expectedAssets);
    }

}
