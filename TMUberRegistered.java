import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import java.io.*;

// Name: Jeffrey Ye
// Student ID: 501244197

public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 
    public static ArrayList<User> loadPreregisteredUsers(String filename)
    {
        try{
            Scanner in = new Scanner(new File(filename));
            ArrayList<User> users = new ArrayList<>();
            while(in.hasNextLine()){
                User newUser = new User(generateUserAccountId(users), in.nextLine(), in.nextLine(), Double.valueOf(in.nextLine()));
                users.add(newUser);
            }
            System.out.println("Users Loaded");
            in.close();
            return users;
        } catch (FileNotFoundException e) {
            System.out.println(filename+" Not Found");
            return new ArrayList<User>();
        }
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    public static ArrayList<Driver> loadPreregisteredDrivers(String filename)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            ArrayList<Driver> drivers = new ArrayList<>();
            while(in.hasNextLine()){
                String name = in.nextLine();
                String model = in.nextLine();
                String license = in.nextLine();
                String address = in.nextLine();
                Driver newDriver = new Driver(generateDriverId(drivers),name,model,license,address);
                drivers.add(newDriver);
            }
            System.out.println("Drivers Loaded");
            in.close();
            return drivers;
        } catch (FileNotFoundException e) {
            System.out.println(filename +" Not Found");
            return new ArrayList<Driver>();
        }
    }
}

