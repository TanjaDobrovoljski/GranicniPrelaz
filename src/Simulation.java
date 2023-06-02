import Passenger.Passenger;
import Vehicle.Car;
import Vehicle.Truck;
import tools.TooManyPassengersException;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public static void main(String[] args) throws TooManyPassengersException {
        Passenger p=new Passenger();
        List<Passenger> list=new ArrayList<Passenger>();
        list.add(p);
        Truck c=new Truck(2,list);
        c.calculateActualWeight(true);
        System.out.println(c.getDeclaredWeight()+" "+c.getActualWeight());

    }
}
