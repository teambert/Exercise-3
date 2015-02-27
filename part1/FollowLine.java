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

public class FollowLine extends RobotProgrammingDemo {
	
	private final WheeledRobotSystem robot;
	private DifferentialPilot pilot;
	private LightSensor rightLight;
	private LightSensor leftLight;
	private Commands commands;
	
	public FollowLine(WheeledRobotSystem robot) {
		this.robot = robot;
		pilot = robot.getPilot();
		rightLight = new LightSensor(SensorPort.S1);
		leftLight = new LightSensor(SensorPort.S4);
	}

	@Override
	public void run() {				
		
		initListeners();
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed()/3);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed()/1);
		Button.waitForAnyPress();
		commands = new Commands(leftLight, rightLight);
		//pilot.backward();
		
		while(m_run){
			
		}

	}
	
	private void initListeners() {		
		SensorPort.S4.addSensorPortListener( new SensorPortListener()	
		{
			public void stateChanged(SensorPort s4, int i, int i1)
			{
				if(commands.onTape(leftLight))
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
				if (commands.onTape(rightLight))
				{
					//pilot.stop();
					pilot.rotate(5);
				}	
				else
					pilot.forward();
			}			
		});						
	}
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		WheeledRobotSystem robot = new WheeledRobotSystem(RobotConfigs.BERT_BOT);
		RobotProgrammingDemo demo = new FollowLine(robot);
		
		demo.run();
	}
}
