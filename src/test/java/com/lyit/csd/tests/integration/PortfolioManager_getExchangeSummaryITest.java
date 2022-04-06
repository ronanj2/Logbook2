package com.lyit.csd.tests.integration;
import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PortfolioManager_getExchangeSummaryITest{

    private PortfolioSystem portfolioSystem;

    @BeforeClass
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client,true);
    }

    @Test
    public void getExchangeSummary_PassedInvalidSymbol_ReturnsNull(){
        String invalidExchange = "BBBBB";
        String region = "US";
        String actualResult = portfolioSystem.getExchangeSummary(region, invalidExchange);
        Assert.assertNull(actualResult);
    }

    @Test
    public void getExchangeSummary_PassedValidSymbol_ReturnsString(){
        String validExchange = "NYSE";
        String region = "AU";
        String actualResult = portfolioSystem.getExchangeSummary(region, validExchange);
        Assert.assertNotNull(actualResult);
    }
}
