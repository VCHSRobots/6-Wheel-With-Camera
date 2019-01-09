package org.usfirst.frc.team4415.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ecommon.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FrisbeeShoot {
	
	Timer pTimer;
	
	double m_pow;
	
	//Solenoid that fires frisbee
	DoubleSolenoid m_shoot;
	
	//Toggle Button that fires frisbee
	Button m_fire;
	Joystick m_joystick;
	
	//Motor for Frisbee shooter
	WPI_TalonSRX m_frisbee;
	WPI_TalonSRX m_frisTurn;
	
	Button m_frisSpeed;
	
	Button m_frisStop;
	
	double m_rTrig, m_lTrig;
	//Boolean
	boolean m_frisLoop = false;
	boolean m_frisSpdLoop = false;
	boolean m_frisStopLoop = false;
	String m_frisActuatorStat = "Disengaged";
	
	private int m_motor_mode = 0;  // 0=manual  1=750 RPM  2=1500 RPM
	
	public void RobotInit(Joystick j) {
		
		pTimer = new Timer();
		
		m_joystick = j;
	
		m_frisSpeed = new JoystickButton(m_joystick, RobotMap.frisStart);
		m_frisStop = new JoystickButton(m_joystick, RobotMap.frisStop);
		
		m_shoot = new DoubleSolenoid(4, 5);
		
		m_fire = new JoystickButton(m_joystick, RobotMap.frisFire);
		
		m_frisbee = new WPI_TalonSRX(RobotMap.frisbee);
		m_frisTurn = new WPI_TalonSRX(RobotMap.frisTurn);
		
	
	}
	
	public void TeleopInit() {
		m_shoot.set(DoubleSolenoid.Value.kReverse);
		
	}

	public void TeleopPeriodic() {
		
		//Pneumatic Engage
		if (m_fire.get() && !m_frisLoop) {
			pTimer.start();
			m_frisLoop= true;
			
			m_shoot.set(DoubleSolenoid.Value.kForward);
			
			m_frisActuatorStat = "engaged";

			
		}
			
		if (!m_fire.get()) {
			m_frisLoop = false;
			if (pTimer.get() >= 0.85) {
				m_shoot.set(DoubleSolenoid.Value.kReverse);
				m_frisActuatorStat = "disengaged";
				pTimer.stop();
				pTimer.reset();
			}
		}
		
		//Frisbee Go
		if (m_frisStop.get() && !m_frisStopLoop) {
			m_frisStopLoop = true;
			m_motor_mode = 0;
		} else if (!m_frisStop.get()) {
			m_frisStopLoop = false;
		}
		
		if (m_frisSpeed.get() && !m_frisSpdLoop) {
			m_motor_mode++;
			m_frisSpdLoop = true;
			if (m_motor_mode > 3) {
				m_motor_mode = 1;
			}
			if (m_motor_mode == 1) {
			m_frisbee.set(ControlMode.PercentOutput, -0.4);
			}
			if (m_motor_mode == 2) {
				
				m_frisbee.set(ControlMode.PercentOutput, -0.7);
				}
			if (m_motor_mode == 3) {
				
				m_frisbee.set(ControlMode.PercentOutput, -1);
				}
		}
		if (!m_frisSpeed.get()) {
			m_frisSpdLoop = false;
		}
		if (m_motor_mode == 0) {
			
			m_frisbee.set(ControlMode.PercentOutput, 0);
		}
		
		
		//FRISBEE TURN TABLE
		m_rTrig = m_joystick.getRawAxis(3);
		m_lTrig = m_joystick.getRawAxis(2);
		m_pow = 0;
		if (m_rTrig <= 1 && m_lTrig == 0) {
			m_pow = m_joystick.getRawAxis(3) * 0.2;
		} else if (m_lTrig <= 1 && m_rTrig == 0) {
			m_pow = m_joystick.getRawAxis(2) * -0.2;
		} else {
			m_pow = 0;
		}
		m_frisTurn.set(ControlMode.PercentOutput, m_pow);
		
	}
	public void Report() {
		SmartDashboard.putNumber("Turn Table Value", m_pow);
		SmartDashboard.putNumber("Motor Mode", m_motor_mode);
		SmartDashboard.putString("Fire", m_frisActuatorStat);
		SmartDashboard.putNumber("Pneumatic Timer", pTimer.get());
	}

}
