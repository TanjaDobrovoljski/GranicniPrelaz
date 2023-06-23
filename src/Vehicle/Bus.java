package Vehicle;

import Passenger.Passenger;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Bus extends Vehicle{
    private int luggageCapacity;
    private int numbOfForbiddenLuggage;

    @Override
    public String toString() {
        return super.toString()+" Bus{" +
                "luggageCapacity=" + luggageCapacity +
                '}';
    }

    public Bus(int passengerCount, List<Passenger> lista, int luggageCapacity) throws TooManyPassengersException {
        super( passengerCount,lista);
        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = passengerCount;
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);

    }
    public Bus(int passengerCount) throws TooManyPassengersException {
        super( passengerCount);
        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = passengerCount;
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);


    }

    public Bus(List<Passenger> lista) throws TooManyPassengersException {
        super( lista);

        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = lista.size();
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);


    }

    public int getLuggageCapacity() {
        return luggageCapacity;
    }

    public void setLuggageCapacity(int luggageCapacity) {
        this.luggageCapacity = luggageCapacity;
    }

    @Override
    public void processToCustom()
    {
        System.out.println("ovo je bus");

    }
}
