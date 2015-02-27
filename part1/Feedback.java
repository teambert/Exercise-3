package part1;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class Feedback extends RobotProgrammingDemo {
	
	private final WheeledRobotSystem robot;
	private final double DISTANCE = 200.0;
	private final double maxDistance = 500.0;

	public Feedback(WheeledRobotSystem robot) {
		this.robot = robot;
	}

	@Override
	public void run() {
		DifferentialPilot pilot = robot.getPilot();
		OpticalDistanceSensor infSensor = new OpticalDistanceSensor(SensorPort.S2);

		pilot.forward();
		final double maxSpeed = pilot.getMaxTravelSpeed();
		
		while (m_run) {
			keepAwayFromWall(infSensor, pilot, maxSpeed);
		}
	}
	
	private void keepAwayFromWall(OpticalDistanceSensor sens, DifferentialPilot pil, double maxSpeed) {
		double distanceToWall = sens.getDistance();
		System.out.println(distanceToWall);
		
		if (distanceToWall > DISTANCE) {
			pil.setTravelSpeed( ((distanceToWall - DISTANCE) / maxDistance)* maxSpeed);
			if(distanceToWall > maxDistance)
				pil.setTravelSpeed(maxSpeed);
			pil.forward();
		}
		else if(distanceToWall < DISTANCE - 20)
		{
			pil.setTravelSpeed( (1 - (distanceToWall / maxDistance) )* maxSpeed);
			pil.backward();
		}
		else
		{
			pil.stop();
		}
	}

	public static void main(String[] args) {
		Button.waitForAnyPress();
		WheeledRobotSystem robot = new WheeledRobotSystem(RobotConfigs.BERT_BOT);
		RobotProgrammingDemo demo = new Feedback(robot);
		demo.run();
	}

}
