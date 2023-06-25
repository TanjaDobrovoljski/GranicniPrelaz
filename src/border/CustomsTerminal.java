package border;

import Vehicle.*;
import sample.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class CustomsTerminal extends Terminal {
    private Vehicle vehicle;
    private boolean isFree;

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public CustomsTerminal( boolean trucks) {
        super(trucks);
        this.component=new JPanel();
        this.component.setBackground(Color.magenta);
        this.isFree=true;
    }

    public void processVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isFree = false;

    }

    @Override
    public void run() {

        while (true) {
            if (!isFree) {
                // Process the vehicle at the customs terminal
                synchronized (this)
                {

                repaintVehicle(this.vehicle, this.x, this.y);
                vehicle.processToCustom();
                if(vehicle instanceof Truck)
                {
                    if(((Truck) vehicle).getActualWeight()!= ((Truck) vehicle).getDeclaredWeight())
                    {
                        System.out.println("kamion ne moze da predje granicu!");
                    }
                }
                vehicle = null;
                isFree = true;


                notify();

            }
        }

            try{
                Thread.sleep(1000);// Add a short delay before checking for the next vehicle
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void repaintVehicle(Vehicle v, int x, int y) {


        int x1= v.getPositionX();
        int y1=v.getPositionY();

        v.setPositionX(x);
        v.setPositionY(y);

        Simulation.getButtons()[x][y].remove(this.getComponent());
        Simulation.getButtons()[x][y].add(v.getComponent());

        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();


        if(v instanceof Bus)
        {
            try {
                Thread.sleep((long) (getBusProcessingTimePerPerson()*v.getPassengerCount()*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (v instanceof Car) {
            try {
                Thread.sleep((long) (2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (v instanceof Truck) {
            try {
                Thread.sleep((long) (getTruckProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
        // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

        Simulation.getButtons()[x][y].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();



    }

}

