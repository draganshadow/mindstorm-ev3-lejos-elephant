package org.lejos.ev3.robot.elephant.behavior;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class WalkBehavior  implements Behavior {
	
	public int iteration = 1;
	public int takospeed = 0;
	public int speed = 300;
	public boolean run = true;

	public EV3LargeRegulatedMotor motor;
	
	public WalkBehavior(EV3LargeRegulatedMotor motor) {
		this.motor = motor;
	}
	
	public boolean takeControl() {
		if (!run)
		{
			run = (Button.readButtons() == Button.ID_ENTER);
		}
		return run;
	}

	public void suppress() {
		run = false;// standard practice for suppress methods
	}

	public void action() {
		Button.LEDPattern(6);
		Button.discardEvents();
		motor.setAcceleration(100);
		System.out.println("Walk");
		while (run) {
			motor.setSpeed(speed);
			motor.backward();
			iteration++;
			takospeed = motor.getSpeed();
			Delay.msDelay(1000);
			Thread.yield(); // don't exit till suppressed
		}
	}

}
