package border;

import Passenger.Passenger;
import Vehicle.*;
import sample.Simulation;
import tools.GenLogger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class PoliceTerminal extends Terminal {
    private Queue<Vehicle> vehicles;
   private Queue<Vehicle> tempQueue = new LinkedList<>();
    private static final Object positionLock = new Object();
    private List<Passenger> passengersWithIncorrectDocuments;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-HH_mm_ss_dd.MM.yyyy");




    private CustomsTerminal c;

    public CustomsTerminal getC() {
        return c;
    }

    public void setC(CustomsTerminal c) {
        this.c = c;
    }

    // Processing time for passengers in buses



    public PoliceTerminal(boolean trucks, Queue<Vehicle> vehicles) {
        super( trucks);
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
                                try {
                                    truck = Simulation.vehicleQueue.stream()
                                            .filter(v -> v instanceof Truck)
                                            .findFirst()
                                            .orElse(null);
                                } catch (ConcurrentModificationException e) {
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

                            if (vehicle != null)
                                processVehicle(vehicle);

                        } else {
                            Vehicle vehicle1 = null;

                            synchronized (Simulation.vehicleQueue) {
                                try {
                                    vehicle1 = Simulation.vehicleQueue.stream()
                                            .filter(v -> v instanceof Car || v instanceof Bus)
                                            .findFirst()
                                            .orElse(null);
                                } catch (ConcurrentModificationException e) {
                                }
                            }
                            if (vehicle1 != null) {
                                processVehicle(vehicle1);
                            }
                        }
                    }
                }
            }

        System.out.println(this.getName()+" Police Terminal finished processing all vehicles.");

    }


    private  void processVehicle(Vehicle vehicle) {

          synchronized (positionLock) {
            Simulation.position++;
            checkPassengers(vehicle);

        }

        if(!isDriverInvalid) {
            Simulation.removeElement(vehicle);
            if(this.getTerminalID()==0) {
                Simulation.move1.removeAll();
                Simulation.move1.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo " +  vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i procesira dalje na carinski terminal</html>");
            }
            if(this.getTerminalID()==1) {
                Simulation.move2.removeAll();
                Simulation.move2.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo " +  vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i procesira dalje na carinski terminal</html>");
            }

            repaintVehicle(vehicle, this.getX(), this.getY());

        }
        else
        {
            Simulation.removeElement(vehicle);
            if(this.getTerminalID()==0) {
                Simulation.move1.removeAll();
                Simulation.move1.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo " + vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i izbacuje ga sa granice jer " + Simulation.razlogVozac+"</html>");
            }
            if(this.getTerminalID()==1) {
                Simulation.move2.removeAll();
                Simulation.move2.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo " +  vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i izbacuje ga sa granice jer " + Simulation.razlogVozac+"</html>");
            }

            abbortVehicle(vehicle,this.getX(),this.getY());
        }




    }

    private void processTruck(Truck truck) {

        synchronized (positionLock) {
            Simulation.position++;
            checkPassengers(truck);
        }
        if(!isDriverInvalid) {
            Simulation.removeElement(truck);
            if(this.getTerminalID()==2) {
                Simulation.move3.removeAll();
                Simulation.move3.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo<br/>" + truck.toString() + "<br/>i procesira dalje na carinski terminal</html>");
            }
            repaintVehicle(truck, this.getX(), this.getY());
        }
        else
        {
            Simulation.removeElement(truck);
            if(this.getTerminalID()==2) {
                Simulation.move3.removeAll();
                Simulation.move3.setText("<html>Policijski terminal " + this.getTerminalID() + " obradjuje vozilo<br/>" + truck.toString() + "<br/>iizbacuje ga sa granice jer " + Simulation.razlogVozac+"</html>");
            }
            abbortVehicle(truck,this.getX(),this.getY());
        }

    }

    public synchronized void repaintVehicle(Vehicle v, int x, int y) {

        CustomsTerminal customsTerminal = this.c;
        int x1 = v.getPositionX();
        int y1 = v.getPositionY();

        v.setPositionX(x);
        v.setPositionY(y);

        Simulation.getButtons()[x][y].remove(this.getComponent());
        Simulation.getButtons()[x][y].add(v.getComponent());

        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();

        if (v instanceof Bus) {
            try {
                Thread.sleep((long) (getBusProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

            synchronized (customsTerminal) {
                if (customsTerminal.isFree()) {
                    customsTerminal.processVehicle(v);
                    if (this.c.getState() == Thread.State.NEW && !Simulation.terminalsMap.get(this.c.getTerminalID()).getStatus().equals("blocked")) {
                        this.c.start();
                    }
                } else {

                    try {
                        customsTerminal.wait(); // Thread waits until notified
                    } catch (InterruptedException e) {
                        GenLogger.log(PoliceTerminal.class,e);
                    }

                    customsTerminal.processVehicle(v);
                }
            }

        } else if (v instanceof Car) {
            try {
                Thread.sleep((long) (getPersonalVehicleProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

            synchronized (customsTerminal) {
                if (customsTerminal.isFree()) {
                    customsTerminal.processVehicle(v);
                    if (this.c.getState() == Thread.State.NEW && !Simulation.terminalsMap.get(this.c.getTerminalID()).getStatus().equals("blocked"))
                    {

                        this.c.start();
                    }
                } else {

                    try {
                        customsTerminal.wait(); // Thread waits until notified
                    } catch (InterruptedException e) {
                        GenLogger.log(PoliceTerminal.class,e);
                    }

                    customsTerminal.processVehicle(v);
                }
            }

        } else if (v instanceof Truck) {
            try {
                Thread.sleep((long) (getTruckProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

            synchronized (customsTerminal) {
                if (customsTerminal.isFree()) {
                    customsTerminal.processVehicle(v);
                    if (this.c.getState() == Thread.State.NEW && !Simulation.terminalsMap.get(this.c.getTerminalID()).getStatus().equals("blocked")) {
                        this.c.start();
                    }
                } else {

                    try {
                        customsTerminal.wait(); // Thread waits until notified
                    } catch (InterruptedException e) {
                        GenLogger.log(PoliceTerminal.class,e);
                    }

                    customsTerminal.processVehicle(v);
                }
            }
        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
        // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

        Simulation.getButtons()[v.getPositionX()][v.getPositionY()].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();

    }

    private boolean isDriverInvalid=false;

    private synchronized void checkPassengers(Vehicle v)
    {
        isDriverInvalid=false;
          List<Passenger> tmp=new ArrayList<>(v.getPassengerList());
        passengersWithIncorrectDocuments=new ArrayList<>();

        Iterator<Passenger> iterator = tmp.iterator();

        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            if (!p.isHasValidDocuments()) {
                if(p.isDriver()) {
                    isDriverInvalid = true;

                }
                passengersWithIncorrectDocuments.add(p);
                iterator.remove(); // Safely remove the element using the iterator
            }
        }
        v.setPassengerList(tmp);

            if(!passengersWithIncorrectDocuments.isEmpty())
            {
                try {
                    if(!Simulation.file1.exists() )
                        Simulation.file1.createNewFile();
                    if(!Simulation.file2.exists())
                        Simulation.file2.createNewFile();

                    BufferedWriter bf=new BufferedWriter(new FileWriter(Simulation.file2.getName(),true));
                    FileOutputStream fileOutputStream = new FileOutputStream(Simulation.file1.getName(),true);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    synchronized (bf ){
                    for (Passenger p:passengersWithIncorrectDocuments
                         ) {
                        if(isDriverInvalid)
                        bf.write("- "+v.toString()+" nije presao granicu! Razlog: "+Simulation.razlogVozac+"\n");
                        else
                            bf.write("+ "+v.toString()+" je presao granicu ali navedeni putnici nisu! Razlog: "+p.toString()+" "+Simulation.razlogPutnik+"\n");
                        objectOutputStream.writeObject(p);
                    }}
                    objectOutputStream.close();
                    fileOutputStream.close();
                   bf.close();

                    System.out.println("Lista kažnjenih osoba je uspešno serijalizovana.");
                } catch (Exception e) {
                    GenLogger.log(PoliceTerminal.class,e);
                }
            }

    }

    public synchronized void abbortVehicle(Vehicle v, int x, int y) {

        Simulation.getButtons()[x][y].remove(this.getComponent());
        Simulation.getButtons()[x][y].add(v.getComponent());

        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();

        if (v instanceof Bus) {
            try {
                Thread.sleep((long) (getBusProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

        } else if (v instanceof Car) {
            try {
                Thread.sleep((long) (getPersonalVehicleProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

        } else if (v instanceof Truck) {
            try {
                Thread.sleep((long) (getTruckProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(PoliceTerminal.class,e);
            }

        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
        // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

        Simulation.getButtons()[x][y].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();


    }

}
