package border;

import Vehicle.*;
import sample.Simulation;
import tools.GenLogger;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                    synchronized (this) {

                        repaintVehicle(this.vehicle, this.x, this.y);
                        vehicle.processToCustom();
                        if (vehicle instanceof Truck) {
                            if (((Truck) vehicle).getActualWeight() != ((Truck) vehicle).getDeclaredWeight()) {
                                try {
                                    BufferedWriter bf = new BufferedWriter(new FileWriter(Simulation.file2.getName(), true));
                                    synchronized (bf) {
                                        bf.write("- "+vehicle.toString() + " nije presao granicu! Razlog: " + Simulation.razlogKamion + "\n");
                                    }Simulation.move5.removeAll();
                                    Simulation.move5.setText("<html>Carinski terminal " + this.getTerminalID() + " obradjuje vozilo " + vehicle.toString() + "<br/>i izbacuje ga sa granice jer " + Simulation.razlogKamion+"</html>");

                                    bf.close();
                                } catch (IOException e) {
                                    GenLogger.log(CustomsTerminal.class,e);;
                                }


                            }
                            else
                            {
                                Simulation.move5.removeAll();
                                Simulation.move5.setText("<html>Carinski terminal " + this.getTerminalID() + " obradjuje vozilo " +  vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i prelazi granicu</html>");

                            }
                        }
                        else
                        {
                            Simulation.move4.removeAll();
                            Simulation.move4.setText("<html>Carinski terminal " + this.getTerminalID() + " obradjuje vozilo " +  vehicle.getClass().getSimpleName()+vehicle.getId() + "<br/>i prelazi granicu</html>");

                        }
                        vehicle = null;
                        isFree = true;


                        notify();

                    }
                }

                try {
                    Thread.sleep(1000);// Add a short delay before checking for the next vehicle
                } catch (InterruptedException e) {
                    GenLogger.log(CustomsTerminal.class,e);;
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
                GenLogger.log(CustomsTerminal.class,e);;
            }
        }
        else if (v instanceof Car) {
            try {
                Thread.sleep((long) (2000));
            } catch (InterruptedException e) {
                GenLogger.log(CustomsTerminal.class,e);
            }
        }
        else if (v instanceof Truck) {
            try {
                Thread.sleep((long) (getTruckProcessingTimePerPerson() * v.getPassengerCount() * 1000));
            } catch (InterruptedException e) {
                GenLogger.log(CustomsTerminal.class,e);
            }
        }

        Simulation.getButtons()[x][y].remove(v.getComponent());
        // Simulation.removeVehicle(v.getPositionX(), v.getPositionY());

        Simulation.getButtons()[x][y].add(this.getComponent());
        Simulation.borderField.repaint();
        Simulation.borderField.revalidate();



    }

}

