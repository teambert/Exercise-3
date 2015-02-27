package linestuff;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class Commands {
	
	private static int midLight;
	
	public Commands(LightSensor leftLight, LightSensor rightLight) {
		midLight = getAverageLightValue(leftLight, rightLight);
	}
	
	public int getAverageLightValue(LightSensor leftLight, LightSensor rightLight) {
		int counter = 0;
		for (int count = 0 ; count < 500 ; ++count) {
			counter += leftLight.getLightValue();
			counter += rightLight.getLightValue();
		}
		return counter / 1000;
	}
	
	public boolean onTape(final LightSensor sensor)
	{
		return sensor.getLightValue() < midLight;
	}
	
	public void stopAndTravel(DifferentialPilot pilot, double distance) {
		pilot.stop();
		Delay.msDelay(100);
		pilot.travel(distance);
	}
	
	public int getMidLight() {
		return midLight;
	}

}
