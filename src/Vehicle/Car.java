package Vehicle;

import Passenger.Passenger;
import sample.Simulation;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Car extends Vehicle{



    public Car(int passengerCount, List<Passenger> list) throws TooManyPassengersException {
        super(passengerCount,list);
        if(getPassengerCount()>5)
            throw new TooManyPassengersException("U automobilu moze biti maksimalno 5 putnika!");

        this.component=new JPanel();
        this.component.setBackground(Color.red);

        //this.vehicleColor=new Color(255, 0, 0);
    }

    @Override
    public void processToCustom()
    {
System.out.println("ovo je auto");
    }

}
