package com.lyit.csd.tests.unit;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import com.lyit.csd.marketapi.mock.MockClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortfolioManager_AddFundsUTest {
    @Test
    public void addFunds_ValidTransaction(){
        // Arrange
        MarketClient marketClient = new MockClient();
        PortfolioSystem portfolioSystem = new PortfolioManager(marketClient);

        // Act
        portfolioSystem.addFunds(500);

        // Assert
        Assert.assertEquals(500, portfolioSystem.getAvailableFunds());
    }
}
