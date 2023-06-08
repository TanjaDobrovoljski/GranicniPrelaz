package border;

import Vehicle.*;
import sample.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class PoliceTerminal extends Terminal {
    public PoliceTerminal(int terminalID, boolean trucks, Queue<Vehicle> vehicles) {
        super(terminalID,trucks,vehicles);
        this.component=new JPanel();
        this.component.setBackground(Color.blue);
    }

    @Override
    public  void run()
    {
        while (!Simulation.vehicleQueue.isEmpty()) {
            Vehicle vehicle;
            synchronized (Simulation.vehicleQueue) {
                vehicle = Simulation.vehicleQueue.poll();
                Simulation.position++;
            }
            System.out.println("position u policiji: "+Simulation.position);
            if (vehicle != null) {

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (vehicle) {
                    // Pass the vehicle to the customs terminal for further processing
                    if (this.isOnlyForTrucks() && vehicle instanceof Truck) {
                        processTruck((Truck) vehicle);
                    } else if (!this.isOnlyForTrucks() && (vehicle instanceof Bus || vehicle instanceof Car)) {
                        processVehicle(vehicle);

                    }
                }
            }


                    // Pause between processing vehicles
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Police Terminal finished processing all vehicles.");

            }


    private synchronized void processVehicle(Vehicle vehicle) {
        Simulation.removeVehicle(vehicle.getPositionX(), vehicle.getPositionY());
        repaintVehicle(vehicle, this.getX(), this.getY());
        Simulation.movingVehicles(vehicle.getPositionX(),vehicle.getPositionY());
    }

    private void processTruck(Truck truck) {
        Simulation.removeVehicle(truck.getPositionX(), truck.getPositionY());
        repaintVehicle(truck, this.getX(), this.getY());
        Simulation.movingVehicles(truck.getPositionX(),truck.getPositionY());

    }

    public synchronized void repaintVehicle(Vehicle v,int x,int y)
    {
        Simulation.removeVehicle(x,y);

        v.setPositionX(x);
        v.setPositionY(y);
        Simulation.getButtons()[x][y].add(v.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       Simulation.removeVehicle(v.getPositionX(),v.getPositionY());

        Simulation.getButtons()[v.getPositionX()][v.getPositionY()].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();

    }
}

