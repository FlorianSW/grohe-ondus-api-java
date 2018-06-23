# grohe-ondus-api-java

This is a library written in Java which can communicate to the Grohe ONDUS API interface, e.g. to request appliance data or to send commands.

# API reference

The API used by the Grohe ONDUS App is not publicly documented or released. The implementation of this library is basically based on
information gathered while reverse engineering the Grohe ONDUS Anroid App. This means, that the use of this API with this library
may not be permitted by Grohe and that this library may not work in the future anymore (when Grohe changes the API interface without
any backwards compatibility). Once Grohe releases a public API, this library may not be needed anymore.

# Usage

This library represents the API of the Grohe ONDUS REST service. The structure of the REST elements
looks something like the following (a list of Locations, which contains a list of rooms, which
contains a list of appliances):

````
+-- Location
|   +-- Room
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   +-- Room
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
+-- Location
|   +-- Room
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   +-- Room
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
|   |   +-- Appliance
|   |   |   +-- ApplianceData
|   |   |   +-- ApplianceCommand
````

Where Appliances actually are devices from Grohe, where device information can be accessed and
commands can be executed.

## Java API
The main entry point of this library is the OndusService class, which provides the public API of
this library. All other classes, except the models (which represent the data returned by the Grohe
API) should be considered as internal API and may therefore not be used in your code.

The first thing you need to do is to login with your Grohe username and password (logging in with
a JWT is being implemented):

````java
class Example {
    public static void main(String[] args) {
        OndusService.login("email@example.com", "YourStrongPassword");
    }
}
````

Which returns an instance of OndusService. Afterwards you can request information from the API,
such as the list of locations or rooms:

````java
class Example {
    public static void main(String[] args) {
        List<Location> locations = ondusService.getLocations();
        List<Room> rooms = ondusService.getRooms(locations.get(0));
        
        System.out.println(rooms.get(0).getName());
    }
}
````
* [JavaDoc](https://floriansw.github.io/grohe-ondus-api-java/org/grohe/ondus/api/OndusService.html)
