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
    }
    
    public void runAutomode()
    {
        
    }
}
