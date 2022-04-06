package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PortfolioManager_ListPortfolioPurchasesInRangeITest {

    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client);
    }

    @Test
    public void listPortfolioPurchasesInRange_GivenValidInput_ReturnsText(){
        long startTime = 978373678000L; // 2001
        long endTime = 2209055278000L; // 2040
        String result = portfolioSystem.listPortfolioPurchasesInRange(startTime, endTime);
        System.out.println(result);
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void listPortfolioPurchasesInRange_GivenInvalidInput_ReturnsEmptyString(){
        long startTime = 1509459200;
        long endTime = 1527776000;
        String result = portfolioSystem.listPortfolioPurchasesInRange(startTime, endTime);
        int expectedStringLength = 0;
        Assert.assertEquals(result.length(), expectedStringLength);
    }
}
