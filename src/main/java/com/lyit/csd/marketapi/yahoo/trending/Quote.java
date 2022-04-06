package com.lyit.csd.marketapi.yahoo.trending;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple POJO class used for deserializing responses from yahoo finance api.
 * Holds a String of type symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
  /**
   * Asset Symbol.
   */
  public String symbol;
}