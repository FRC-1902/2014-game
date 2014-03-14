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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;

public class AerialPig2014 extends IterativeRobot
{
    ControlStation controlStation = new ControlStation(1, 2, 3);
    Drivetrain drivetrain = new Drivetrain(1, 2, 3, 4, 1, 2, 3, 4, 1, 1, 9);
    Shooter shooter = new Shooter(IOConfig.SHOOTER_PWM_1, IOConfig.SHOOTER_PWM_2, 0, 0, IOConfig.SHOOTER_FIRE, IOConfig.SHOOTER_FIRE_SAFE, IOConfig.SHOOTER_TOUCH, 5);
    Arm arm = new Arm(8, 9, 7, 8, 3, 10, 12, 13, 1);
    Teleop teleop = new Teleop(controlStation, drivetrain, shooter, arm);
    Automode automode = new Automode(drivetrain, shooter, arm);
    
    int m_disabledPeriodicLoops = 0;
    
    public static AerialPig2014 me;
    
    static int printSec;
    static int startSec;
    
    public void robotInit()
    {
        teleop.init();
        me=this;
        shooter.winchTouchPower.set(true);
        System.out.println("robotinit");
        
    }
    
    public void disabledInit()
    {
        System.out.println("disabledinit");
    }
    
    public void autonomousInit()
    {
        automode.init();
        System.out.println("Autoinit");
    }

    public void disabledPeriodic()  {
		// feed the user watchdog at every period when disabled
		Watchdog.getInstance().feed();
                
                

		// increment the number of disabled periodic loops completed
		//m_disabledPeriodicLoops++;

		// while disabled, printout the duration of current disabled mode in seconds
//		if ((Timer.getUsClock() / 1000000.0) > printSec) {
//			System.out.println("Disabled seconds: " + (printSec - startSec));
//			printSec++;
//		}
                System.out.println("Arm pot: " + arm.positionSensor.get());
	}
    
    public void autonomousPeriodic()
    {
        automode.runAutomode();
        System.out.println("Autoperiodic");
        getWatchdog().feed();
    }
    
    public void teleopInit()
    {
        teleop.init();
        System.out.println("teleopinit");
    }

    public void teleopPeriodic()
    {
        teleop.runTeleop();
        getWatchdog().feed();
    }
    
    public void testPeriodic()
    {
        System.out.println("Test periodic");
    }
}
