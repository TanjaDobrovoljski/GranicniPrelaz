package tools;


import sample.Simulation;

import javax.swing.*;
import java.text.DecimalFormat;

public class GameDurationTimer  extends Thread{
    private int second,minute;

    private String ddSeconds,ddMinutes;
    private DecimalFormat dFormat=new DecimalFormat("00");
    private Simulation g;
    public static  boolean paused = false;

    public GameDurationTimer(Simulation g)
    {
        this.second=0;
        this.minute=0;
        this.g=g;
    }

    public String getDdSeconds() {
        return ddSeconds;
    }

    public void setDdSeconds(String ddSeconds) {
        this.ddSeconds = ddSeconds;
    }

    public String getDdMinutes() {
        return ddMinutes;
    }

    public void setDdMinutes(String ddMinutes) {
        this.ddMinutes = ddMinutes;
    }

    @Override
    public  void run()
    {
        while (true)
        {
            while(paused) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    GenLogger.log(GameDurationTimer.class, e);
                }
            }

            second++;
            ddSeconds=dFormat.format(second);
            ddMinutes=dFormat.format(minute);
            g.getSimulationTime().setText(ddMinutes+" : "+ddSeconds);
            if(second==60)
            {
                second=0;
                minute++;
                g.getSimulationTime().setText(ddMinutes+" : "+ddSeconds);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                GenLogger.log(GameDurationTimer.class,e);
            }
        }
    }
}

