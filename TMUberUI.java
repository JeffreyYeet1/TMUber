import java.io.FileNotFoundException;
import java.util.Scanner;

// Name: Jeffrey Ye
// Student ID: 501244197

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT")){
        scanner.close();
        return;
      }
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if(scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        tmuber.registerNewDriver(name, carModel, license, address);
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl!! Only needed when mixing strings and int/double
        }
        tmuber.registerNewUser(name, address, wallet);
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestRide() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        System.out.print("User Account Id: ");
        String accID = scanner.nextLine();
        System.out.print("From Address: ");
        String FromAddress = scanner.nextLine();
        System.out.print("To Address: ");
        String toAddress = scanner.nextLine();
        tmuber.requestRide(accID,FromAddress,toAddress);
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestDelivery() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        // "Restaurant: "           (string)
        // "Food Order #: "         (string)
        System.out.print("User Account Id: ");
        String accID = scanner.nextLine();
        System.out.print("From Address: ");
        String FromAddress = scanner.nextLine();
        System.out.print("To Address: ");
        String toAddress = scanner.nextLine();
        System.out.print("Restaurant: ");
        String restaurant = scanner.nextLine();
        System.out.print("Food Order #: ");
        String foodOrderNum = scanner.nextLine();
        tmuber.requestDelivery(accID,FromAddress,toAddress,restaurant,foodOrderNum);
      }
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        tmuber.sortByDistance();
      }
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int request = -1;
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        System.out.println("Zone #: ");
        int zone = -1;
        zone = scanner.nextInt();
        tmuber.cancelServiceRequest(request, zone);
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        String driverId = "";
        System.out.print("Driver Id: ");
        if (scanner.hasNextLine())
        {
          driverId = scanner.nextLine();
        }
        tmuber.dropOff(driverId); 
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }

      else if (action.equalsIgnoreCase("PICKUP")) 
      {
        String driverId;
        System.out.print("Driver Id: ");
        driverId = scanner.nextLine();
        tmuber.pickup(driverId);

      }

      else if (action.equalsIgnoreCase("LOADUSERS")) 
      {
        System.out.print("Users File: ");
        String filename = scanner.nextLine();
        try {
          tmuber.loadUsers(filename);
        } catch (FileNotFoundException e) {
        } 
      }

      else if (action.equalsIgnoreCase("LOADDRIVERS")) 
      {
        System.out.print("Drivers File: ");
        String filename = scanner.nextLine();
        try {
          tmuber.loadDrivers(filename);
        } catch (FileNotFoundException e) {
        }
      }

      else if (action.equalsIgnoreCase("DRIVETO")) 
      {
        System.out.print("Driver Id: ");
        String driverId = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        tmuber.driveTo(driverId, address);
      }
  
      System.out.print("\n>");
    }
    scanner.close();
  }
}

