package Vehicle;

import Passenger.Passenger;
import sample.Simulation;
import tools.FW;
import tools.GenLogger;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Bus extends Vehicle{
    private int luggageCapacity;
    private int numbOfForbiddenLuggage;

    @Override
    public String toString() {
        return "Bus "+this.getId();
    }

    public Bus(int passengerCount, List<Passenger> lista, int luggageCapacity) throws TooManyPassengersException {
        super( passengerCount,lista);
        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = passengerCount;
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);
        checkForUnallowedLuggage();

    }
    public Bus(int passengerCount) throws TooManyPassengersException {
        super( passengerCount);
        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = passengerCount;
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);
        checkForUnallowedLuggage();


    }

    public Bus(List<Passenger> lista) throws TooManyPassengersException {
        super( lista);

        if(getPassengerCount()>52)
            throw new TooManyPassengersException("U autobusu moze biti maksimalno 52 putnika!");
        this.luggageCapacity = lista.size();
        this.component=new JPanel();
        this.component.setBackground(Color.yellow);
        checkForUnallowedLuggage();


    }

    private void checkForUnallowedLuggage()
    {
        List<Passenger> lista=this.getPassengerList();
        int i=(lista.size()*10)/100;

        if(i!=0) {
            int tmp = 0;
            Random rand = new Random();

            for (int j = 0; j < i; ) {
                int k = rand.nextInt(i);
                if (lista.get(k).gethasUnallowedItemsInLuggage() == false) {
                    lista.get(k).setHasUnallowedItems(true);
                    j++;
                }

            }
        }
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

       List<Passenger> lista= new ArrayList<>(this.getPassengerList());
        Iterator<Passenger> iterator = lista.iterator();

        String s=":";



        while(iterator.hasNext())
        {
            Passenger p = iterator.next();

            if(p.gethasUnallowedItemsInLuggage())
            {
                  iterator.remove();
                  s+=p.toString()+" "+Simulation.razlogPrtljag+"\n";
            }
        }
        this.setPassengerList(lista);
        try {
            BufferedWriter bf=new BufferedWriter(new FileWriter(Simulation.file2.getName(),true));
            bf.write("+ "+this +" je presao granicu ali navedeni putnici nisu! Razlog: \n"+s);
            bf.close();
        } catch (IOException e) {
            GenLogger.log(Bus.class,e);
        }

    }
}
