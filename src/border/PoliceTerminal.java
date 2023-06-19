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
                vehicle = Simulation.vehicleQueue.peek();

            }
            if (vehicle == null) {
                break;
            }
            // System.out.println("Position in police: " + Simulation.position);
            if (vehicle != null) {

                if (this.isOnlyForTrucks()) {
                    if (vehicle instanceof Truck) {

                        synchronized (Simulation.vehicleQueue) {
                            vehicle = Simulation.vehicleQueue.poll();

                        }
                        processTruck((Truck) vehicle);
                    } else {
                        Vehicle truck;
                        synchronized (Simulation.vehicleQueue) {


                            truck = Simulation.vehicleQueue.stream()
                                    .filter(v -> v instanceof Truck)
                                    .findFirst()
                                    .orElse(null);
                        }
                        if (truck != null) {
                            processTruck((Truck) truck);
                        }
                    }
                } else {
                    if (vehicle instanceof Bus || vehicle instanceof Car) {

                        synchronized (Simulation.vehicleQueue) {
                            vehicle = Simulation.vehicleQueue.poll();


                        }

                        processVehicle(vehicle);
                    } else {
                        Vehicle vehicle1;

                        synchronized (Simulation.vehicleQueue) {
                            vehicle1 = Simulation.vehicleQueue.stream()
                                    .filter(v -> v instanceof Car || v instanceof Bus)
                                    .findFirst()
                                    .orElse(null);
                        }
                        if (vehicle1 != null) {
                            processVehicle(vehicle1);
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

        Simulation.position++;

        Simulation.removeElement(vehicle);
        repaintVehicle(vehicle, this.getX(), this.getY());



    }

    private void processTruck(Truck truck) {

        Simulation.position++;
        Simulation.removeElement(truck);
        repaintVehicle(truck, this.getX(), this.getY());


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


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
       // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

Simulation.getButtons()[v.getPositionX()][v.getPositionY()].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();



        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
