/*
  Author: Tim Tabachuk
  Filename: LargeNumberTest.Java
  Date: 10/15/2022;

  The following program is a driver file for the UnboundedInt
  class. The UnboundedInt class will use the IntNode class
  to create a linked list, which will represent an Integer
  number that exceeds the size allowed for an int. The program
  will demonstate the use of the class.

*/

import java.util.Scanner;

public class LargeNumberTest {
  public static void main(String[] args) {

    displayIntroduction();

    // create a scanner object to read input data
    Scanner scanner = new Scanner(System.in);
    // create two UnboundedInt objects
    UnboundedInt int1 = new UnboundedInt(getNumber(scanner));
    UnboundedInt int2 = new UnboundedInt(getNumber(scanner));

    boolean quit = false;
    int choice;
    while (!quit) {
      displayMenu();

      choice = prompt(scanner);

      switch (choice) {
        case 1: {
          // output both numbers
          try {
            System.out.println("Integer 1: " + int1.toString());
            System.out.println("Integer 2: " + int2.toString());

          } catch (NullPointerException e) {
            System.out.println(e.getMessage());
          }
          break;
        }
        case 2: {
          // input two new numbers
          try {
            int1 = new UnboundedInt(getNumber(scanner));
            int2 = new UnboundedInt(getNumber(scanner));
            System.out.println("--Integers updated--");

          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          } catch (NullPointerException e) {
            System.out.println(e.getMessage());
          }
          break;
        }
        case 3: {
          // check if numbers are equal
          try {
            System.out.println("Numbers are equal: " + int1.equals(int2));

          } catch (NullPointerException e) {
            System.out.println(e.getMessage());
          }
          break;
        }
        case 4: {
          // report the sum of two numbers
          try {
            UnboundedInt int3 = int1.add(int2);
            System.out.println("Sum of both intgers: " + int3.toString());

          } catch (NullPointerException e) {
            System.out.println(e.getMessage());
          }

          break;
        }
        case 5: {
          // report the multiplication of two numbers
          try {
            UnboundedInt int3 = int1.multiply(int2);
            System.out.println("Product of both intgers: " + int3.toString());

          } catch (NullPointerException e) {
            System.out.println(e.getMessage());
          }

          break;
        }
        case 6: {
          // create the clone of the first number and output to user
          try {
            UnboundedInt clone = int1.clone();
            System.out.println("Clone of first number: " + clone.toString());

          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          }

          break;
        }
        case 7: {
          quit = true;
          break;
        }
        default:
          System.out.println("Invalid selection made.");
          break;
      }

    }
    System.out.println("Program Terminated.");

  }

  /*
   * The following method will display an introduction message to the
   * user.
   */
  public static void displayIntroduction() {
    System.out.println("--- Welcome to my UnboundedInt Driver File ---");
    System.out.println();
    System.out.println("This program will use the UnboundedInt Class");
    System.out.println("to demonstate how the class works and how its");
    System.out.println("methods are used. To begin please enter two ");
    System.out.println("large integers without commas.");
    System.out.println();

  }

  /*
   * The following method will display a menu of options from which the
   * user can choose from.
   */
  public static void displayMenu() {
    System.out.println();
    System.out.println("1. Display both numbers");
    System.out.println("2. Input two new numbers");
    System.out.println("3. Check if the numbers are equal");
    System.out.println("4. Report the sum of the two numbers");
    System.out.println("5. Report the multiplication of the two numbers");
    System.out.println("6. Create and output the clone of the first number");
    System.out.println("7. Quit");
    System.out.println();
  }

  /*
   * The following method will prompt the user to make an entry for
   * menu selection. The method will accept a Scanner object in its
   * parameter. The method will then return the selection made by
   * the user.
   */
  public static int prompt(Scanner sc) {
    System.out.print("Please enter your selection: ");
    while (!sc.hasNextInt()) {
      System.out.print("Invalid selection made. Please try again: ");
      // clear the Scanner
      sc.next();
      sc.nextLine();
    }
    System.out.println();
    int number = sc.nextInt();
    // clear the Scanner
    sc.nextLine();
    return number;

  }

  /*
   * The following method will prompt the user to enter a String
   * that represents a number that will be used for creating the UnboundedInt
   * objects. The method will validate the users input.
   */
  public static String getNumber(Scanner sc) {
    System.out.print("Enter integer here: ");
    String number = sc.nextLine();
    boolean validate = true;
    int j = 0;
    while (validate) {
      // verify the string is valid
      for (int i = 0; i < number.length(); i++) {
        if (!Character.isDigit(number.charAt(i))) {
          j++;
        }
      }
      if (j > 0) {
        System.out.println("Invalid integer entered. Try again.");
        System.out.print("Please enter a large integer: ");
        number = sc.nextLine();
        j = 0;
      } else {
        validate = false;
      }
    }
    return number;
  }
}
