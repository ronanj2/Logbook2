package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PortfolioManager_GetAssetQuoteITest {
    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client,true);
    }

    @Test
    public void getAssetQuote_GivenValidSymbol_ReturnsValidAssetQuote(){
        String symbol = "GME";
        AssetQuote assetQuote = portfolioSystem.getAssetQuote(symbol);
        Assert.assertNotNull(assetQuote);
    }

    @Test
    public void getAssetQuote_GivenInvalidSymbol_ReturnsNull(){
        String symbol = "BABABABA";
        AssetQuote assetQuote = portfolioSystem.getAssetQuote(symbol);
        Assert.assertNull(assetQuote);
    }
}
