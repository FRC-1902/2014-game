/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

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
    double moveDistance = 4;
    //TODO: make camera work good
    boolean isHotGoal = true;
    
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
        arm.startArmEncoder(true);
        arm.resetArmEncoder();
        
        shooter.setFireSolenoid(false);
        autoStartTimeMS = System.currentTimeMillis();
    }
    
    public void runAutomode()
    {
        System.out.println("Step: " + step);
        //TODO: Auto must be tested and tuned
        if(step == 0)
        {
            shooter.chargeShooter();
            arm.setHoldPosition(Arm.autoScorePreset);
            
            if(getTime() >= 800)
            {
                step++;
            }
        }
        else if(step == 1)
        {
            arm.updateArmPosition();
            arm.setGripperSolenoid(true);
            if(getTime() >= 1300)
            {
               step++; 
            }
        }
        else if(step == 2)
        {
            //TODO:if camera
            arm.updateArmPosition();
            if(isHotGoal || getTime() >= 5000)
            {
                step++;
            }
        }
        else if(step == 3)
        {
            arm.updateArmPosition();
            shooter.setFireSolenoid(true);
            if(getTime() >= 2500)
            {
                step++;
                drivetrain.resetDrivetrainEncoder();
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
        
    }
    
    public long getTime()
    {
        return System.currentTimeMillis() - autoStartTimeMS;
    }
}
