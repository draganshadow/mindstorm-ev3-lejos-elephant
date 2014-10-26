package org.lejos.ev3.robot.elephant.behavior;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

import org.lejos.ev3.robot.elephant.thread.TouchSensorThread;

public class TrumpBehavior implements Behavior {
	public static boolean up = true;
	public static boolean run = false;
	public static int fullMoveRotation = 1200;
	public static int currentRotation = 0;

	public EV3LargeRegulatedMotor motor;
	public TouchSensorThread touchsensor;
	
	public TrumpBehavior(EV3LargeRegulatedMotor motor, TouchSensorThread touchsensor) {
		this.motor = motor;
		this.touchsensor = touchsensor;
	}
	
	public boolean takeControl() {
		if (!run)
		{
			run = (Button.readButtons() == Button.ID_UP);
		}
		return run;
	}

	public void suppress() {
		run = false;
	}

	public void action() {
		if (run) {
			System.out.println("Trump " + (up ? "up" : "down"));
			this.motor.setAcceleration(1000);
			this.motor.setSpeed(800);
			this.motor.rotate(getDirection() * fullMoveRotation, true);
			if (up) {
				while (run) {
					if (this.touchsensor.push) {
						this.motor.stop();
						currentRotation = 0;
						up = false;
						run = false;
					}
				}
			} else {
				up = true;
			}
			Delay.msDelay(3000);
			run = false;
		}
	}
	
	public int getDirection() {
		return up ? -1 : 1;
	}
}
