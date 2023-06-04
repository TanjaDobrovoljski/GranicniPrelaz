package Vehicle;

import Passenger.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Vehicle {

    protected Color vehicleColor;
    protected JPanel component;
    private int position;
    protected int passengerCount=1;
    List<Passenger> passengerList;
    private Integer positionX;
    private Integer positionY;

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public JPanel getComponent() {
        return component;
    }

    public void setComponent(JPanel component) {
        this.component = component;
    }

    public Vehicle(int passengerCount, List<Passenger> list) {

        this.passengerCount += passengerCount;
        this.passengerList=list;

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Color getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(Color vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }


}
