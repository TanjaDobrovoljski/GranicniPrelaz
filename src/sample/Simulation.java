package sample;

import Passenger.Passenger;
import Vehicle.Bus;
import Vehicle.Car;
import Vehicle.*;
import border.CustomsTerminal;
import border.PoliceTerminal;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {

    private JPanel mainPanel;
    private JPanel middle;
    private JButton pauseButton;
    private JButton restartButton;
    private JLabel simulationTime;
    private AtomicBoolean isPaused;
    public static  Queue<Vehicle> vehicleQueue=new LinkedList<Vehicle>();

    private static JButton[][] buttons;
    public static JPanel borderField;

    public static JButton[][] getButtons() {
        return buttons;
    }

    public void setButtons(JButton[][] buttons) {
        this.buttons = buttons;
    }

    public static JPanel getBorderField() {
        return borderField;
    }

    public static void setBorderField(JPanel borderField) {
        Simulation.borderField = borderField;
    }

    public Simulation() throws TooManyPassengersException {
        isPaused=new AtomicBoolean(false);
        borderField = middle;
        int rows=7,columns=55;
        borderField.setLayout(new GridLayout(rows, columns));


        buttons = new JButton[rows][columns];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {

                buttons[x][y] = new JButton();

                buttons[x][y].setEnabled(false);

                buttons[x][y].setPreferredSize(new Dimension(50, 50));

                buttons[x][y].setMargin(new Insets(0, 0, 0, 0));
                borderField.add(buttons[x][y]);
            }
            borderField.setEnabled(false);
        }



        initializeVehicles();

       initilizeTerminals();

    }

    public static void main(String args[]) throws TooManyPassengersException {

        Simulation application = new Simulation();
        JFrame frame = new JFrame("Border Crossing");


        frame.setVisible(true);
        frame.setContentPane(application.mainPanel);


        frame.pack();
        frame.setSize(1400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




    }

    public void initializeVehicles() throws TooManyPassengersException {
        Passenger p=new Passenger();
        List<Passenger> lista=new ArrayList<Passenger>();
        lista.add(p);

       for (int i = 0; i < 5; i++) {
            Bus bus = new Bus(lista.size(),lista,0);
            vehicleQueue.add(bus);
        }

        // Generate 10 trucks and add them to the queue
        for (int i = 0; i < 10; i++) {
            Truck truck = new Truck(0,lista);
            vehicleQueue.add(truck);
        }

        // Generate 35 cars and add them to the queue
        for (int i = 0; i < 35; i++) {
            Car car = new Car(0,lista);

            vehicleQueue.add(car);
        }

        // Shuffle the queue to randomize the order of vehicles
        List<Vehicle> vehicleList = new ArrayList<>(vehicleQueue);
        Collections.shuffle(vehicleList);
        vehicleQueue = new LinkedList<>(vehicleList);

        // Assign random positions to each vehicle in the queue
        Random random = new Random();
        int maxPosition = 50; // Assuming there are 50 positions in the queue


        int size=vehicleQueue.size();
        for(int row=3,column=49;column>=0;column--)
        {
            Vehicle v=vehicleQueue.peek();
            buttons[row][column].add(v.getComponent());
            v.setPositionX(row);
            v.setPositionY(column);
            vehicleQueue.add(vehicleQueue.poll());
        }

    }

    public void initilizeTerminals()
    {
        PoliceTerminal p1=new PoliceTerminal(1,false,vehicleQueue);
        PoliceTerminal p2=new PoliceTerminal(2,false,vehicleQueue);
        PoliceTerminal pK=new PoliceTerminal(3,true,vehicleQueue);

        CustomsTerminal c=new CustomsTerminal(1,false,vehicleQueue);
        CustomsTerminal cK=new CustomsTerminal(2,true,vehicleQueue);

        buttons[1][51].add(p1.getComponent());
        buttons[3][51].add(p2.getComponent());
        buttons[5][51].add(pK.getComponent());

        buttons[1][53].add(c.getComponent());
        buttons[5][53].add(cK.getComponent());

        p1.start();

    }

    private static int position=1;
     public static void movingVehicles(int xx,int yy) {


         try {
             Thread.sleep(1000);
             removeVehicles();
             Thread.sleep(1000);

         } catch (InterruptedException e) {
             e.printStackTrace();
         }

         for(int row=3,column=49;column>=position;column--)
         {
             Vehicle v=vehicleQueue.peek();
             buttons[row][column].add(v.getComponent());
             v.setPositionX(row);
             v.setPositionY(column);
             vehicleQueue.add(vehicleQueue.poll());
         }
        position++;
        /*
        for (Vehicle vehicle : vehicleQueue) {
             int currentY = vehicle.getPositionY();
             System.out.println("u funkciji "+currentY+" "+vehicle.getClass().getSimpleName());

             removeDiamond(3,currentY);
            // buttons[3][currentY].remove(vehicle.getComponent());
             vehicle.setPositionY(yy); // Update the Y position

             // Update the button content with the updated vehicle

             buttons[3][yy].add(vehicle.getComponent());
         }

         // Clear the last button in the row
         removeDiamond(3,position);
        position++;*/


     }
    public static void removeVehicles() //refresh za duh figuru (uklanjanje dijamanata)
    {
        var list=buttons;
        for (var i: list) {
            for (var k:i) {
                var list2= k.getComponents();
                for (var m:list2)
                {
                    if (m.getBackground().equals(Color.red) || m.getBackground().equals(Color.green) ||m.getBackground().equals(Color.yellow))
                    { borderField.remove(m);
                        k.remove(m);

                    }
                }
            }
        }
    }
    public static void removeDiamond(int x,int y) //refresh za duh figuru (uklanjanje dijamanata)
    {
        var list2= buttons[x][y].getComponents();
        for (var m:list2)
        {
            if(buttons[x][y].getComponents().length>1)
            {
                if ((m.equals(buttons[x][y].getComponent(0)) || m.equals(buttons[x][y].getComponent(1))) )
                { borderField.remove(m);

                    buttons[x][y].remove(m);
                    borderField.revalidate();
                    borderField.repaint();
                    break;
                }
            }
            else if (m.equals(buttons[x][y].getComponent(0)))
            { borderField.remove(m);

                buttons[x][y].remove(m);
                borderField.revalidate();
                borderField.repaint();
                break;
            }
        }
    }
}
