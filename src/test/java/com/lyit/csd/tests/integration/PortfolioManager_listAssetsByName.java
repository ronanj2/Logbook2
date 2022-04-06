package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class PortfolioManager_listAssetsByName {

    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client);
    }

    @Test
    public void testListPortfolioAssetsByName_GivenNoAssets_ReturnsEmptyString() {
        List<String> assetNames = new ArrayList<>();
        String result = portfolioSystem.listPortfolioAssetsByName(assetNames);
        System.out.println(result);
        Assert.assertEquals(result,"");
    }

    @Test
    public void testListPortfolioAssetsByName_GivenHeldAsset_ReturnNotNull() {
        List<String> assetNames = new ArrayList<>();
        assetNames.add("AAPL");
        String result = portfolioSystem.listPortfolioAssetsByName(assetNames);
        System.out.println(result);
        Assert.assertNotEquals(result,"");
    }

    @Test
    public void testListPortfolioAssetsByName_GivenSymbolNotHeld_ReturnEmptyString() {
        List<String> assetNames = new ArrayList<>();
        assetNames.add("WASD");
        String result = portfolioSystem.listPortfolioAssetsByName(assetNames);
        System.out.println(result);
        Assert.assertEquals(result,"");
    }

    @Test
    public void testListPortfolioAssetsByName_GivenNameNotHeld_ReturnEmptyString() {
        List<String> assetNames = new ArrayList<>();
        assetNames.add("GoodStonks");
        String result = portfolioSystem.listPortfolioAssetsByName(assetNames);
        System.out.println(result);
        Assert.assertEquals(result,"");
    }
}