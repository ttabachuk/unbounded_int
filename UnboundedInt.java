/*
  Author: Ammaad Denmark, Tim Tabachuk
  Date: 10/12/2022
  Filename: UnboundedInt.java
  -----------------------------------------------------------------------------
  The following program will allow a user to store a number that exceeds the
  capacity of integers in java. This class will use a linked list structure
  to store the value of the number entered in by the user.
  -----------------------------------------------------------------------------
*/
/**
 * @author
 *         UnboundedInt class - Ammaad Denmark, Tim Tabachuk
 * @author
 *         IntNode class - Michael Main
 **/

public class UnboundedInt implements Cloneable {
  /*
   * Invariant of the UnboundedInt class:
   * 1. The number entered in by the user will be stored using a linked list.
   * 2. manyNodes - the number of nodes stored in the linked list
   * 3. front - a link to the head of the linked list
   * 4. back - a link to the tail of the linked list
   * 5. cursor - a link to a list. Used for iterating through the linked list.
   */

  private int manyNodes;
  private IntNode front;
  private IntNode back;
  private IntNode cursor;

  /**
   * The following method is the constructor. The method accepts the number
   * entered
   * by the user as a string, parses the string, and creates a linked list
   * containing the number entered by the user.
   * 
   * @param
   * number        - a string that represents the number entered by the user
   * @precondition
   *               the string entered must not be empty
   * @postcondition
   *                a linked list storing the number entered by the user has been
   *                created
   *                and filled
   * @exception
   * IllegalArgumentException
   *                                     OutOfMemoryError
   **/
  public UnboundedInt(String number) throws IllegalArgumentException, OutOfMemoryError {

    // ensure that the string is not empty;
    if (number.length() < 1) {
      throw new IllegalArgumentException("No integer was provided.");
    }

    manyNodes = 0;

    // if the string has at least one character, but no more than 3,
    // add it to the list. Note, head and tail will reference the same node.
    if (number.length() <= 3) {
      front = new IntNode(Integer.parseInt(number), null);
      back = front;
      manyNodes++;
    } // end if
    else {
      int endIndex;

      // Determine how many groupings of 3 can be made from
      // the string passed in. Do this by finding an starting index that
      // allows the most grouping of 3.
      boolean remainder = true;
      switch (number.length() % 3) {
        case 1:
          endIndex = 1;
          break;
        case 2:
          endIndex = 2;
          break;
        default:
          endIndex = 0;
          remainder = false;
          break;
      }

      // if there is a remainder, add it to the list
      int data;
      if (remainder) {
        data = Integer.parseInt(number.substring(0, endIndex));
        front = new IntNode(data, null);
        manyNodes++;
      }

      // parse the string into groupings of 3
      for (int i = endIndex; i < number.length(); i += 3) {
        data = Integer.parseInt(number.substring(i, i + 3));
        // If front node was not yet created, create it.
        if (front == null) {
          front = new IntNode(data, null);
        } else {
          IntNode node = new IntNode(data, front);
          front = node;

        }
        manyNodes++;
      }
    } // end else
  }

  /**
   * The following method will add two UnboundedInt objects together and return
   * a new linked list containg the result.
   * 
   * @param
   * that        - a reference to the UnboundedInt object passed in (summand)
   * @precondition
   *               the current list and the list passed in must not be empty
   * @postcondition
   *                a new linked list containing the result of the addition has
   *                been created
   *                and returned to the calling method
   * @exception
   * OutOfMemoryError
   *                             NullPointerException
   **/
  public UnboundedInt add(UnboundedInt that) throws OutOfMemoryError, NullPointerException {
    // declare local constants/ variables
    final int MAX = 999;
    String padding = "";
    String result = "";

    // initialize both cursors
    this.start();
    that.start();

    // begin the addition
    int sum = 0;
    int carry = 0;
    while (this.cursor != null && that.cursor != null) {
      sum = this.cursor.getData() + that.cursor.getData() + carry;

      if (sum > MAX) {
        carry = sum / (MAX + 1);
        sum %= (MAX + 1);
      } else {
        carry = 0;
      }

      // determine if padding is needed
      padding = getPadding(sum);
      result = padding + sum + result;

      // advance to the next nodes
      this.advance();
      that.advance();
    }

    // If both cursors are null, we are finished. Otherwise, add the remaining
    // data list that does not have a null cursor.
    if (this.cursor != null || that.cursor != null) {
      while (that.cursor != null) {
        sum = that.cursor.getData() + carry;
        padding = getPadding(sum);
        result = padding + sum + result;
        carry = 0;
        that.advance();
      }
      while (this.cursor != null) {
        sum = this.cursor.getData() + carry;
        padding = getPadding(sum);
        result = padding + sum + result;
        carry = 0;
        this.advance();
      }
    } else {
      // add any carry left over
      if (carry > 0) {
        result = carry + result;
      }
    }

    // create the new UnboundedIntObject
    UnboundedInt added = new UnboundedInt(result);
    return added;

  }

  /**
   * The following method will take the current UnboundedInt object and
   * multiply it by the UnboundedInt object that is passed in.
   * 
   * @param
   * that        - a reference to the UnboundedInt object passed in
   * @precondition
   *               the current UnboundedInt object and the UnboundedInt object
   *               passed
   *               in must not be null.
   * @postcondition
   *                A new UnboundedInt object has been created and filled with the
   *                result
   *                of the multiplication
   * @exception
   * NullPointerException,            OutOfMemoryError
   **/
  public UnboundedInt multiply(UnboundedInt that) throws NullPointerException,
      OutOfMemoryError {
    final int MAX = 999;
    // We can create a new Unbounded int object to store the value of the
    // multiplication. In out loop structure, we will add the result of each
    // set of multiplication. The object will act as an accumulator.
    UnboundedInt result = new UnboundedInt("0");
    // the following unbounded int object will contain the data from one set
    // of multiplication
    UnboundedInt step;

    int carry = 0;
    int data;
    String weight = ""; // represents the numerical weight of each node
    String strNum = ""; // temporary string variable for storing step value
    String padding = ""; // padded 0's
    // Use a nested for loop structure to iterate through both linked lists
    // and multiply them together.
    for (this.start(); this.cursor != null; this.advance()) {
      strNum += weight;
      for (that.start(); that.cursor != null; that.advance()) {
        data = this.cursor.getData() * that.cursor.getData() + carry;
        carry = data / (MAX + 1); // (same as mod 1000)
        data %= (MAX + 1);
        ;
        padding = getPadding(data);
        // accumulate the result to the string
        strNum = padding + data + strNum;
      }
      // add any carry left over
      if (carry > 0) {
        strNum = carry + strNum;
        // clear the carry;
        carry = 0;
      }
      // create an UnboundedInt object storing a result from the previous
      // multiplication
      step = new UnboundedInt(strNum);
      // accumulate the previous step to the result object
      result = result.add(step);
      // clear the data stored in strNum
      strNum = "";
      // increase the weight for the next node;
      weight += "000";
    }
    // return the result of the multiplication to the calling method
    return result;

  }

  /**
   * The following method will take the current UnboundedInt object
   * and return a copy of that list
   * 
   * @param
   * none
   * @precondition
   *               the current object must not be null
   * @postcondition
   *                a copy of the current objecs list has been created and
   *                returned
   *                to the calling method
   * @exception
   * RuntimeException,            OutOfMemoryError
   **/
  public UnboundedInt clone() throws IllegalArgumentException, OutOfMemoryError {
    UnboundedInt clone;

    try {
      // create the clone
      clone = (UnboundedInt) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalArgumentException("Class does not implement cloneable");
    }
    clone.front = IntNode.listCopy(this.front);
    return clone;

  }

  /**
   * The following method will determine if two UnboundedIntObject are
   * equal. This method overrides the equals method from the object class.
   * 
   * @param
   * obj        - reference to an object type
   * @precondition
   *               the object we are calling the method on and the argument object
   *               must not be null
   * @postcondition
   *                A boolean indicating whether the objects are equal will be
   *                returned
   *                to the calling functions.
   * @exception
   * NullPointerException
   **/
  public boolean equals(Object obj) throws NullPointerException {
    // return flag;
    boolean equals = true;

    // determine if the object passed in is of type UnboundedInt
    if (obj instanceof UnboundedInt) {
      // typecast the object to UnboundedInt type
      UnboundedInt candidate = (UnboundedInt) obj;
      // deterine if both objects have the same number of nodes. If
      // they dont, they will not be equal.
      if (this.manyNodes == candidate.manyNodes) {
        // initialize the cursor for both objects and iterate through
        // the linked list. If any nodes differ, they will not be equal
        this.start();
        candidate.start();
        while (this.cursor != null && candidate.cursor != null) {
          if (this.cursor.getData() != candidate.cursor.getData()) {
            equals = false;
            break;
          }
          // advance to the next set of nodes
          this.advance();
          candidate.advance();
        }
      } else {
        equals = false;
      }

    } else {
      equals = false;
    }

    return equals;
  }

  /**
   * The following method will iterate through the linked list and convert
   * the number stored in the list to a string.
   * 
   * @param
   * none
   * @precondition
   *               the current list must not be empty
   * @postcondition
   *                the number contained in the linked list has been converted to
   *                a String
   *                and returned to the calling method
   * @exception
   * NullPointerException
   **/
  public String toString() throws NullPointerException {
    // string to store the number
    String number = "";
    String comma = "";
    int data;
    for (int i = manyNodes; i > 0; i--) {

      // get the data from the link returned by listPosition();
      data = (IntNode.listPosition(front, i)).getData();
      /*
       * Determine if padding needs to be added to the number.
       * Note, the i != manyNodes will prevent padding from being
       * added to the number at the front of the string.
       */
      if (data < 10 && i != manyNodes) {
        number += "00";
      } else if (data < 100 && i != manyNodes) {
        number += "0";
      }
      // determine if commas need to be used
      if (i > 1) {
        comma = ",";
      }
      number += data + comma;
      comma = "";
    }
    return number;
  }

  /**
   * The following method set cursor to reference the same link as front.
   * 
   * @param
   * none
   * @precondition
   *               the current list must not be empty
   * @postcondition
   *                cursor is set to reference the same link as front
   * @exception
   * NullPointerException
   **/
  public void start() throws NullPointerException {
    cursor = front;
  }

  /**
   * The following method takes the link referenced by cursor and advances it to
   * the next link.
   * 
   * @param
   * none
   * @precondition
   *               the current list must not be empty
   * @postcondition
   *                cursor now references the next link in the list
   * @exception
   * NullPointerException
   **/
  public void advance() throws NullPointerException {
    cursor = cursor.getLink();
  }

  /**
   * The following method will determine if padding is needed for the number
   * passed in.
   * 
   * @param
   * num        - a number stored in the linked list
   * @precondition
   *               none
   * @postcondition
   *                A string containing the padded 0's has been returned to the
   *                calling method
   * @exception
   * NullPointerException
   **/
  public static String getPadding(int num) {
    String padding = "";
    if (num < 10) {
      padding = "00";
    } else if (num < 100) {
      padding = "0";
    } else {
      padding = "";
    }
    return padding;

  }

}
