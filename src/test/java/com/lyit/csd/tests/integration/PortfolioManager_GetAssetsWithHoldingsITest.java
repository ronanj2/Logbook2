package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.Asset;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class PortfolioManager_GetAssetsWithHoldingsITest {
    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client,true);
    }

    @Test
    public void getAssetsWithHoldings_GivenSymbolWithCurrentHoldings_ReturnsList(){
        String symbol = "TSLA";
        List<Asset> actualList = portfolioSystem.getAssetsWithHoldings(symbol);
        int expectedSize = 1;
        Assert.assertEquals(actualList.size(), expectedSize);
    }

    @Test
    public void getAssetsWithHoldings_GivenInvalidSymbol_ReturnsEmptyList(){
        String symbol = "jellybeans";
        List<Asset> actualList = portfolioSystem.getAssetsWithHoldings(symbol);
        int expectedSize = 0;
        Assert.assertEquals(actualList.size(),expectedSize);
    }
}
