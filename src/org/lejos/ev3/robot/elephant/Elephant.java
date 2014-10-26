package org.lejos.ev3.robot.elephant;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import org.lejos.ev3.robot.elephant.behavior.QuitBehavior;
import org.lejos.ev3.robot.elephant.behavior.TrumpBehavior;
import org.lejos.ev3.robot.elephant.behavior.WalkBehavior;
import org.lejos.ev3.robot.elephant.thread.TouchSensorThread;

public class Elephant {
	
	static EV3LargeRegulatedMotor mainMotor = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("A"));
	static EV3LargeRegulatedMotor trumpMotor = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("C"));
	static TouchSensorThread headTouchsensor;

	public static void introMessage() {

		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Elephant", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		g.drawString("Requires : ", 2, 20, 0);
		g.drawString(" A - Legs motor", 2, 30, 0);
		g.drawString(" B - Head motor", 2, 40, 0);
		g.drawString(" C - Trump motor", 2, 50, 0);
		g.drawString(" 1 - Trump sensor", 2, 60, 0);
		g.drawString("Actions : ", 2, 70, 0);
		g.drawString(" Enter - Walk", 2, 80, 0);
		g.drawString(" Up - Toggle Trump", 2, 90, 0);

		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using
										// Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit / 2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit + 7, 0);
		g.drawLine(0, y_quit, 45, y_quit); // top line
		g.drawLine(0, y_quit, 0, y_quit + height_quit - arc_diam / 2); // left
																		// line
		g.drawLine(width_quit, y_quit, width_quit, y_quit + height_quit / 2); // right
																				// line
		g.drawLine(0 + arc_diam / 2, y_quit + height_quit, width_quit - 10,
				y_quit + height_quit); // bottom line
		g.drawLine(width_quit - 10, y_quit + height_quit, width_quit, y_quit
				+ height_quit / 2); // diagonal
		g.drawArc(0, y_quit + height_quit - arc_diam, arc_diam, arc_diam, 180,
				90);

		// Enter GUI button:
		g.fillRect(width_quit + 10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit + 15, y_quit + 7, 0, true);

		Button.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			System.exit(0);
		g.clear();
	}

	public static void main(String[] args) {
		introMessage();
		mainMotor.resetTachoCount();
		mainMotor.rotateTo(0);
		mainMotor.setAcceleration(0);
		mainMotor.setSpeed(0);
		
		headTouchsensor = new TouchSensorThread(SensorPort.S1);
		headTouchsensor.setDaemon(true);
		headTouchsensor.start();
		Behavior b1 = new WalkBehavior(mainMotor);
		Behavior b2 = new TrumpBehavior(trumpMotor, headTouchsensor);
		Behavior b3 = new QuitBehavior();
		Behavior[] behaviorList = { 
				b1,
				b2,
					b3
				};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		Button.LEDPattern(6);
		Button.waitForAnyPress();
		arbitrator.start();
	}
}
