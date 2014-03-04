 /*
  __   ____  ____  __   __   __      ____  __  ___ 
 / _\ (  __)(  _ \(  ) / _\ (  )    (  _ \(  )/ __)
/    \ ) _)  )   / )( /    \/ (_/\   ) __/ )(( (_ \
\_/\_/(____)(__\_)(__)\_/\_/\____/  (__)  (__)\___/
FRC Code for the 2014 robot
Written by:
Tyler Felsted :D
Phillip Parker
Bryce John-Michael "The Bryceter" Dere
Anderson Perkins
Garry Little
Sebastian Hedge

 */
package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;

public class AerialPig2014 extends IterativeRobot
{
    ControlStation controlStation = new ControlStation();
    Drivetrain drivetrain = new Drivetrain(1, 2, 3, 4, 1, 2, 3, 4, 1, 1, 9);
    Shooter shooter = new Shooter(5, 6, 5, 6, 2, 10);
    Arm arm = new Arm(7, 8, 9, 7, 8, 3, 11, 12, 13);
    Teleop teleop = new Teleop(controlStation, drivetrain, shooter, arm);
    Automode automode = new Automode(drivetrain, shooter, arm);
    
    public void robotInit()
    {
        teleop.init();
    }
    
    public void autonomousInit()
    {
        automode.init();
    }

    public void autonomousPeriodic()
    {
        automode.runAutomode();
    }
    
    public void teleopInit()
    {
        teleop.init();
    }

    public void teleopPeriodic()
    {
        teleop.runTeleop();
    }
    
    public void testPeriodic()
    {
    
    }
}
