// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.Swerve;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SequentialChaseTagCmd extends SequentialCommandGroup {
  SwerveBase swerveBase;
  Limelight limelight;

  /** Creates a new SequentialChaseTagCmd. */
  public SequentialChaseTagCmd(SwerveBase swerveBase,
      Limelight limelight) {
    this.limelight = limelight;
    this.swerveBase = swerveBase;
    addCommands(getCommand(), new InstantCommand(() -> swerveBase.stopModules()));

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

  }

  public Command getCommand() {
    System.out.println("INIT START"); 
    // robot pose
    var startingPose = new Pose2d(0, 0, new Rotation2d());

    var result = limelight.getCamera().getLatestResult();

    if (result.hasTargets() == false) {
      System.out.println("has target " + result.hasTargets());
      System.out.println("no target to make best!");

      return new InstantCommand();
    } else {
      System.out.println("has target " + result.hasTargets());
      var bestTarget = result.getBestTarget();

      // target pose
      var endingPose = new Pose2d(bestTarget.getBestCameraToTarget().getX() - Units.inchesToMeters(7),
          bestTarget.getBestCameraToTarget().getY(),
          new Rotation2d(bestTarget.getBestCameraToTarget().getRotation().getZ() - Math.PI));

      var interiorWaypoints = new ArrayList<Translation2d>();
      interiorWaypoints.add(new Translation2d(endingPose.getX() / 3.0, endingPose.getY() / 3.0));
      interiorWaypoints.add(new Translation2d(2.0 * endingPose.getX() / 3.0, 2.0 * endingPose.getY() / 3.0));

      TrajectoryConfig config = new TrajectoryConfig(0.75, 0.5);
      config.setReversed(false);

      var trajectory = TrajectoryGenerator.generateTrajectory(
          startingPose,
          interiorWaypoints,
          endingPose,
          config);

      swerveBase.resetOdometry(trajectory.getInitialPose());

      // 3. Define PID controllers for tracking trajectory
      PIDController xController = new PIDController(1, 0,
          0);
      PIDController yController = new PIDController(1, 0,
          0);
      ProfiledPIDController thetaController = new ProfiledPIDController(
          AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
      thetaController.enableContinuousInput(-Math.PI, Math.PI);

      return new SwerveControllerCommand(
          trajectory,
          swerveBase::getPose,
          Swerve.kinematics,
          xController,
          yController,
          thetaController,
          swerveBase::setModuleStates,
          swerveBase);

      // followPath.schedule();

      // System.out.println("INIT END fullCmd has been scheduled");

    }
  }
}