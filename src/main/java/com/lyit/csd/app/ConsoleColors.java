package com.lyit.csd.app;

/**
 * ConsoleColors defines colours for the User Interface.
 */
public class ConsoleColors {
  /**
   * Resets console formatting.
   */
  public static final String RESET = "\033[0m";  // Text Reset

  /**
   * Returns a <code>string</code> with setting for red bolded font in the console.
   */
  public static final String RED_BOLD = "\033[1;31m";    // RED
  /**
   * Returns a <code>string</code> with setting for yellow bolded font in the console.
   */
  public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
  /**
   * Returns a <code>string</code> with setting for purple bolded font in the console.
   */
  public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE

  /**
   * Returns a <code>string</code> with setting for green underlined font in the console.
   */
  public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
  /**
   * Returns a <code>string</code> with setting for blue underlined font in the console.
   */
  public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
}