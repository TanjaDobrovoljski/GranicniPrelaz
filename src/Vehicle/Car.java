package Vehicle;

import Passenger.Passenger;
import tools.TooManyPassengersException;

import java.util.List;

public class Car extends Vehicle{

    public Car(int passengerCount, List<Passenger> list) throws TooManyPassengersException {
        super(passengerCount,list);
        if(getPassengerCount()>5)
            throw new TooManyPassengersException("U automobilu moze biti maksimalno 5 putnika!");
    }

}
