package org.usfirst.frc.team4415.robot;

import ecommon.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4415.robot.TalonReset;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class DriveTrain {
	
	
	private Joystick m_joystick;
	
	//PDP
	PowerDistributionPanel m_pdp;
	
	//Right Side Motors
	WPI_TalonSRX m_rMaster;
	WPI_VictorSPX m_rMotor1;
	WPI_VictorSPX m_rMotor2;
	//Left Side Motors
	WPI_VictorSPX m_lMotor1;
	WPI_VictorSPX m_lMotor2;
	WPI_TalonSRX m_lMaster;
	
	//Current
	double m_rSideCurrent, m_lSideCurrent;
	
	//Encoders
	Encoder m_rightEncoder;
	Encoder m_leftEncoder;
	String m_shiftStatus;
	
	int x = 0;
	
	//Buttons
	Button m_gearShift;
	Button m_encoderBut;
	
	//Pneumatics
	DoubleSolenoid m_gears;
	
	//Booleans
	boolean m_gearLoop = false;
	boolean m_encoderLoop = false;
	
	public void RobotInit(Joystick j) {
		//Joystick
		m_joystick = j;
		
		
		//Buttons
		m_gearShift = new JoystickButton(m_joystick, RobotMap.gearShift);
		m_encoderBut = new JoystickButton(m_joystick, RobotMap.encoderReset);
		
		
		//Right Side Motors Talon Assign
		m_rMaster = new WPI_TalonSRX(RobotMap.rMaster);
		m_rMotor1 = new WPI_VictorSPX(RobotMap.rMotor1);
		m_rMotor2 = new WPI_VictorSPX(RobotMap.rMotor2);
		//Left Side Motors Talon Assign
		m_lMotor1= new WPI_VictorSPX(RobotMap.lMotor1);
		m_lMotor2 = new WPI_VictorSPX(RobotMap.lMotor2);
		m_lMaster = new WPI_TalonSRX(RobotMap.lMaster);
		
//		//Pneumatics
		m_gears = new DoubleSolenoid(0, 1);
		//Current Gear
		m_shiftStatus = "NULL";
		
		//Encoders
		m_rMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		m_lMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
	}
	
	public void TeleopInit() {
		m_gears.set(DoubleSolenoid.Value.kReverse);
		m_shiftStatus = "Low Gear";
		m_rMaster.setSelectedSensorPosition(0, 0, 0);
		m_lMaster.setSelectedSensorPosition(0, 0, 0);
		
		m_rMaster.configOpenloopRamp(0, 0);
		m_lMaster.configOpenloopRamp(0, 0);
	}

	public void TeleopPeriodic(Joystick j, PowerDistributionPanel p) {
		m_joystick = j;
		m_pdp = p;
		x++;
		SmartDashboard.putNumber("x", x);
		
		double rMasterCurrent = m_pdp.getCurrent(RobotMap.rMasterCurrent);
		double rMotor1Current = m_pdp.getCurrent(RobotMap.rMotor1Current);
		double rMotor2Current = m_pdp.getCurrent(RobotMap.rMotor2Current);
		
		double lMasterCurrent = m_pdp.getCurrent(RobotMap.lMasterCurrent);
		double lMotor1Current = m_pdp.getCurrent(RobotMap.lMotor1Current);
		double lMotor2Current = m_pdp.getCurrent(RobotMap.lMotor2Current);
		
		m_rSideCurrent = rMasterCurrent + rMotor1Current + rMotor2Current;
		m_lSideCurrent = lMasterCurrent + lMotor1Current + lMotor2Current;
		
		
		
		
		
		
		
		//Retrieves Axis value from controller (-1 to 1)
				double xAxis, yAxis;
				xAxis = m_joystick.getRawAxis(5);
				yAxis = m_joystick.getRawAxis(0);
				
				//Equation for Arcade Drive
				double leftSide, rightSide;
				rightSide = yAxis + xAxis;
				leftSide = xAxis - yAxis;
				
				//Right Side Motors Control
				m_rMaster.set(ControlMode.PercentOutput, rightSide);
				m_rMotor1.set(ControlMode.PercentOutput, rightSide);
				m_rMotor2.set(ControlMode.PercentOutput, rightSide);

				//Left Side Motors Control
				m_lMaster.set(ControlMode.PercentOutput, leftSide);
				m_lMotor1.set(ControlMode.PercentOutput, leftSide);
				m_lMotor2.set(ControlMode.PercentOutput, leftSide);
				
				//Configurates Acceleration slowly (Neutral to full, Timeout MS)
//				m_rMaster.configOpenloopRamp(0.7, 0);
//				m_rMotor1.configOpenloopRamp(0.7, 0);
//				m_rMotor2.configOpenloopRamp(0.7, 0);
//				
//				m_lMaster.configOpenloopRamp(0.7, 0);
//				m_lMotor1.configOpenloopRamp(0.7, 0);
//				m_lMotor2.configOpenloopRamp(0.7, 0);
				
				
		//Gear Shift
				if (m_gearShift.get() && !m_gearLoop) {
					m_gearLoop = true;
					if (m_gears.get() == DoubleSolenoid.Value.kReverse) {
						m_gears.set(DoubleSolenoid.Value.kForward);
						m_shiftStatus = "High Gear";
					} else {
						m_gears.set(DoubleSolenoid.Value.kReverse);
						m_shiftStatus = "Low Gear";
					}
					
				}
				if (!m_gearShift.get()) {
					m_gearLoop = false;
				}
				
		//Sets Encoder value to 0;	
				if (m_encoderBut.get() == true && m_encoderLoop == false) {
					m_encoderLoop = true;
					m_rMaster.setSelectedSensorPosition(0, 0, 0);
					m_lMaster.setSelectedSensorPosition(0, 0, 0);
					
				}
				if (m_encoderBut.get() == false) {
					m_encoderLoop = false;
				}
				
			}
	
	
		public void Report() {
			
			
			SmartDashboard.putString("Current Gear", m_shiftStatus);
			SmartDashboard.putNumber("Right Side Current", m_rSideCurrent);
			SmartDashboard.putNumber("Left Side Current", m_lSideCurrent);
			
			SmartDashboard.putNumber("Right Encoder Position", m_rMaster.getSelectedSensorPosition());
			SmartDashboard.putNumber("Left Encoder Position", m_lMaster.getSelectedSensorPosition());
		}
	}
