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
    private static List<Vehicle> vehicleList;



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
        int k=1;
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
        vehicleList = new ArrayList<>(vehicleQueue);
        Collections.shuffle(vehicleList);
        vehicleQueue = new LinkedList<>(vehicleList);



        for(int row=3,column=49;column>=0;column--)
        {
            Vehicle v=vehicleQueue.poll();
            buttons[row][column].add(v.getComponent());

            v.setPositionX(row);
            v.setPositionY(column);

        }
        vehicleQueue.addAll(vehicleList);
System.out.println("lista na pocetku: ");
        for (Vehicle v:vehicleList
        ) {
            System.out.print(v+"("+v.getPositionX()+","+v.getPositionY()+") ");

        }
    }

    public synchronized static void copyArray()
    {
        vehicleQueue.clear();

        vehicleQueue.addAll(vehicleList);
        System.out.println("lista na kopiranju: ");
        for (Vehicle v:vehicleList
             ) {
            System.out.print(v.getClass().getSimpleName()+"("+v.getPositionX()+","+v.getPositionY()+") ");

        }


        System.out.println("red na kopiranju: ");
        for (Vehicle ve:vehicleQueue
        ) {
            System.out.print(ve+"("+ve.getPositionX()+","+ve.getPositionY()+") ");

        }



    }


    public void initilizeTerminals()
    {
        PoliceTerminal p1=new PoliceTerminal(1,false,vehicleQueue);
        p1.setX(1);
        p1.setY(51);
        PoliceTerminal p2=new PoliceTerminal(2,false,vehicleQueue);
        p2.setX(3);
        p2.setY(51);
        PoliceTerminal pK=new PoliceTerminal(3,true,vehicleQueue);
        pK.setX(5);
        pK.setY(51);


        CustomsTerminal c=new CustomsTerminal(1,false,vehicleQueue);
        c.setX(1);
        c.setY(53);
        CustomsTerminal cK=new CustomsTerminal(2,true,vehicleQueue);
        cK.setX(5);
        cK.setY(53);

        buttons[1][51].add(p1.getComponent());
        buttons[3][51].add(p2.getComponent());
        buttons[5][51].add(pK.getComponent());

        buttons[1][53].add(c.getComponent());
        buttons[5][53].add(cK.getComponent());

       // p1.start();
      //  p2.start();
       pK.start();

    }

    public static int position=0;

     public synchronized static void movingVehicles(int xx,int yy) {

         ArrayList < Vehicle > temp = new ArrayList <> ();
         removeVehicles();
         System.out.println(vehicleQueue.size());

         for(int row=3,column=49;vehicleQueue.size()>0 && column>=position;column--)
         {
             Vehicle v=vehicleQueue.peek();
             temp.add(v);
             vehicleQueue.remove();
             buttons[row][column].add(v.getComponent());
             v.setPositionX(row);
             v.setPositionY(column);
             borderField.repaint();
             borderField.revalidate();
             try{
                 Thread.sleep(500);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         }
         for (int i = 0; i < temp.size(); i++)
         {
             vehicleQueue.add(temp.get(i));
         }


/*
         System.out.println("moving");
         copyArray();
         removeVehicles();
         System.out.println("x==== " + xx + "y===== " + yy);
         Iterator<Vehicle> iterator = vehicleQueue.iterator();
         List<Vehicle> previousElements = new ArrayList<>();

         while (iterator.hasNext()) {
             Vehicle next = iterator.next();
             System.out.println("x= " + next.getPositionX() + "y= " + next.getPositionY());
             if ((next.getPositionX() == xx && next.getPositionY() == yy)) {
                 while (iterator.hasNext()) {
                     Vehicle previous = iterator.next();

                     previous.setPositionX(previous.getPositionX() + 1);
                     previous.setPositionY(previous.getPositionY() + 1);
                     previousElements.add(previous);

                 }
                 break; // Exit the loop after printing the previous elements from 3
             }
         }


         for (int row = 3, column = 49, i = 0; previousElements.size() > 0; column--, i++) {
             Vehicle v = previousElements.get(i);
             buttons[row][column].add(v.getComponent());
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             borderField.repaint();
             borderField.revalidate();

         }

         copyArray();*/
     }

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



    public synchronized static void removeVehicles() {
        var list = buttons;
        for (var i : list) {
            for (var k : i) {
                var list2 = k.getComponents();
                for (var m : list2) {
                    if (vehicleQueue.contains(m))
                    //  if (m.getBackground().equals(Color.red) || m.getBackground().equals(Color.green) ||m.getBackground().equals(Color.yellow))
                    {
                        borderField.remove(m);
                        k.remove(m);


                    }
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
System.out.println("obrisano");
    }
        public synchronized static void removeVehicle(int x,int y) //refresh za duh figuru (uklanjanje dijamanata)
    {

        System.out.println(x+" "+y);
        var list2= buttons[x][y].getComponents();
        for (var m:list2)
        {
            if(buttons[x][y].getComponents().length>1)
            {
                if ((m.equals(buttons[x][y].getComponent(0)) || m.equals(buttons[x][y].getComponent(1))) && vehicleQueue.contains(m) )
                { borderField.remove(m);

                    vehicleQueue.remove(m);
                    buttons[x][y].remove(m);

                    System.out.println("obrisan objekat1");

                    break;
                }
            }
            else if ((m.equals(buttons[x][y].getComponent(0)))&& vehicleQueue.contains(m))
            { borderField.remove(m);

                buttons[x][y].remove(m);
                System.out.println("obrisan objekat2");
                break;
            }
        }
        borderField.repaint();
        borderField.revalidate();
    }

    public static void removeElement(Vehicle k)
    {
        int found=0;

        ArrayList < Vehicle > temp = new ArrayList <> ();
                    // Declare the array list v for storing the numbers after popping out
                    while (!vehicleQueue.isEmpty() && !k.equals(null))
                    {

                        if (vehicleQueue.peek().equals(k) && found == 0)
                        { // If the current front of the queue is k and
                            // we have not found it earlier, i.e. found==0 then, this is the first
                            // occurrence. So we pop it out and don't insert into the vector
                            vehicleQueue.remove();
                            found = 1; // Update the value of found to 1 as we have found the first occurrence
                            // of k in this case
                        } else
                        { // Else, push the current front into the vector to store it and pop it from the queue
                            temp.add(vehicleQueue.peek());
                            vehicleQueue.remove();
                        }
                    }
                    if (found == 0) { // Check if k is found yet. If not, print that k is not present in the queue
                        System.out.println("K NOT PRESENT IN THE QUEUE");
                    }
                    // Else
                    else
                    {
                        // Now, the vector contains all the elements other than the first occurrence of k and
                        // queue is empty. So, we just push all the elements of the vector into the queue one by one
                        for (int i = 0; i < temp.size(); i++)
                        {
                            vehicleQueue.add(temp.get(i));
                        }
                        // Now, the queue contains all the elements other than the first occurrence of k in the
                        // correct order.
                        // We just print these elements one by one from the queue.
                    }
                    repaintField(position);
    }

    public static void repaintField(int numOfBlanks)
    {
        System.out.println("repaint field");
        ArrayList < Vehicle > temp = new ArrayList <> ();

        for(int row=3,column=48;column>numOfBlanks;column--)
        {
            Vehicle v=vehicleQueue.peek();
            temp.add(v);
            vehicleQueue.remove();
            buttons[row][column].add(v.getComponent());

            v.setPositionX(row);
            v.setPositionY(column);
            borderField.repaint();
            borderField.revalidate();

            try{
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        for (int i = 0; i < temp.size(); i++)
        {
            vehicleQueue.add(temp.get(i));
        }

    }
}
