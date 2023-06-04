package border;

import Vehicle.Vehicle;
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
    public void run()
    {
        while (!Simulation.vehicleQueue.isEmpty()) {
            Vehicle vehicle;
            synchronized (Simulation.vehicleQueue) {
                vehicle = Simulation.vehicleQueue.poll();
            }
                if (vehicle != null) {
                    //System.out.println("Processing vehicle: " + vehicle.toString());

                    // Simulate processing for 2 seconds
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Pass the vehicle to the customs terminal for further processing

                    Simulation.removeDiamond(vehicle.getPositionX(),vehicle.getPositionY());
                    Simulation.borderField.repaint();
                    Simulation.borderField.revalidate();
                    System.out.println(vehicle.getPositionY());

                    Simulation.movingVehicles(vehicle.getPositionX(),vehicle.getPositionY());

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


}

