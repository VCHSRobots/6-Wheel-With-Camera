package org.usfirst.frc.team4415.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonReset {
	
	public static void ResetConfig(WPI_TalonSRX talon, boolean resetPID) {
		WPI_TalonSRX m_talon;
		m_talon = talon;
		boolean m_PID = resetPID;
		
		m_talon.configOpenloopRamp(0, 0);
		m_talon.configClosedloopRamp(0, 0);
		m_talon.configPeakOutputForward(+1);
		m_talon.configPeakOutputReverse(-1);
		m_talon.configNominalOutputForward(0);
		m_talon.configNominalOutputReverse(0);
		m_talon.configNeutralDeadband(0.04);
		m_talon.configVoltageCompSaturation(0);
		m_talon.configVoltageMeasurementFilter(32);
		m_talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		m_talon.configSelectedFeedbackCoefficient(1.0);
		m_talon.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.QuadEncoder, 0);
		m_talon.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.QuadEncoder, 0);
		m_talon.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.QuadEncoder, 0);
		m_talon.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0);
		m_talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, 0);
		m_talon.configVelocityMeasurementWindow(64);
		m_talon.configForwardSoftLimitThreshold(0);
		m_talon.configReverseSoftLimitThreshold(0);
		m_talon.configForwardSoftLimitEnable(false);
		m_talon.configReverseSoftLimitEnable(false);
		
		if (!m_PID) {
		m_talon.config_kP(0, 0);
		m_talon.config_kI(0, 0);
		m_talon.config_kD(0, 0);
		m_talon.config_kF(0, 0);
		m_talon.config_IntegralZone(0, 0);
		}
		
		m_talon.configAllowableClosedloopError(0, 0);
		m_talon.configMaxIntegralAccumulator(0, 0);
		m_talon.configClosedLoopPeakOutput(0, 1);
		m_talon.configClosedLoopPeriod(0, 1);
		m_talon.configAuxPIDPolarity(false);	
		m_talon.configMotionCruiseVelocity(0);
		m_talon.configMotionAcceleration(0);
		m_talon.configMotionProfileTrajectoryPeriod(0);
		m_talon.configSetCustomParam(0, 0);
		m_talon.configPeakCurrentLimit(0);
		m_talon.configContinuousCurrentLimit(0);
		
		//m_talon.configForwardLimitSwitchSource(Off, normalOpenOrClose), For Remote Talon
		//m_talon.configForwardLimitSwitchSource(Off, normalOpenOrClose), For Remote Talon
		
		//m_talon.configRemoteFeedbackFilter(deviceID, remoteSensorSource, remoteOrdinal)... Can't config... automatically off
		
		
	}

}
