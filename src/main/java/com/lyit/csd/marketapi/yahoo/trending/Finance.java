package com.lyit.csd.marketapi.yahoo.trending;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

/**
 * Simple POJO class used for deserializing responses from yahoo finance api.
 * Holds an arraylist of type Result called result
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Finance {
  public ArrayList<Result> result;
}