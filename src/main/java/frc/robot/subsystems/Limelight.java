// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Limelight extends SubsystemBase {

  private PhotonCamera camera;

  public PhotonCamera getCamera() {
    return camera;
  }

  // Constants such as camera and target height stored. Change per robot and goal!
  public final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(11);
  public final double TARGET_HEIGHT_METERS = Units.inchesToMeters(25);

  // Angle between horizontal and the camera.
  public final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);

  /** Creates a new Limelight. */
  public Limelight() {

    camera = new PhotonCamera("hhCam");

  }

  @Override
  public void periodic() {
    // Query the latest result from PhotonVision
    var result = camera.getLatestResult(); // returns a PhotoPipeLine Container

    // Check if the latest result has any targets.
    boolean hasTargets = result.hasTargets();

    SmartDashboard.putBoolean("Has target", hasTargets);

    if (hasTargets) {
      // SmartDashboard.putNumber("range inches", PhotonUtils.calculateDistanceToTargetMeters(
      //     CAMERA_HEIGHT_METERS,
      //     TARGET_HEIGHT_METERS,
      //     CAMERA_PITCH_RADIANS,
      //     Units.degreesToRadians(result.getBestTarget().getPitch())));
      // SmartDashboard.putNumber("target Yaw", result.getBestTarget().getYaw());
      SmartDashboard.putNumber("tag ID", result.getBestTarget().getFiducialId());

      Transform3d bestCameraToTarget = result.getBestTarget().getBestCameraToTarget();
     

      SmartDashboard.putNumber("x (roll)",
          Units.radiansToDegrees(bestCameraToTarget.getRotation().getX()));
      SmartDashboard.putNumber("y (pitch)",
          Units.radiansToDegrees(bestCameraToTarget.getRotation().getY()));
      SmartDashboard.putNumber("z (yaw)",
          Units.radiansToDegrees(bestCameraToTarget.getRotation().getZ()));

      SmartDashboard.putNumber("x",
        bestCameraToTarget.getX());
      SmartDashboard.putNumber("y",
          bestCameraToTarget.getY());
      SmartDashboard.putNumber("z",
         bestCameraToTarget.getZ());

    }

  }
}