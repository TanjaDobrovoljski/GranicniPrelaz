package border;

import Vehicle.*;
import sample.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class PoliceTerminal extends Terminal {
    private Queue<Vehicle> vehicles;
   private Queue<Vehicle> tempQueue = new LinkedList<>();
    private static final Object positionLock = new Object();
    private final double personalVehicleProcessingTimePerPerson = 0.5; // in seconds
   private final double truckProcessingTimePerPerson = 0.5; // in seconds
    private CustomsTerminal c;

    public CustomsTerminal getC() {
        return c;
    }

    public void setC(CustomsTerminal c) {
        this.c = c;
    }

    // Processing time for passengers in buses
    private final double busProcessingTimePerPerson = 0.1;


    public PoliceTerminal(int terminalID, boolean trucks, Queue<Vehicle> vehicles) {
        super(terminalID, trucks);
        this.vehicles=vehicles;
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
                        Vehicle truck = null;
                        synchronized (Simulation.vehicleQueue) {
                            try{
                            truck = Simulation.vehicleQueue.stream()
                                    .filter(v -> v instanceof Truck)
                                    .findFirst()
                                    .orElse(null);
                            } catch (ConcurrentModificationException e) {
                                // Handle the exception
                                System.err.println("Concurrent modification detected: " + e.getMessage());
                                // Additional error handling or logging can be performed here
                            }
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


                      /*  synchronized (customsTerminal) {
                            if (customsTerminal.isFree()) {
                                customsTerminal.processVehicle(vehicle);
                                if (this.c.getState() == Thread.State.NEW) {
                                    this.c.start();
                                }
                            }else {

                                try {
                                    customsTerminal.wait(); // Thread waits until notified
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                customsTerminal.processVehicle(vehicle);
                            }
                        }*/


                    } else {
                        Vehicle vehicle1 = null;

                        synchronized (Simulation.vehicleQueue) {
                            try {
                                vehicle1 = Simulation.vehicleQueue.stream()
                                        .filter(v -> v instanceof Car || v instanceof Bus)
                                        .findFirst()
                                        .orElse(null);
                            } catch (ConcurrentModificationException e) {
                                // Handle the exception
                                System.err.println("Concurrent modification detected: " + e.getMessage());
                                // Additional error handling or logging can be performed here
                            }
                        }
                        if (vehicle1 != null) {
                            processVehicle(vehicle1);

                          /*  synchronized (customsTerminal) {
                                if (customsTerminal.isFree()) {
                                    customsTerminal.processVehicle(vehicle1);
                                    if (this.c.getState() == Thread.State.NEW) {
                                        this.c.start();
                                    }
                                }
                                else {

                                            try {
                                                customsTerminal.wait(); // Thread waits until notified
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        customsTerminal.processVehicle(vehicle1);
                                    }
                                }
                            }*/
                        }

                    }
                }
            }
        }

        System.out.println(this.getName()+" Police Terminal finished processing all vehicles.");
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

        synchronized (positionLock) {
            Simulation.position++;
        }

        Simulation.removeElement(vehicle);
        repaintVehicle(vehicle, this.getX(), this.getY());



    }

    private void processTruck(Truck truck) {

        synchronized (positionLock) {
            Simulation.position++;
        }

        Simulation.removeElement(truck);
        repaintVehicle(truck, this.getX(), this.getY());


    }

    public synchronized void repaintVehicle(Vehicle v, int x, int y) {

        CustomsTerminal customsTerminal = this.c;
       int x1= v.getPositionX();
        int y1=v.getPositionY();

        v.setPositionX(x);
        v.setPositionY(y);

        Simulation.getButtons()[x][y].remove(this.getComponent());
        Simulation.getButtons()[x][y].add(v.getComponent());

        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();

System.out.println(this.getName()+" prva obrada "+v);
        if(v instanceof Bus)
        {
            try {
                Thread.sleep((long) (busProcessingTimePerPerson*v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (customsTerminal) {
                if (customsTerminal.isFree()) {
                    customsTerminal.processVehicle(v);
                    if (this.c.getState() == Thread.State.NEW) {
                        this.c.start();
                    }
                }else {

                    try {
                        customsTerminal.wait(); // Thread waits until notified
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    customsTerminal.processVehicle(v);
                }
            }

        }
        else if (v instanceof Car){
            try {
                Thread.sleep((long) (personalVehicleProcessingTimePerPerson*v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (customsTerminal) {
                if (customsTerminal.isFree()) {
                    customsTerminal.processVehicle(v);
                    if (this.c.getState() == Thread.State.NEW) {
                        this.c.start();
                    }
                }else {

                    try {
                        customsTerminal.wait(); // Thread waits until notified
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    customsTerminal.processVehicle(v);
                }
            }

        }
        else if (v instanceof Truck) {
            try {
                Thread.sleep((long) (truckProcessingTimePerPerson * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
       // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

Simulation.getButtons()[v.getPositionX()][v.getPositionY()].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();



        if(v instanceof Bus)
        {
            try {
                Thread.sleep((long) (busProcessingTimePerPerson*v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Thread.sleep((long) (personalVehicleProcessingTimePerPerson*v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
