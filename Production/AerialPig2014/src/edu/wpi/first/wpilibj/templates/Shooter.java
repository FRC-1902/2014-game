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
    public final static boolean SHOOTER_FIRE = true;
    public final static boolean SHOOTER_COCKED = false;
    
    Talon winchTalon1;
    Talon winchTalon2;
    Solenoid fireSolenoidShoot;
    Solenoid fireSolenoidSafe;
    Solenoid winchTouchPower;
    Encoder winchEncoder;
    DigitalInput winchTouchSensor;
    
    int chargeStep = 0;
    long chargeTime = 0;
    
    

    public Shooter(int sT1, int sT2, int encW1, int encW2, int SolSh1, int SolSh2, int digIn, int touchPower)
    {
        winchTalon1 = new Talon(sT1);
        winchTalon2 = new Talon(sT2);
        //winchEncoder = new Encoder(encW1, encW2);
        fireSolenoidShoot = new Solenoid(SolSh1);
        fireSolenoidSafe = new Solenoid(SolSh2);
        winchTouchSensor = new DigitalInput(digIn);
        winchTouchPower = new Solenoid(touchPower);
        
        prepareToShoot();
    }
    
    //Pulls back the piston for firing
    public boolean chargeShooter()
    {
        
        if (winchTouchSensor.get())
        {
            winchTalon1.set(1.0);
            winchTalon2.set(1.0);
            return false;
        }
        else 
        {
            winchTalon1.set(0.0);
            winchTalon2.set(0.0);
            return true;
        }
    }
    
    public void process()
    {
        if(chargeStep == 0)
        {
            return;
        }
        boolean done = chargeShooter();
        if(chargeStep == 1)
        {
            chargeTime = System.currentTimeMillis();
            nextChargeStep();
        }
        else if(chargeStep == 2)
        {
                prepareToShoot();
                nextChargeStep();  
        }
        else if(chargeStep == 3 && done)
        {
            stopChargeStep();
        }
    }
    
    public void startChargeShooter()
    {
        if(chargeStep == 0)
        {
            System.out.println("Charging shooter");
            nextChargeStep();
        }
    }
    
    public void stopChargeShooter()
    {
        if(chargeStep > 0)
        {
            stopChargeStep();
        }
        winchTalon1.set(0.0);
        winchTalon2.set(0.0);
    }
    
    //Gets the encoder values for something I don't know what
    public void startWinchEncoder(boolean setEnc)
    {
        if(setEnc)
        {
            winchEncoder.start();
        }
        else
        {
            winchEncoder.stop();
        }
    }
    
    public void resetWinchEncoder()
    {
        winchEncoder.reset();
    }
    
    public int getWinchEncoder()
    {
        return winchEncoder.get();
    }
    
    public boolean getWinchTouchSensor()
    {
        return winchTouchSensor.get();
    }
    
    public void shoot()
    {
        fireSolenoidShoot.set(SHOOTER_FIRE);
        fireSolenoidSafe.set(SHOOTER_COCKED);
        System.out.println("Shooting");
    }
    
    public void prepareToShoot()
    {
        fireSolenoidShoot.set(SHOOTER_COCKED);
        fireSolenoidSafe.set(SHOOTER_FIRE);
        System.out.println("Preparing to shoot");
    }
    
    private void nextChargeStep()
    {
        chargeStep++;
        System.out.println("chargeStep: " + chargeStep);
    }
    
    private void stopChargeStep()
    {
        chargeStep = 0;
        System.out.println("ChargeStep is stoppd");
    }
}
