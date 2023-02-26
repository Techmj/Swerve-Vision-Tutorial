// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class ExtendArm extends SubsystemBase {

  public CANSparkMax getExtensionMotor() {
    return extensionMotor;
  }

  public RelativeEncoder getExtensionEncoder() {
    return extensionEncoder;
  }

  public PIDController getExtensionController() {
    return extensionController;
  }

  public double getExtendPosition() {
    return extensionEncoder.getPosition();
  }

  public CANSparkMax extensionMotor;
  public RelativeEncoder extensionEncoder;

  public PIDController extensionController;

  /** Creates a new ExampleSubsystem. */
  public ExtendArm() {

    extensionMotor = new CANSparkMax(Constants.ArmConstants.extendId, MotorType.kBrushless);

    extensionEncoder = extensionMotor.getEncoder();

    extensionController = new PIDController(Constants.ArmConstants.extensionkP, 0, 0.001);
    /**
     * The restoreFactoryDefaults method can be used to reset the configuration
     * parameters
     * in the SPARK MAX to their factory default state. If no argument is passed,
     * these
     * parameters will not persist between power cycles
     */

    extensionMotor.restoreFactoryDefaults();
    extensionMotor.setIdleMode(IdleMode.kBrake);
    extensionMotor.setInverted(false);
    // extensionEncoder.setPosition(0);

  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("extension pos", extensionEncoder.getPosition());

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}