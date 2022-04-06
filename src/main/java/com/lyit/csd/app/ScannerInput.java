package com.lyit.csd.app;

import java.util.Scanner;
import java.util.function.Function;

/**
 * The {@link ScannerInput} class is responsible to getting user input via the console.
 */
public class ScannerInput {

  private static final Scanner scanner = new Scanner(System.in);

  private ScannerInput() {}

  /**
   * This method is used to get the next line of input from the console.

   * @return a <code>string</code> representing users input via the console.
   */
  public static String getLine() {
    return scanner.nextLine();
  }

  /**
   * This method is used to get input from the user.
   * Its return type is generic as the input is casted into the
   * desired. It will recursively ask the user until the input in valid.

   * @param prompt This is the question posed to the user in the console.
   * @param function This parameter defines how the input should be validated.
   * @param <T> The return type.
   * @return Returns the collected input
   */
  public static <T> T getParsed(String prompt, Function<String, T> function) {
    T input = null;
    while (input == null) {
      try {
        input = function.apply(scanner.nextLine());
      } catch (Exception ex) {
        System.out.println(ConsoleColors.RED_BOLD + "Invalid data entered. " + prompt
            + ConsoleColors.RESET);
      }
    }
    return input;
  }
}