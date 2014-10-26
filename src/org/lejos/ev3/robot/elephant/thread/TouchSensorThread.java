package org.lejos.ev3.robot.elephant.thread;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class TouchSensorThread  extends Thread {
	public EV3TouchSensor sensor;
	public SensorMode sensorSampleProvider;
	public boolean push = false;

	public TouchSensorThread(Port p) {
		sensor = new EV3TouchSensor(p);
		sensorSampleProvider = sensor.getTouchMode();
	}

	public void run() {
		while (true) {
			float[] sample = new float[sensorSampleProvider.sampleSize()];
			sensorSampleProvider.fetchSample(sample, 0);
			if (push != ((int) sample[0] > 0)) {
				push = sample[0] > 0;
				System.out.println(push);
			}
		}
	}
}
