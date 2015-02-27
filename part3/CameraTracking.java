package part3;

import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.robotics.navigation.DifferentialPilot;
import part2.FollowJunction;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class CameraTracking extends RobotProgrammingDemo {
	
	private final WheeledRobotSystem robot;
	private final DifferentialPilot pilot;
	
	public CameraTracking(WheeledRobotSystem robot) {
		this.robot = robot;
		pilot = robot.getPilot();
	}

	@Override
	public void run() {
		NXTCam cam = new NXTCam(SensorPort.S2);
		cam.setTrackingMode(NXTCam.COLOR);
		cam.sortBy(NXTCam.SIZE);
		cam.enableTracking(true);
		
		while(m_run){
			for (int i = 0; i < cam.getNumberOfObjects(); i++){
				cam.getRectangle(i);
			}
		}
	}

	public static void main(String[] args) {
		Button.waitForAnyPress();
		WheeledRobotSystem robot = new WheeledRobotSystem(RobotConfigs.BERT_BOT);
		RobotProgrammingDemo demo = new CameraTracking(robot);
		
		demo.run();
	}
}
