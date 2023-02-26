// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TiltArm;
import frc.robot.subsystems.ExtendArm;
import frc.robot.subsystems.Pneumatics;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class PIDExtendArmCmd extends CommandBase {
  private ExtendArm extendArm;
  private Pneumatics pneumatics;
  private double setpoint;
  double output;

  /** Creates a new PIDExtendArmCmd. */
  public PIDExtendArmCmd(ExtendArm extendArm, Pneumatics pneumatics, double setpoint) {
    this.extendArm = extendArm;
    this.pneumatics = pneumatics;
    this.setpoint = setpoint;
    addRequirements(extendArm, pneumatics);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putNumber("extend setpoint", setpoint);
    // pneumatics.setExtenderState(kForward);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double position = extendArm.getExtendPosition();
    output = extendArm.getExtensionController().calculate(position, setpoint);
    System.out.println(output);
    SmartDashboard.putNumber("extendPos", position);
    SmartDashboard.putNumber("extendOutput", output);

    extendArm.getExtensionMotor().set(output);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    pneumatics.setExtenderState(kReverse);
    // pneumatics.toggleExtenderSolenoid();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return arm.getExtendPosition() >= 0.9 * setpoint;
    return Math.abs(output) < 0.05;
  }
}