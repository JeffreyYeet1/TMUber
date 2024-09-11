// Name: Jeffrey Ye
// Student ID: 501244197

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap
{
  public CityMap(){

  }

  // Checks if a string contains only numeric characters
  public static boolean isNumeric(String str) {
    return str != null && str.matches("-?\\d+(\\.\\d+)?");
  }
  // Checks for a valid address by splitting the address into 3 parts and then verifying the requirements for a valid address
  public static boolean validAddress(String address)
  {
    String addressParts [] = address.split(" ");
    String a1 = "";
    String a2 = "";
    String a3 = "";
    if(addressParts.length == 3){
      a1 = addressParts[0];
      a2 = addressParts[1];
      a3 = addressParts[2].toUpperCase();
    } else {
      return false;
    }
    if(isNumeric(a1)&&a1.length()==2&&a2.length()==3){
      if((a2.equals("1st")||a2.equals("2nd")||a2.equals("3rd"))||(a2.substring(1,3).equals("th")&&(int)a2.charAt(0)-48>3&&(int)a2.charAt(0)-48<10)){
        if(a3.equals("STREET")||a3.equals("AVENUE"))
          return true;
      }
    }
    return false;
  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};
    String a[] = address.split(" ");
    String c = a[2].toUpperCase();
    // Checks if its a street or avenue to determine which order the coordinates go in
    if(c.equals("STREET")){
      block[0]=(int)address.charAt(0)-48;
      block[1]=(int)address.charAt(3)-48;
    } else {
      block[1]=(int)address.charAt(0)-48;
      block[0]=(int)address.charAt(3)-48;
    }
    // Fill in the code
    return block;
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  // Uses the outlined formula to calculate distance
  public static int getDistance(String from, String to)
  {
    if(validAddress(from)&&validAddress(to))
      {int [] fromAddress = getCityBlock(from);
      int [] toAddress = getCityBlock(to);
      return Math.abs(toAddress[0] - fromAddress[0]) + Math.abs(toAddress[1] - fromAddress[1]);
    }
    return 0;
  }

  public static int getCityZone(String address){
    if(validAddress(address)){
      int [] block = getCityBlock(address);
      int s = block[1];
      int a = block[0];
      if(s>5&&s<10){
        if(a>0&&a<6)
          return 0;
        return 1;
      } else {
        if(a>5&&a<10)
          return 2;
        return 3;
      }
    }
    return -1;
  }

}
