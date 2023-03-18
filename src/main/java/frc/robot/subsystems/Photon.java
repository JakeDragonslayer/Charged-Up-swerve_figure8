package frc.robot.subsystems;

import java.lang.annotation.Target;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonTargetSortMode;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public final class Photon extends SubsystemBase
{
    final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(29);
    
    // Angle between horizontal and the camera.
    final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);
    final double GOAL_RANGE_METERS = Units.inchesToMeters(16);
    PhotonCamera camera = new PhotonCamera("photonvision");
    
    PhotonPipelineResult result = camera.getLatestResult();
    //AprilTagFieldLayout aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
   // there are some errors with this ^
            //gets the list of targets seen
            PhotonTargetSortMode sortMode = PhotonTargetSortMode.Centermost;
            PhotonTargetSortMode photonTargetSortMode;
            List<PhotonTrackedTarget> targets = result.getTargets();
            //gets the best target
            PhotonTrackedTarget target = result.getBestTarget();
            //gets the id of the best target
            public int targetID = target.getFiducialId();
            
            public void Tag(){
                if(targetID == 1){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                
                else if(targetID == 2){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 3){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 4){
                //loading
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(24.38);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 5){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 6){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 7){
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(15.13);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));
                }
                else if(targetID == 8){
                //loading
                    final double TARGET_HEIGHT_METERS = Units.inchesToMeters(24.38);
                    double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            CAMERA_HEIGHT_METERS,
                            TARGET_HEIGHT_METERS,
                            CAMERA_PITCH_RADIANS,
                            Units.degreesToRadians(result.getBestTarget().getPitch()));

                }
            }

            public String test() {
                System.out.println("test");
                return "hi";
            }
            //outputs the tracked id to smart dashboard
                 private void updateSmartDashboard() {
                SmartDashboard.putNumber("targetID", targetID);
                System.out.println(targetID);
                System.out.println("Hi");
            }
                public void periodic() {
                    updateSmartDashboard();
                }
                public void targetID() {
                }
            
}