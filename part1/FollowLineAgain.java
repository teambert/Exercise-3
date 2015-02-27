package part1;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import linestuff.Commands;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class FollowLineAgain extends RobotProgrammingDemo {
	
	private final WheeledRobotSystem robot;
	private DifferentialPilot pilot;
	private LightSensor rightLight;
	private LightSensor leftLight;
	private Commands commands;
	private int midLight;
	
	public FollowLineAgain(WheeledRobotSystem robot) {
		this.robot = robot;
		pilot = robot.getPilot();
		rightLight = new LightSensor(SensorPort.S1);
		leftLight = new LightSensor(SensorPort.S4);
	}

	@Override
	public void run() {				
		
		midLight = getAverageLightValue(leftLight, rightLight);
		Button.waitForAnyPress();
		
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed()/3);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed()/1);
		initListeners();
		//pilot.backward();
		
		while(m_run){
			
		}

	}
	
	private void initListeners() {		
		SensorPort.S4.addSensorPortListener( new SensorPortListener()	
		{
			public void stateChanged(SensorPort s4, int i, int i1)
			{
				if(onTape(leftLight))
				{
					//pilot.stop();
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
				if (onTape(rightLight))
				{
					//pilot.stop();
					pilot.rotate(5);
				}	
				else
					pilot.forward();
			}			
		});						
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
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		WheeledRobotSystem robot = new WheeledRobotSystem(RobotConfigs.BERT_BOT);
		RobotProgrammingDemo demo = new FollowLineAgain(robot);
		
		demo.run();
	}
}
