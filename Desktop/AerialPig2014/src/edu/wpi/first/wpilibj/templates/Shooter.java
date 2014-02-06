/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Developer
 */
public class Shooter 
{    
    Talon winchTalon1;
    Talon winchTalon2;
    Solenoid fireSolenoid;
    Encoder winchEncoder;
    DigitalInput winchTouchSensor;

    public Shooter(int sT1, int sT2, int encW1, int encW2, int SolSh, int digIn)
    {
        winchTalon1 = new Talon(sT1);
        winchTalon2 = new Talon(sT2);
        winchEncoder = new Encoder(encW1, encW2);
        fireSolenoid = new Solenoid(SolSh);
        winchTouchSensor = new DigitalInput(digIn);
    }
    
    //Pulls back the piston for firing
    public void chargeShooter()
    {
        if (!winchTouchSensor.get())
        {
            winchTalon1.set(1.0);
            winchTalon2.set(1.0);
        }
        else 
        {
            winchTalon1.set(0.0);
            winchTalon2.set(0.0);
        }
    }
    
    //Gets the encoder values for something I don't know what
    public int getWinchEncoder()
    {
        return winchEncoder.get();
    }
    
    public boolean getWinchTouchSensor()
    {
        return winchTouchSensor.get();
    }
    
    public void setFireSolenoid(boolean fire)
    {
        fireSolenoid.set(fire);
    }
}
