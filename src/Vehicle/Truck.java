package Vehicle;

import Passenger.Passenger;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Truck extends Vehicle{
    private boolean needsDocumentation;
    private double declaredWeight;
    private double actualWeight;
    private final static int MAX_WEIGHT=12000,MIN_WEIGHT=1000;
    private String documentation;

    @Override
    public String toString() {
        return super.toString()+" Truck{" +
                "documentation=" + documentation +
                ", declaredWeight=" + declaredWeight +
                ", actualWeight=" + actualWeight +
                '}';
    }

    public Truck(int passengerCount, List<Passenger> list) throws TooManyPassengersException {
        super(passengerCount, list);
        if(getPassengerCount()>3)
            throw new TooManyPassengersException("U kamionu moze biti maksimalno 3 putnika!");
        Random random = new Random();
        this.needsDocumentation= random.nextDouble()<=0.5;
        this.declaredWeight=random.nextDouble() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
        this.component=new JPanel();
        this.component.setBackground(Color.green);

    }

    public Truck( List<Passenger> list) throws TooManyPassengersException {
        super(list);
        if(getPassengerCount()>3)
            throw new TooManyPassengersException("U kamionu moze biti maksimalno 3 putnika!");
        Random random = new Random();
        this.needsDocumentation= random.nextDouble()<=0.5;
        this.declaredWeight=random.nextDouble() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
        this.component=new JPanel();
        this.component.setBackground(Color.green);


    }

    public void calculateActualWeight(boolean b)
    {
        if(b==true) {
            Random random=new Random();
            double percentage= random.nextDouble() * (30.0 - 0.1) + 0.1;

            this.actualWeight=this.declaredWeight+(this.declaredWeight*percentage)/100.0;
        }
        else
            this.actualWeight=this.declaredWeight;
    }

    public boolean isNeededDocumentation() {
        return needsDocumentation;
    }

    public void setNeedsDocumentation(boolean documentation) {
        this.needsDocumentation = documentation;
    }

    public double getDeclaredWeight() {
        return declaredWeight;
    }

    public void setDeclaredWeight(double declaredWeight) {
        this.declaredWeight = declaredWeight;
    }

    public double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Override
    public void processToCustom()
    {
        System.out.println("ovo je kamion");
        if(this.isNeededDocumentation())
        {
            UUID uuid = UUID.randomUUID();
            this.documentation=uuid.toString();
        }
    }
}
