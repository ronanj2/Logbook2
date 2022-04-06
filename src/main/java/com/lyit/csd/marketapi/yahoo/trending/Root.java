package com.lyit.csd.marketapi.yahoo.trending;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple POJO class used for deserializing responses from yahoo finance api.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
  public Finance finance;
}