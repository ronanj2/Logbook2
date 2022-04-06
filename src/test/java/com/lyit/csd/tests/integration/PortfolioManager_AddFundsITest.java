package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortfolioManager_AddFundsITest {
    @Test
    public void addFunds_ValidTransaction(){
        // Arrange
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);

        // Act
        portfolioSystem.addFunds(500);

        // Assert
        Assert.assertEquals(500, portfolioSystem.getAvailableFunds());
    }

    @Test
    public void addFunds_ZeroAmount(){
        // Arrange
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);
        double expectedBalance = portfolioSystem.getAvailableFunds();

        // Act
        portfolioSystem.addFunds(0);

        // Assert
        Assert.assertEquals(expectedBalance, portfolioSystem.getAvailableFunds());
    }

    @Test
    public void addFunds_NegativeAmount(){
        // Arrange
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);
        int expectedBalance = 0;

        // Act
        portfolioSystem.addFunds(-1);

        // Assert
        Assert.assertEquals(expectedBalance, portfolioSystem.getAvailableFunds());
    }
}
