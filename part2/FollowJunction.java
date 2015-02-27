package part2;

import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import linestuff.Commands;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class FollowJunction extends RobotProgrammingDemo {

	private final WheeledRobotSystem robot;
	private DifferentialPilot pilot;
	private LightSensor rightLight;
	private LightSensor leftLight;
	private int midLight;
	private Random ran;
	private double forwardDistance = 70;
	
	public FollowJunction(WheeledRobotSystem robot) {
		this.robot = robot;
		pilot = robot.getPilot();
		rightLight = new LightSensor(SensorPort.S1);
		leftLight = new LightSensor(SensorPort.S4);
		ran = new Random();	
	}
	
	@Override
	public void run() {
		midLight = getAverageLightValue(leftLight, rightLight);
		Sound.beep();
		Button.waitForAnyPress();
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed()/2);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed()/2);
		pilot.forward();
		
		initListeners();
		
		
		while (m_run) {

		}
	}
	
	private void rotateOnMap() {
		
			switch (ran.nextInt(3)) {
				case (0): {
					pilot.rotate(90);
					break;
				}
				case (1): {
					pilot.rotate(-90);
					break;
				}
				case (2): {
					pilot.rotate(0);
					break;
				}
			}
			/*
			case ("right"): {
				pilot.rotate(-90);
				break;
			}
			case ("left"): {
				pilot.rotate(90);
				break;
			}
			*/
			
			pilot.forward();
	}
			
	public boolean onTape(LightSensor sensor)
	{
		return sensor.getLightValue() < midLight;
	}
	
	public int getAverageLightValue(LightSensor leftLight, LightSensor rightLight) {
		int counter = 0;
		for (int count = 0 ; count < 500 ; ++count) {
			counter += leftLight.getLightValue();
			counter += rightLight.getLightValue();
		}
		return counter / 1000;
	}
	
	public void stopAndTravel(DifferentialPilot pilot, double distance) {
		pilot.stop();
		Delay.msDelay(100);
		pilot.travel(distance);
	}
	
	private void initListeners() {		
		SensorPort.S4.addSensorPortListener( new SensorPortListener()	
		{
			public void stateChanged(SensorPort s4, int i, int i1)
			{
				if (onTape(leftLight) && onTape(rightLight)) {
					stopAndTravel(pilot, forwardDistance);
					rotateOnMap();
				}
				else if(onTape(leftLight))
				{
					pilot.stop();
					pilot.rotate(-5);
				}
				else
					pilot.forward();
			}
		});
		SensorPort.S1.addSensorPortListener( new SensorPortListener()	
		{
			public void stateChanged(SensorPort s1, int i, int i1)
			{	
				if (onTape(leftLight) && onTape(rightLight)) {
					stopAndTravel(pilot, forwardDistance);
					rotateOnMap();
				}
				else if (onTape(rightLight))
				{
					pilot.stop();
					pilot.rotate(5);
				}	
				else
					pilot.forward();
			}			
		});
	}
	
	/* 
	 * New robot measurements:
	 * 	wheel diameter: 56mm
	 * 	track width: 115mm
	 * 	robot length: 110mm
	 * 	left motor: C
	 * 	right motor: A
	 */
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		WheeledRobotSystem robot = new WheeledRobotSystem(RobotConfigs.BERT_BOT);
		RobotProgrammingDemo demo = new FollowJunction(robot);
		
		demo.run();
	}
}

