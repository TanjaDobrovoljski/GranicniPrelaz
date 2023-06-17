package border;

import Vehicle.*;
import sample.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class PoliceTerminal extends Terminal {
   private Queue<Vehicle> tempQueue = new LinkedList<>();

    public PoliceTerminal(int terminalID, boolean trucks, Queue<Vehicle> vehicles) {
        super(terminalID, trucks, vehicles);
        this.component = new JPanel();
        this.component.setBackground(Color.blue);
    }

    @Override
    public void run() {
        while (!Simulation.vehicleQueue.isEmpty()) {
            Vehicle vehicle;
            synchronized (Simulation.vehicleQueue) {
                vehicle = Simulation.vehicleQueue.poll();

            }
           // System.out.println("Position in police: " + Simulation.position);
            if (vehicle != null) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (vehicle) {
                    if (this.isOnlyForTrucks()) {
                        if (vehicle instanceof Truck) {
                            System.out.println("element u policiji : "+vehicle);

                            processTruck((Truck) vehicle);
                        } else {
                            System.out.println("velicina niza je "+Simulation.vehicleQueue.size());

                            // Iterate through the vehicleQueue to find the first truck
                            Vehicle truck = Simulation.vehicleQueue.stream()
                                    .filter(v -> v instanceof Truck)
                                    .findFirst()
                                    .orElse(null);
                            if (truck != null) {
                                // Remove the truck from the vehicleQueue and process it


                                processTruck((Truck) truck);
                            }
                        }
                    } else {
                        if (vehicle instanceof Bus || vehicle instanceof Car) {
                            processVehicle(vehicle);
                        }
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Police Terminal finished processing all vehicles.");
    }


   /* @Override
    public void run() {
        int i=0;
        while (true) {
            Vehicle vehicle;

            synchronized (Simulation.vehicleQueue) {
                vehicle = Simulation.vehicleQueue.peek();
            }

            if (vehicle == null) {
                break;
            }

            if (this.isOnlyForTrucks()) {
                if (vehicle instanceof Truck) {
                    synchronized (Simulation.vehicleQueue) {
                        vehicle = Simulation.vehicleQueue.poll();
                        Simulation.position++;
                    }
                    processTruck((Truck) vehicle);

                } else {
                    Vehicle nextVehicle;
                    synchronized (Simulation.vehicleQueue) {

                         nextVehicle = Simulation.vehicleQueue.stream()
                                .filter(v -> v instanceof Truck)
                                .findFirst()
                                .orElse(null);
                    }
                        if (nextVehicle != null) {
                            processTruck((Truck) nextVehicle);
                            Simulation.position++;

                            }

                }
            } else {
                if (vehicle instanceof Car || vehicle instanceof Bus) {
                    synchronized (Simulation.vehicleQueue) {
                        vehicle = Simulation.vehicleQueue.poll();
                        Simulation.position++;
                    }
                    processVehicle(vehicle);
                } else {
                    Vehicle nextVehicle;
                    synchronized (this) {

                        nextVehicle = Simulation.vehicleQueue.stream()
                                .filter(v -> v instanceof Car || v instanceof Bus)
                                .findFirst()
                                .orElse(null);
                        System.out.println("pozicija je "+nextVehicle.getPositionX()+" "+nextVehicle.getPositionY());
                    }
                        if (nextVehicle != null) {
                            processVehicle(nextVehicle);

                            Simulation.position++;


                        }

                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Police Terminal finished processing all vehicles.");
    }

*/



    private  void processVehicle(Vehicle vehicle) {
        int x=vehicle.getPositionX();
        int y=vehicle.getPositionY();

        repaintVehicle(vehicle, this.getX(), this.getY());
        Simulation.removeElement(vehicle);

    }

    private void processTruck(Truck truck) {

        int y=truck.getPositionY();
        Simulation.position++;
        repaintVehicle(truck, this.getX(), this.getY());
       // move(y);
        Simulation.removeElement(truck);

    }

    public synchronized void repaintVehicle(Vehicle v, int x, int y) {

        System.out.println("repaint");
       int x1= v.getPositionX();
        int y1=v.getPositionY();

        v.setPositionX(x);
        v.setPositionY(y);

        Simulation.getButtons()[x][y].remove(v.getComponent());
        Simulation.getButtons()[x][y].add(v.getComponent());

        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Simulation.removeVehicle(v.getPositionX(), v.getPositionY());
        this.setX(x);
        this.setY(y);
Simulation.getButtons()[v.getPositionX()][v.getPositionY()].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void move(int yy) {


        Simulation.removeVehicles();
     //   Simulation.copyArray();
        System.out.println("dio1");
        Simulation.vehicleQueue.stream()
                .skip(yy) // Skip the first 3 elements (starting from position 3)
                .forEach(vehicle -> {
                    int newY = vehicle.getPositionY() + 1; // Modify the Y coordinate as desired
                    vehicle.setPositionY(newY);
                });
        System.out.println("dio2");
        for(int row=3;Simulation.vehicleQueue.size()>0 ;)
        {

            Vehicle v=Simulation.vehicleQueue.poll();
            Simulation.getButtons()[row][v.getPositionY()].add(v.getComponent());
            Simulation.borderField.repaint();
            Simulation.borderField.revalidate();

        }
      //  Simulation.copyArray();
        }
}
