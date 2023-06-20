package Vehicle;

import Passenger.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public abstract class Vehicle {

    protected Color vehicleColor;
    protected JPanel component;
    private int position;
    protected int passengerCount=1;
    List<Passenger> passengerList;
    private Integer positionX;
    private Integer positionY;
    private  int id;
    private static int broj;

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
        this.broj++;
        this.id=broj;

    }

    public Vehicle(int passengerCount) {

        this.passengerCount += passengerCount;
        this.broj++;
        this.id=broj;

    }

    public  int getId() {
        return id;
    }

    public  void setId(int broj) {
        this.id = id;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {

            return false;
        }

        Vehicle other = (Vehicle) obj;

        return Objects.equals(this.vehicleColor, other.vehicleColor) &&
                Objects.equals(this.component, other.component) &&
                this.position == other.position &&
                this.passengerCount == other.passengerCount &&
                Objects.equals(this.passengerList, other.passengerList) &&
                Objects.equals(this.positionX, other.positionX) &&
                Objects.equals(this.positionY, other.positionY) &&
                this.id == other.id;
    }

    public abstract void processToCustom();

}
