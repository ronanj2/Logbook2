package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PortfolioManager_ListPortfolioSalesInRange {

    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client);
    }

    @Test
    public void listPortfolioSalesInRange_GivenInvalidTimestamp_ReturnsEmptyString(){
        long start = 635090288;
        long end = 666626288;
        int expectedStringLength = 0;
        String result = portfolioSystem.listPortfolioSalesInRange(start, end);
        Assert.assertEquals(result.length(), expectedStringLength);
    }

    @Test
    public void listPortfolioSalesInRange_GivenValidTimestamp_ReturnsValidString(){
        long startTime = 1609542893000L;
        long endTime = 2240608493000L; // 2040
        portfolioSystem.sellAsset("TSLA", 1);

        String result = portfolioSystem.listPortfolioSalesInRange(startTime, endTime);
        System.out.println(result);
        Assert.assertFalse(result.isEmpty());
    }
}
