package Vehicle;

import Passenger.Passenger;
import tools.TooManyPassengersException;

import java.util.List;
import java.util.Random;

public class Truck extends Vehicle{
    private boolean documentation;
    private double declaredWeight;
    private double actualWeight;
    private final static int MAX_WEIGHT=12000,MIN_WEIGHT=1000;

    public Truck(int passengerCount, List<Passenger> list) throws TooManyPassengersException {
        super(passengerCount, list);
        if(getPassengerCount()>3)
            throw new TooManyPassengersException("U kamionu moze biti maksimalno 3 putnika!");
        Random random = new Random();
        this.documentation= random.nextDouble()<=0.5;
        this.declaredWeight=random.nextDouble() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;

    }

    public void calculateActualWeight(boolean b)
    {
        if(b==true) {
            Random random=new Random();
            double percentage= random.nextDouble() * (30.0 - 0.1) + 0.1;

            System.out.println(percentage);
            this.actualWeight=this.declaredWeight+(this.declaredWeight*percentage)/100.0;
        }
        else
            this.actualWeight=this.declaredWeight;
    }

    public boolean isDocumentation() {
        return documentation;
    }

    public void setDocumentation(boolean documentation) {
        this.documentation = documentation;
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
}
