package com.lyit.csd.tests.integration;

import com.lyit.csd.domain.PortfolioManager;
import com.lyit.csd.domain.PortfolioSystem;
import com.lyit.csd.domain.AssetQuote;
import com.lyit.csd.marketapi.MarketClientFactory;
import com.lyit.csd.marketapi.abstraction.MarketClient;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PortfolioManager_GetHistoricData {
    private PortfolioSystem portfolioSystem;

    @BeforeTest
    public void setup(){
        MarketClient client = new MarketClientFactory().getMarketClient(MarketClientFactory.MarketClientTypes.Yahoo);
        portfolioSystem = new PortfolioManager(client,true);
    }

    @Test
    public void getHistoricData_GivenValidSymbols_ReturnsList(){
        List<String> validSymbols = new ArrayList<>();
        validSymbols.add("TSLA");
        validSymbols.add("GME");
        validSymbols.add("MSFT");

        String interval = "1d";
        String range = "5d";
        int expectedSize = 3;
        List<AssetQuote> result = portfolioSystem.getHistoricalData(validSymbols, interval, range);

        Assert.assertEquals(result.size(), expectedSize);
    }

    @Test
    public void getHistoricData_GivenSymbolsOfMixedValidity_ReturnsPartialList(){
        List<String> symbols = new ArrayList<>();
        symbols.add("TSLA");
        symbols.add("BBBBBBBB");
        symbols.add("MSFT");

        String interval = "1d";
        String range = "5d";
        int expectedSize = 2;
        List<AssetQuote> result = portfolioSystem.getHistoricalData(symbols, interval, range);

        Assert.assertEquals(result.size(), expectedSize);
    }

    @Test
    public void getHistoricData_GivenInvalidSymbols_ReturnsEmptyList(){
        List<String> validSymbols = new ArrayList<>();
        validSymbols.add("BBBBBB");
        validSymbols.add("GGGGGGG");
        validSymbols.add("MMMMMMMMM");

        String interval = "1d";
        String range = "5d";
        int expectedSize = 0;
        List<AssetQuote> result = portfolioSystem.getHistoricalData(validSymbols, interval, range);

        Assert.assertEquals(result.size(), expectedSize);
    }
}
