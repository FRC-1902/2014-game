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
    long stepStartTimeMS = 0;
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
        drivetrain.compressor.start();
        shooter.prepareToShoot();
        autoStartTimeMS = System.currentTimeMillis();
        arm.enableAngleControl();
        setStep(0);
    }
    
    public void runAutomode()
    {
        arm.updateArmPosition();
        //TODO: Auto must be tested and tuned
        if(step == 0)
        {
            shooter.prepareToShoot();
            shooter.chargeShooter();
            
            if(getStepTime() >= 800)
            {
                nextStep();
            }
        }
        else if(step == 1)
        {
            arm.setGripperSolenoid(true);
            
            if(getStepTime() >= 500)
            {
               nextStep(); 
            }
        }
        else if(step == 2)
        {
            //TODO:if camera
            arm.setHoldPosition(Arm.autoScorePreset);
            if(getStepTime() >= 2000)
            {
                nextStep();
            }
        }
        else if(step == 3)
        {
            arm.setHoldPosition(Arm.autoScorePreset);
            shooter.shoot();
            if(getStepTime() >= 2500)
            {
                nextStep();
                drivetrain.resetDrivetrainEncoder();
            }
        }
        else if(step == 4)
        {
   
            
                drivetrain.moveTo(moveDistance);
                if(drivetrain.getDistance() >= moveDistance)
                {
                    nextStep();
                }
            
        }
        
    }
    
    public long getTime()
    {
        return System.currentTimeMillis() - autoStartTimeMS;
    }
    
    public long getStepTime()
    {
        return System.currentTimeMillis() - stepStartTimeMS;
    }
    
    private void nextStep()
    {
        setStep(step + 1);
    }
    
    private void setStep(int newStep)
    {
        step = newStep;
        stepStartTimeMS = System.currentTimeMillis();
        System.out.println("Step: " + step + " Time: " + getTime());
    }
}
