package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortfolioManager_ListAllAssetsITest {

    @Test
    public void listAllAssets_ReturnsNotNullString(){
        // Arrange
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        PortfolioSystem portfolioSystem = new PortfolioManager(client);

        // Act
        String actualResult = portfolioSystem.listAllInvestments();

        // Assert
        Assert.assertNotNull(actualResult);
    }
}
