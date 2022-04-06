package com.lyit.csd.marketapi.yahoo.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

/**
 * Simple POJO class used for deserializing responses from yahoo finance api.
 * This class provides the output of the quote being provided, based on the provided information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {
  public ArrayList<Result> result;
}