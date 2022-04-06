package com.lyit.csd.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * {@link PortfolioSystem} class provides from useful helpers for dealing with epoch conversion
 * between long and human-readable strings.
 */
public class DateTimeHelper {
  /**
   * Get the current epoch time in milliseconds.

   * @return a <code>long</code>.
   */
  public static long getCurrentEpochTime() {
    return Instant.now().toEpochMilli();
  }

  /**
   * Converts a {@link LocalDate} to epoch milliseconds.

   * @param localDate is the input to convert.
   * @return a <code>long</code> representing the {@link LocalDate} in epoch milliseconds.
   */
  public static long convertToEpochMilliseconds(LocalDate localDate) {
    Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    long timeInMillis = instant.toEpochMilli();
    return timeInMillis;
  }

  /**
   * Get the epoch milliseconds for a provided date.

   * @param year the year
   * @param month the month
   * @param day the day
   * @return a <code>long</code> representing the input in epoch milliseconds.
   */
  public static long getEpochMillisecondsForDate(int year, int month, int day) {
    return convertToEpochMilliseconds(LocalDate.of(year, month, day));
  }

  /**
   * Gets a human-readable String for epoch.

   * @param timestamp inputted epoch in milliseconds.
   * @return a <code>String</code> representation of the inputted timestamp.
   */
  public static String getEpochAsHumanReadable(long timestamp) {
    String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        .format(new java.util.Date(timestamp));
    return date;
  }

  /**
   * Get a {@link LocalDate} for inputted epoch timestamp.

   * @param timestamp inputted epoch milliseconds.
   * @return a <code>LocalDate</code>.
   */
  public static LocalDate getDateFromEpochMilli(long timestamp) {
    LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    return date;
  }

  /**
   * Check if inputted epoch falls between two provided epoch. Typically, it is
   * used to check if a value falls between two dates.

   * @param inputTimestamp the value to check
   * @param minimumTimestamp the minimum value to compare the input with
   * @param maximumTimestamp the maximum value to compare the input with.
   * @return a <code>boolean</code> to indicate whether the input is in the range.
   */
  public static boolean isEpochInDateRange(long inputTimestamp,
                                           long minimumTimestamp, long maximumTimestamp) {
    LocalDate inputDate = DateTimeHelper.getDateFromEpochMilli(inputTimestamp);
    LocalDate minDate = DateTimeHelper.getDateFromEpochMilli(minimumTimestamp);
    LocalDate maxDate = DateTimeHelper.getDateFromEpochMilli(maximumTimestamp);

    return inputDate.isAfter(minDate) && inputDate.isBefore(maxDate);
  }
}
