package border;

import Vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Terminal extends Thread{
    private int terminalID;

    private boolean onlyForTrucks;
    protected Color terminalColor;
    protected JPanel component;
    protected int x,y;
    private final double busProcessingTimePerPerson = 0.1;
    private final double personalVehicleProcessingTimePerPerson = 0.5; // in seconds
    private final double truckProcessingTimePerPerson = 0.5; // in seconds

    public double getBusProcessingTimePerPerson() {
        return busProcessingTimePerPerson;
    }

    public double getPersonalVehicleProcessingTimePerPerson() {
        return personalVehicleProcessingTimePerPerson;
    }

    public double getTruckProcessingTimePerPerson() {
        return truckProcessingTimePerPerson;
    }

    public boolean isOnlyForTrucks() {
        return onlyForTrucks;
    }

    public void setOnlyForTrucks(boolean onlyForTrucks) {
        this.onlyForTrucks = onlyForTrucks;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public JPanel getComponent() {
        return component;
    }

    public void setComponent(JPanel component) {
        this.component = component;
    }

    public Terminal(int terminalID, boolean onlyForTrucks) {
        this.terminalID = terminalID;
        this.onlyForTrucks=onlyForTrucks;

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

    @Override
    public void run()
    {}
    // Other common methods and attributes for the Terminal class
}
