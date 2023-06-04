package Vehicle;

import Passenger.Passenger;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Bus extends Vehicle{
    private int luggageCapacity;

    public Bus(int passengerCount, List<Passenger> lista, int luggageCapacity) throws TooManyPassengersException {
        super( passengerCount,lista);
        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = luggageCapacity;
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);

    }

    public int getLuggageCapacity() {
        return luggageCapacity;
    }

    public void setLuggageCapacity(int luggageCapacity) {
        this.luggageCapacity = luggageCapacity;
    }
}
