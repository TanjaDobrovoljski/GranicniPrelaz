package Vehicle;

import Passenger.Passenger;

import java.util.List;

public abstract class Vehicle {

    private int passengerCount=1;
    List<Passenger> passengerList;

    public Vehicle( int passengerCount,List<Passenger> list) {

        this.passengerCount += passengerCount;
        this.passengerList=list;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
}
