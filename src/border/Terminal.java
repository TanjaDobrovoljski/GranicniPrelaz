package border;

import Vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Terminal extends Thread{
    private int terminalID;
    private Queue<Vehicle> vehicles;
    private boolean onlyForTrucks;
    protected Color terminalColor;
    protected JPanel component;

    public JPanel getComponent() {
        return component;
    }

    public void setComponent(JPanel component) {
        this.component = component;
    }

    public Terminal(int terminalID, boolean onlyForTrucks, Queue<Vehicle> vehicles) {
        this.terminalID = terminalID;
        this.onlyForTrucks=onlyForTrucks;
        this.vehicles =vehicles;
    }

    public Color getTerminalColor() {
        return terminalColor;
    }

    public void setTerminalColor(Color terminalColor) {
        this.terminalColor = terminalColor;
    }

    public int getTerminalID() {
        return terminalID;
    }


    public synchronized void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public synchronized boolean hasVehicles() {
        return !vehicles.isEmpty();
    }

    public synchronized Vehicle getNextVehicle() {
        return vehicles.poll();
    }


    @Override
    public void run()
    {}
    // Other common methods and attributes for the Terminal class
}
