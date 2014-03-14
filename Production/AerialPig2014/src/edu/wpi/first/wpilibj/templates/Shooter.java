/*
 * Shooter for launching the ball.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author Developer
 */
public class Shooter 
{    
    Talon winchTalon1;
    Talon winchTalon2;
    Solenoid fireSolenoidShoot;
    Solenoid fireSolenoidSafe;
    Solenoid winchTouchPower;
    Encoder winchEncoder;
    DigitalInput winchTouchSensor;
    
    

    public Shooter(int sT1, int sT2, int encW1, int encW2, int SolSh1, int SolSh2, int digIn, int touchPower)
    {
        winchTalon1 = new Talon(sT1);
        winchTalon2 = new Talon(sT2);
        //winchEncoder = new Encoder(encW1, encW2);
        fireSolenoidShoot = new Solenoid(SolSh1);
        fireSolenoidSafe = new Solenoid(SolSh2);
        winchTouchSensor = new DigitalInput(digIn);
        winchTouchPower = new Solenoid(touchPower);
        
        setFireSolenoid(false);
    }
    
    //Pulls back the piston for firing
    public void chargeShooter()
    {
        if (winchTouchSensor.get())
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
    
    public void stopChargeShooter()
    {
        winchTalon1.set(0.0);
        winchTalon2.set(0.0);
    }
    
    //Gets the encoder values for something I don't know what
//    public void startWinchEncoder(boolean setEnc)
//    {
//        if(setEnc)
//        {
//            winchEncoder.start();
//        }
//        else
//        {
//            winchEncoder.stop();
//        }
//    }
    
//    public void resetWinchEncoder()
//    {
//        winchEncoder.reset();
//    }
//    
//    public int getWinchEncoder()
//    {
//        return winchEncoder.get();
//    }
    
    public boolean getWinchTouchSensor()
    {
        return winchTouchSensor.get();
    }
    
    public void setFireSolenoid(boolean fire)
    {
        fireSolenoidShoot.set(fire);
        fireSolenoidSafe.set(!fire);
    }
}
