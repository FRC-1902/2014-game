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
public class Automode
{
    Drivetrain drivetrain;
    Shooter shooter;
    Arm arm;
    
    //Variables
    int step = 0;
    long autoStartTimeMS = 0;
    long currentAutoTimeMS = 0;
    int moveDistance = 3;
    
    public Automode(Drivetrain dt, Shooter shoot, Arm a)
    {
        drivetrain = dt;
        shooter = shoot;
        arm = a;
    }
    
    public void init()
    {
        drivetrain.startDrivetrainEncoder(true);
        drivetrain.resetDrivetrainEncoder();
        shooter.startWinchEncoder(true);
        shooter.resetWinchEncoder();
        arm.startArmEncoder(true);
        arm.resetArmEncoder();
        
        autoStartTimeMS = System.currentTimeMillis();
    }
    
    public void runAutomode()
    {
        //TODO: Auto must be tested and tuned
        if(step == 0)
        {
            arm.setGripperSolenoid(true);
            
            if(getTime() >= 800)
            {
                step++;
            }
        }
        else if(step == 1)
        {
                arm.setGripperSolenoid(false);
                step++;
        }
        
        else if(step == 2)
        {
            arm.moveArmTo(50);
            if(arm.getArmEncoder() <= 50)
            {
                step++;
            }
        }
        else if(step == 3)
        {
            //TODO:if camera
            if(getTime() >= 5000)
            {
                step++;
            }
        }
        else if(step == 4)
        {
            drivetrain.moveTo(moveDistance);
            if(drivetrain.getDistance() >= moveDistance)
            {
                step++;
            }
        }
        else if(step == 5)
        {
            shooter.setFireSolenoid(true);
        }
    }
    
    public long getTime()
    {
        return System.currentTimeMillis() - autoStartTimeMS;
    }
}
