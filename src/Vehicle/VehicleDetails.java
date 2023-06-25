package Vehicle;

import Passenger.*;

import javax.swing.*;

public class VehicleDetails extends JFrame {
    public JPanel mainPanel;
    private JLabel vehicleTitle;
    private JTextPane vehicleDetailsLabel;

    public VehicleDetails(Vehicle v)
    {
        super();
        setContentPane(mainPanel);
        setVisible(true);
        pack();
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        vehicleTitle.setText(v.getClass().getName()+" "+v.getId());
        String s=" ";
        for (Passenger p:v.getPassengerList()
             ) {
            s+=p.toString()+"\n";
        }
       vehicleDetailsLabel.setText(v.toString()+"\n"+s);

    }
}
