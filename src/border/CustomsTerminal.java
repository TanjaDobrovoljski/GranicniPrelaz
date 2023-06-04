package border;

import Vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class CustomsTerminal extends Terminal {
    public CustomsTerminal(int terminalID, boolean trucks, Queue<Vehicle> vehicles) {
        super(terminalID,trucks,vehicles);
        this.component=new JPanel();
        this.component.setBackground(Color.magenta);
    }

}

