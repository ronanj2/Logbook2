package com.lyit.csd.tests.unit;


import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PortfolioManager_withdrawFundsUTest {

    protected PortfolioSystem portfolioSystem;

    @BeforeClass
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client);
    }

    @Test
    protected void withdrawFunds_ValidTransaction_SufficientBalanceForWithdrawal(){
        portfolioSystem.addFunds(500);
        double expectedBalanceAfterWithdrawal = 200;
        portfolioSystem.withdrawFunds(300);
        double actualBalance = portfolioSystem.getAvailableFunds();
        Assert.assertEquals(actualBalance, expectedBalanceAfterWithdrawal);
    }

    @Test
    protected void withdrawFunds_InvalidTransaction(){
        boolean actual = portfolioSystem.withdrawFunds(10000);
        Assert.assertFalse(actual);
    }
}
