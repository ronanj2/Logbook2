package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PortfolioManager_GetAssetQuotesITest {
    private PortfolioSystem portfolioSystem;

    @BeforeClass
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client,true);
    }

    @Test
    public void getAssetQuotes_GivenValidSymbols_ReturnsList(){
        List<String> validSymbols = new ArrayList<>();
        validSymbols.add("GME");
        validSymbols.add("AAPL");
        validSymbols.add("MSFT");

        int expectedSize = 3;

        List<AssetQuote> actualArray = portfolioSystem.getAssetQuotes(validSymbols);

        Assert.assertEquals(actualArray.size(), expectedSize);
    }

    @Test
    public void getAssetQuotes_GivenInvalidSymbols_ReturnsNull(){
        List<String> invalidSymbols = new ArrayList<>();
        invalidSymbols.add("GGGGGG");
        invalidSymbols.add("AAAAA");
        invalidSymbols.add("NNNNNNNN");

        int expectedSize = 0;
        List<AssetQuote> actualArray = portfolioSystem.getAssetQuotes(invalidSymbols);

        Assert.assertEquals(actualArray.size(), expectedSize);
    }

    @Test
    public void getAssetQuotes_GivenMixedInputOfMixedValidity_ReturnsPartialList(){
        List<String> assetSymbols = new ArrayList<>();
        assetSymbols.add("GGGGGG");
        assetSymbols.add("TSLA");
        assetSymbols.add("GME");
        assetSymbols.add("PPPPPP");

        int expectedSize = 2;
        List<AssetQuote> actualArray = portfolioSystem.getAssetQuotes(assetSymbols);

        Assert.assertEquals(actualArray.size(), expectedSize);
    }

    @Test
    public void getAssetQuotes_GivenEmptySymbolList_ReturnsEmptyList(){
        List<String> assetSymbols = new ArrayList<>();

        int expectedSize = 0;
        List<AssetQuote> actualArray = portfolioSystem.getAssetQuotes(assetSymbols);

        Assert.assertEquals(actualArray.size(), expectedSize);
    }
}
