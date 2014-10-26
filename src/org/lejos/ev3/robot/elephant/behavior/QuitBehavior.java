package org.lejos.ev3.robot.elephant.behavior;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class QuitBehavior implements Behavior {
	
	public static boolean quit = false;
	
	public boolean takeControl() {
		if (!quit)
		{
			quit = (Button.readButtons() == Button.ID_ESCAPE);
		}
		return quit;
	}

	public void suppress() {
	}

	public void action() {
		Button.LEDPattern(0);
		System.out.println("QUIT");
		System.out.flush();
		Delay.msDelay(2000);
		System.exit(1);
	}
}
