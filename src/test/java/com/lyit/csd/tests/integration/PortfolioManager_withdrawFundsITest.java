package com.lyit.csd.tests.integration;


import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;

import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PortfolioManager_withdrawFundsITest {
    @Test
    protected void withdrawFunds_ValidTransaction_SufficientBalanceForWithdrawal(){
        // Arrange
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);
        portfolioSystem.addFunds(500);
        double expectedBalanceAfterWithdrawal = 200;

        // Act
        portfolioSystem.withdrawFunds(300);

        // Assert
        double actualBalance = portfolioSystem.getAvailableFunds();
        Assert.assertEquals(actualBalance, expectedBalanceAfterWithdrawal);
    }

    @Test
    protected void withdrawFunds_InvalidTransaction(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);
        boolean actual = portfolioSystem.withdrawFunds(10000);
        Assert.assertFalse(actual);
    }
}
