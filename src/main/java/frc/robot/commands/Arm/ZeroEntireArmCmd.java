// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Arm;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.ExtendArm;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.TiltArm;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ZeroEntireArmCmd extends SequentialCommandGroup {
  /** Creates a new ZeroEntireArmCmd. */
  public ZeroEntireArmCmd(ExtendArm extendArm, TiltArm tiltArm, Pneumatics pneumatics) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new InstantCommand(() -> pneumatics.setGripperState(Value.kReverse)),
        new InstantCommand(() -> extendArm.setServoAngle(Constants.ArmConstants.extendServoAngle)),
        
        new WaitCommand(0.25),
        new ExtendToLimitSwitch(extendArm),
        // new ExtendToSetpointSequenceCmd(extendArm, Constants.Presets.REST_EXTEND),
        new PIDTiltArmCmd(tiltArm, Constants.Presets.REST_TILT));
  }
}
