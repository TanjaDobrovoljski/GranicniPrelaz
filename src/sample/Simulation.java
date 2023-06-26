package sample;

import Passenger.Passenger;
import Vehicle.Bus;
import Vehicle.Car;
import Vehicle.*;
import border.CustomsTerminal;
import border.PoliceTerminal;
import border.Terminal;
import tools.FW;
import tools.GameDurationTimer;
import tools.GenLogger;
import tools.TooManyPassengersException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {

    private JPanel mainPanel;
    private JPanel middle;
    private JButton pauseButton;
    private JLabel simulationTime;
    private JButton dePauseButton;
    private  JLabel move1Description;
    private  JLabel move2Description;
    private  JLabel move3Description;
    private  JLabel move4Description;
    private  JLabel move5Description;
    private JButton showTablesButton;

    public static boolean isPaused;
    public static  Queue<Vehicle> vehicleQueue=new LinkedList<Vehicle>();
    private static List<Vehicle> vehicleList;
    public static HashMap<Integer, Terminal> terminalsMap=new HashMap<>();

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-HH_mm_ss_dd.MM.yyyy");
    static String time=simpleDateFormat.format(new Date());
    private static String f =  time+ ".ser";
    private static String f2 = time+".txt";

    public static File file1 = new File(f),file2=new File(f2);

    public static JLabel move1,move2,move3,move4,move5;


    public JLabel getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(JLabel simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void pause() {
        isPaused=true;
    }

    public void resume() {
        isPaused=false;
    }

    public static final String razlogVozac="Vozac nema ispravna dokumenta!",razlogKamion="Stvarna masa kamiona je veca od deklarisane!",
    razlogPutnik="Putnik nema ispravna dokumenta!",razlogPrtljag="Putnik ima nedozvoljene stvari u prtljagu!";

    private String filePath = "status.txt";
    private File file = new File(filePath);



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
        isPaused=false;
        borderField = middle;
        int rows=7,columns=55;
        borderField.setLayout(new GridLayout(rows, columns));
        dePauseButton.setEnabled(false);


        showTablesButton.setEnabled(false);
        move1=move1Description;
        move2=move2Description;
        move3=move3Description;
        move4=move4Description;
        move5=move5Description;

        buttons = new JButton[rows][columns];
        int k=1;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {

                buttons[x][y] = new JButton();

                buttons[x][y].setEnabled(false);

                buttons[x][y].setMargin(new Insets(0, 0, 0, 0));
                borderField.add(buttons[x][y]);

            }
            borderField.setEnabled(false);
        }



        initializeVehicles();

       initilizeTerminals();

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GameDurationTimer.paused=true;
               for(int i=0;i<5;i++)
                   if(terminalsMap.get(i).getStatus().equals("released") || terminalsMap.get(i).getStatus().equals("released\r"))
                       terminalsMap.get(i).suspend();
                   pauseButton.setEnabled(false);
                   dePauseButton.setEnabled(true);

            }});

        dePauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                GameDurationTimer.paused=false;
                for(int i=0;i<5;i++)
                    if(terminalsMap.get(i).getStatus().equals("released") || terminalsMap.get(i).getStatus().equals("released\r"))
                        terminalsMap.get(i).resume();
                pauseButton.setEnabled(true);
                dePauseButton.setEnabled(false);

            }});


        GameDurationTimer gameTimer = new GameDurationTimer(this);
        gameTimer.start();


       /* while(terminalsMap.get(0).isAlive()|| terminalsMap.get(1).isAlive() || terminalsMap.get(2).isAlive() || terminalsMap.get(3).isAlive() || terminalsMap.get(4).isAlive())
        { }
        GameDurationTimer.paused=true;
        showTablesButton.setEnabled(true);
        showTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                pauseButton.setEnabled(false);
                dePauseButton.setEnabled(false);
                Penalties p=new Penalties();

            }});*/


    }


    public static void main(String args[]) throws TooManyPassengersException {


        JFrame frame = new JFrame("Border Crossing");


        frame.setVisible(true);

        frame.pack();
        frame.setSize(1400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Simulation application = new Simulation();
        frame.setContentPane(application.mainPanel);
        FW fw=new FW();
        fw.start();


    }

    public void initializeVehicles() throws TooManyPassengersException {


        List<Passenger> lista=new ArrayList<Passenger>();
        Random rand=new Random();
        int tmp=0;

        for(int i=0;i<50;i++)
        {
            Passenger passengerDriver=new Passenger(true);
            lista.add(passengerDriver);
        }


       for (int i = 0; i < 5; i++) {
           List<Passenger> vehiclePassengers=new ArrayList<Passenger>();


           vehiclePassengers.add(lista.get(i));
           tmp= rand.nextInt(52);
           for(int j=0;j<tmp;j++)
           {
               Passenger p=new Passenger(false);
               lista.add(p);
               vehiclePassengers.add(p);

           }

           Bus bus = new Bus(vehiclePassengers);
           vehicleQueue.add(bus);
        }

        // Generate 10 trucks and add them to the queue
        for (int i = 0,k=5; i < 10; i++,k++) {
            List<Passenger> vehiclePassengers=new ArrayList<Passenger>();


            vehiclePassengers.add(lista.get(k));
            tmp= rand.nextInt(3);
            for(int j=0;j<tmp;j++)
            {
                Passenger p=new Passenger(false);
                lista.add(p);
                vehiclePassengers.add(p);

            }


            Truck truck = new Truck(vehiclePassengers);
            if(k/2!=0)
                truck.calculateActualWeight(true);
            else
                truck.calculateActualWeight(false);
            vehicleQueue.add(truck);



        }

        // Generate 35 cars and add them to the queue
        for (int i = 0,k=15; i < 35; i++,k++) {
            List<Passenger> vehiclePassengers=new ArrayList<Passenger>();


            vehiclePassengers.add(lista.get(k));
            tmp= rand.nextInt(5);
            for(int j=0;j<tmp;j++)
            {
                Passenger p=new Passenger(false);
                lista.add(p);
                vehiclePassengers.add(p);

            }

            Car car = new Car(vehiclePassengers);


            vehicleQueue.add(car);
        }

        for(int i=0;i<(lista.size()*3)/100;i++)
        {
           Passenger p= lista.get(rand.nextInt(lista.size()));
            p.setHasValidDocuments(false);
        }

        // Shuffle the queue to randomize the order of vehicles
        vehicleList = new ArrayList<>(vehicleQueue);
        Collections.shuffle(vehicleList);
        vehicleQueue = new LinkedList<>(vehicleList);


        for(int row=3,column=49;column>=0;column--)
        {
            Vehicle v=vehicleQueue.poll();
            v.getComponent().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Handle the click event here
                    VehicleDetails vehicleForm = new VehicleDetails(v);


                }
            });
            buttons[row][column].add(v.getComponent());

            v.setPositionX(row);
            v.setPositionY(column);

        }
        vehicleQueue.addAll(vehicleList);

    }




    public void initilizeTerminals()
    {
        PoliceTerminal p1=new PoliceTerminal(false,vehicleQueue);
        p1.setX(1);
        p1.setY(51);
        terminalsMap.put(p1.getTerminalID(),p1);
        PoliceTerminal p2=new PoliceTerminal(false,vehicleQueue);
        p2.setX(3);
        p2.setY(51);
        terminalsMap.put(p2.getTerminalID(),p2);
        PoliceTerminal pK=new PoliceTerminal(true,vehicleQueue);
        pK.setX(5);
        pK.setY(51);
        terminalsMap.put(pK.getTerminalID(),pK);


        CustomsTerminal c=new CustomsTerminal(false);
        c.setX(1);
        c.setY(53);
        terminalsMap.put(c.getTerminalID(),c);
        CustomsTerminal cK=new CustomsTerminal(true);
        cK.setX(5);
        cK.setY(53);
        terminalsMap.put(cK.getTerminalID(),cK);

        buttons[1][51].add(p1.getComponent());
        buttons[3][51].add(p2.getComponent());
        buttons[5][51].add(pK.getComponent());

        buttons[1][53].add(c.getComponent());
        buttons[5][53].add(cK.getComponent());

        p1.setC(c);
        p2.setC(c);
        pK.setC(cK);


        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getName()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] niz=line.split(" ");
                terminalsMap.get(Integer.parseInt(niz[1])).setStatus(niz[2]);

            }
            reader.close();
              }
        catch (IOException e) {
            GenLogger.log(Simulation.class,e);
        }

        for(int i=0;i<3;i++)
        {
            if(terminalsMap.get(i).getStatus().equals("released"))
                terminalsMap.get(i).start();
        }


    }



    public static int position=0;


    public synchronized static void removeElement(Vehicle k)
    {
        if(vehicleQueue.contains(k)) {
        int found=0;

        ArrayList < Vehicle > temp = new ArrayList <> ();
                    // Declare the array list v for storing the numbers after popping out

            while (!vehicleQueue.isEmpty() && !k.equals(null)) {

                if (vehicleQueue.peek().equals(k) && found == 0) { // If the current front of the queue is k and
                    // we have not found it earlier, i.e. found==0 then, this is the first
                    // occurrence. So we pop it out and don't insert into the vector
                    vehicleQueue.remove();
                    found = 1; // Update the value of found to 1 as we have found the first occurrence
                    // of k in this case
                } else { // Else, push the current front into the vector to store it and pop it from the queue
                    temp.add(vehicleQueue.peek());
                    vehicleQueue.remove();
                }
            }
            if (found == 0) { // Check if k is found yet. If not, print that k is not present in the queue
                System.out.println("K NOT PRESENT IN THE QUEUE");
            }
            // Else
            else {
                // Now, the vector contains all the elements other than the first occurrence of k and
                // queue is empty. So, we just push all the elements of the vector into the queue one by one
                for (int i = 0; i < temp.size(); i++) {
                    vehicleQueue.add(temp.get(i));
                }
                // Now, the queue contains all the elements other than the first occurrence of k in the
                // correct order.
                // We just print these elements one by one from the queue.
            }
        }

        try{
            Thread.sleep(10);
            repaintField(position);
        } catch (NullPointerException e)
        {
            GenLogger.log(Simulation.class,e);
        }
        catch (InterruptedException e ) {
            GenLogger.log(Simulation.class,e);
        }



    }

    public synchronized static void repaintField(int numOfBlanks)
    {

        ArrayList < Vehicle > temp = new ArrayList <> ();

        for(int row=3,column=49;column>=numOfBlanks;column--)
        {

            Vehicle v=vehicleQueue.peek();

                temp.add(v);
                if(v!=null){
                try {

                    vehicleQueue.remove();
                    buttons[row][column].add(v.getComponent());

                    v.setPositionX(row);
                    v.setPositionY(column);

                }catch (NoSuchElementException e)
                {}
                }
            borderField.repaint();
            borderField.revalidate();


        }
        for (int i = 0; i < temp.size(); i++)
        {
            vehicleQueue.add(temp.get(i));
        }

    }




}
