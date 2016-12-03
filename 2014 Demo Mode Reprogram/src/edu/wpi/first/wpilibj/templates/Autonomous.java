package edu.wpi.first.wpilibj.templates;

public class Autonomous extends Thread {
    
    public void run() {
        
    }    
    
    public void wait(double seconds) {
        int millis = (int) seconds * 1000;
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
