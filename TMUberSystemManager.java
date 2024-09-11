import java.io.FileNotFoundException;
import java.util.*;

// Name: Jeffrey Ye
// Student ID: 501244197

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map<String, User>   users;
  private Map<String, Driver> drivers;

  private Queue<TMUberService> [] serviceQueues = new Queue[4]; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new TreeMap<String, User>();
    drivers = new TreeMap<String, Driver>();
    for (int i = 0; i<serviceQueues.length;i++){
      serviceQueues[i] = new LinkedList<>();
    }
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    if(users.get(accountId)!=null)
      return users.get(accountId);
    return null;
  }
  
  // Check for duplicate user
  private void userExists(User user)
  {
    if(users.containsKey(user.getAccountId()))
      throw new UserExistsException("User Already Exists in System");
  }
  
 // Check for duplicate driver
 private void driverExists(Driver driver)
 {
   if(drivers.containsKey(driver.getId()))
    throw new DriverExistsException("Driver Already Exists in System");
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private void existingRequest(TMUberService req)
  {
    boolean exists = false;
    for(int i = 0; i<serviceQueues.length; i++){
      if(serviceQueues[i]!=null)
      {  
        for(int j = 0; j<serviceQueues[j].size();j++){
          if(req.equals(serviceQueues[i].poll())){
            exists = true;
          }
        }
      }
    }
    if(exists)
      throw new RequestExistsException("Request Already Exists in System");
  }
  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  // private Driver getAvailableDriver()
  // {
  //   for(int i = 0; i < drivers.size(); i++){
  //     if(drivers.get(i).getStatus() == Driver.Status.AVAILABLE)
  //       return drivers.get(i);
  //   }
  //   return null;
  // }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    
    Collection<User> values = users.values();
    int count = 1;
    for (User user : values) {
      System.out.printf("%-2s. ", count);
      user.printInfo();
      System.out.println(); 
      count++;
    }
    
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    System.out.println();
    
    Collection<Driver> values = drivers.values();
    int count = 1;
    for (Driver driver : values) {
      System.out.printf("%-2s. ", count);
      driver.printInfo();
      System.out.println("\n"); 
      count++;
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    for(int i = 0; i < serviceQueues.length; i++){
      System.out.println("\nZONE "+i+"\n"+"=====");
      if(serviceQueues[i]==null)
        System.out.println("\n");
      else {  
        for(int j = 0; j < serviceQueues[i].size(); j++)
        {  
          Queue<TMUberService> tempQ = new LinkedList<>(serviceQueues[i]);
          System.out.print("\n"+(j+1)+". --------------------------------------------------");
          tempQ.poll().printInfo();
          System.out.println();
        }
      }
    }
  }

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    //    set errMsg string variable to "Invalid Address "
    //    return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    try {
      if(wallet < 0){
        throw new InvalidArgumentException("Invalid Money in Wallet");
      }
      if(!CityMap.validAddress(address)){
        throw new InvalidArgumentException("Invalid Address");
      }
      if(name.equals(null)||name.equals("")){
        throw new InvalidArgumentException("Invalid User Name");
      }
      User newUser = new User(String.valueOf(userAccountId)+String.valueOf(users.size()),name,address,wallet);
      userExists(newUser);
      users.put(String.valueOf(userAccountId)+String.valueOf(users.size()), newUser);
      newUser.printInfo();
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address)
  {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    try {
      if(name.equals(null)||name.equals("")){
        throw new InvalidArgumentException("Invalid Driver Name");
      }
      if(carModel.equals(null)||carModel.equals("")){
        throw new InvalidArgumentException("Invalid Car Model");
      }
      if(carLicencePlate.equals(null)||carLicencePlate.equals("")){
        throw new InvalidArgumentException("Invalid Car License Plate");
      }
      Driver newDriver = new Driver(String.valueOf(driverId)+String.valueOf(drivers.size()),name,carModel,carLicencePlate,address);
      driverExists(newDriver);
      drivers.put(String.valueOf(driverId)+String.valueOf(drivers.size()),newDriver);
      newDriver.printInfo();
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Check for valid parameters
	// Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user at a time!
    // Change driver status
    // Add the ride request to the list of requests
    // Increment the number of rides for this user
    try {
      int distance = 0;
      User currentUser = users.get(accountId);
      if(CityMap.validAddress(from)&&CityMap.validAddress(to)){
        distance = CityMap.getDistance(from,to);
        if (distance<1){
          throw new InsufficientArgumentException("Insufficient Travel Distance");
        }
      } else {
        throw new InvalidArgumentException("Invalid Address");
      }
      double dis = distance;
      if(dis*RIDERATE>currentUser.getWallet()){
        throw new InsufficientArgumentException("Insufficient Funds");
      }
      TMUberRide newRide = new TMUberRide(from, to, currentUser,distance,dis*RIDERATE);
      try{
        existingRequest(newRide);
      } catch (RequestExistsException e) {
        throw new RequestExistsException("User Already has Ride Request");
      }
      currentUser.addRide();
      serviceQueues[CityMap.getCityZone(currentUser.getAddress())].add(newRide);
      System.out.println("\nRIDE for: "+currentUser.getName()+" From: "+from+" To: "+to);
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food order id
    // Increment the number of deliveries the user has had
      try{
      int distance = 0;
      User currentUser = users.get(accountId);
      if(CityMap.validAddress(from)&&CityMap.validAddress(to)){
        distance = CityMap.getDistance(from,to);
        if (distance<1){
          throw new InsufficientArgumentException("Insufficient Travel Distance");
        }
      } else {
        throw new InvalidArgumentException("Invalid Address");
      }
      double dis = distance;
      if(dis*DELIVERYRATE>currentUser.getWallet()){
        throw new InsufficientArgumentException("Insufficient Funds");
      }
      TMUberDelivery newDelivery = new TMUberDelivery(from, to, currentUser,distance,dis*DELIVERYRATE,restaurant,foodOrderId);
      
      try{
        existingRequest(newDelivery);
      } catch (RequestExistsException e) {
        throw new RequestExistsException("User Already Has Delivery Request at Restaurant with this Food Order");
      }
        
      
      currentUser.addDelivery();
      
      serviceQueues[CityMap.getCityZone(currentUser.getAddress())].add(newDelivery);
      System.out.println("\nDELIVERY for: "+currentUser.getName()+" From: "+from+" To: "+to);
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public void cancelServiceRequest(int request, int zone)
  {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    try {
      if(zone<0||zone>3)
        throw new InvalidArgumentException("Invalid Zone");
      if(request<=serviceQueues[zone].size()&&request>0){
        int count = 1;
        Queue<TMUberService> tempQ = new LinkedList<>();
        for (TMUberService servReq : serviceQueues[zone]) {
          if(count == request){
            if(servReq.getServiceType().equals("RIDE"))
              servReq.getUser().removeRide();
            else
              servReq.getUser().removeDelivery();
            break;
          } else {
            count++;
            tempQ.add(servReq);
          }
        }
        serviceQueues[zone] = tempQ;
        System.out.println("Service request #" + request + " cancelled");
      } else {
        throw new InvalidArgumentException("Invalid Request #");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public void dropOff(String driverId)
  {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    try {
      double driverCut = 0;
      Driver currentDriver = drivers.get(driverId);
      if(currentDriver == null)
        throw new DriverNotFoundException("No Driver Found With Given ID");
      driverCut+=currentDriver.getService().getCost()*PAYRATE;
      totalRevenue+=currentDriver.getService().getCost()-driverCut;
      currentDriver.pay(driverCut);
      currentDriver.setStatus(Driver.Status.AVAILABLE);
      currentDriver.getService().getUser().payForService(currentDriver.getService().getCost());
      Queue<TMUberService> tempQ = new LinkedList<>();
      for (TMUberService servReq : serviceQueues[currentDriver.getZone()]) {
        if(!currentDriver.getService().equals(servReq))
          tempQ.offer(servReq);
      }
      serviceQueues[currentDriver.getZone()] = tempQ;
      currentDriver.setAddress(currentDriver.getService().getTo());
      System.out.println("Driver "+driverId+" dropping off");
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }

  // Sort users by name
  // Then list all users
  public void sortByUserName()
  {
    // Sort the users by the value returned in getName function
    ArrayList<User> userList = new ArrayList<>();
    for (String key : users.keySet()) {
      userList.add(users.get(key));
    }
    Collections.sort(userList,Comparator.comparing(User::getName));
    System.out.println();
    int count = 1;
    for (User user : userList)
    {
      System.out.printf("%-2s. ", count);
      user.printInfo();
      System.out.println(); 
      count++;
    }
  }

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet()
  {
    // Sort by the double returned by getWallet
    ArrayList<User> userList = new ArrayList<>();
    for (String key : users.keySet()) {
      userList.add(users.get(key));
    }
    Collections.sort(userList,Comparator.comparingDouble(User::getWallet));
    System.out.println();
    int count = 1;
    for (User user : userList)
    {
      System.out.printf("%-2s. ", count);
      user.printInfo();
      System.out.println(); 
      count++;
    }
  }
  
  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance()
  {
    // Sort by the int returned by getDistance
    for (Queue<TMUberService> queue : serviceQueues) {
      ArrayList<TMUberService> sortList = new ArrayList<>(queue);
      Collections.sort(sortList,Comparator.comparingInt(TMUberService::getDistance));
      queue.clear();
      sortList.forEach(queue::offer);
      System.out.println();
    
      listAllServiceRequests();
    }
    
  }

  public void pickup(String driverId)
  {
    try {
      Driver driver = drivers.get(driverId);
      if(driver==null)
          throw new InvalidArgumentException("No Driver Found with Given ID");
      int currentZone = CityMap.getCityZone(driver.getAddress());
      TMUberService currentService = serviceQueues[currentZone].remove();
      if(driver.getZone()==CityMap.getCityZone(currentService.getFrom())){
        driver.setService(currentService);
        driver.setStatus(Driver.Status.DRIVING);
        driver.setAddress(currentService.getFrom());
        System.out.println("Driver "+driverId+" picking up in zone "+String.valueOf(currentZone));
      } else {
        throw new DriverNotFoundException("No Driver in zone "+currentZone);
      }
    } catch (NoSuchElementException e){
      System.out.println("No Service Request Found");
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
    }
  }
  public void setUsers(ArrayList<User> userList)
  {
    for (User user : userList) {
      users.put(user.getAccountId(),user);
    }
  }
  public void loadUsers(String filename) throws FileNotFoundException
  {
    setUsers(TMUberRegistered.loadPreregisteredUsers(filename));
  }
  public void setDrivers(ArrayList<Driver> driverList)
  {
    for (Driver driver : driverList) {
      drivers.put(driver.getId(),driver);
    }
  }
  public void loadDrivers(String filename) throws FileNotFoundException
  {
    setDrivers(TMUberRegistered.loadPreregisteredDrivers(filename));
  }

  public void driveTo(String driverId, String address)
  {
    try {
      Driver driver = drivers.get(driverId);
      if(driver==null)
        throw new InvalidArgumentException("No Driver Found with Given ID");
      if(driver.getStatus().equals(Driver.Status.AVAILABLE)&&CityMap.validAddress(address))
        driver.setAddress(address);
      else 
        throw new InvalidArgumentException("Invalid Address");
      System.out.println("Driver " +driverId+" now in zone "+CityMap.getCityZone(address));
    } catch (Exception e) {
      System.out.println(e.getMessage()+". Action Incomplete");
      return;
    }
  }
}

class UserExistsException extends RuntimeException {
  public UserExistsException(String m){
    super(m);
  }
}
class RequestExistsException extends RuntimeException {
  public RequestExistsException(String m){
    super(m);
  }
}
class DriverExistsException extends RuntimeException {
  public DriverExistsException(String m){
    super(m);
  }
}
class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(String m){
    super(m);
  }
}
class InvalidArgumentException extends RuntimeException {
  public InvalidArgumentException(String m){
    super(m);
  }
}
class InsufficientArgumentException extends RuntimeException {
  public InsufficientArgumentException(String m){
    super(m);
  }
}
