package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Swerve.ModulePosition;
import frc.robot.Constants.Swerve.Ports;

import java.util.HashMap;
import java.util.Map;

import static frc.robot.Constants.Swerve.*;

public class SwerveSubsystem extends SubsystemBase {
    private final HashMap<ModulePosition, SwerveModule> m_swerveModules =
            new HashMap<>(
                    Map.of(
                            ModulePosition.FRONT_LEFT,
                            new SwerveModule(
                                    0,
                                    new CANSparkMax(Ports.frontLeftTurnMotor, CANSparkMaxLowLevel.MotorType.kBrushless),
                                    new WPI_TalonFX(Ports.frontLeftDriveMotor),
                                    new DutyCycleEncoder(Ports.frontLeftBoreEncoder),
                                    frontLeftCANCoderOffset),
                            ModulePosition.FRONT_RIGHT,
                            new SwerveModule(
                                    1,
                                    new CANSparkMax(Ports.frontRightTurnMotor, CANSparkMaxLowLevel.MotorType.kBrushless),
                                    invertMotor(Ports.frontRightDriveMotor),
                                    new DutyCycleEncoder(Ports.frontRightBoreEncoder),
                                    frontRightCANCoderOffset),
                            ModulePosition.BACK_LEFT,
                            new SwerveModule(
                                    2,
                                    new CANSparkMax(Ports.backLeftTurnMotor, CANSparkMaxLowLevel.MotorType.kBrushless),
                                    new WPI_TalonFX(Ports.backLeftDriveMotor),
                                    new DutyCycleEncoder(Ports.backLeftBoreEncoder),
                                    backLeftCANCoderOffset),
                            ModulePosition.BACK_RIGHT,
                            new SwerveModule(
                                    3,
                                    new CANSparkMax(Ports.backRightTurnMotor, CANSparkMaxLowLevel.MotorType.kBrushless),
                                    invertMotor(Ports.backRightDriveMotor),
                                    new DutyCycleEncoder(Ports.backRightBoreEncoder),
                                    backRightCANCoderOffset)));
    private final AHRS gyro = new AHRS();

    private final SwerveDriveOdometry m_odometry =
            new SwerveDriveOdometry(
                    kSwerveKinematics,
                    getHeadingRotation2d(),
                    getModulePositions(),
                    new Pose2d());

    private ProfiledPIDController m_xController =
            new ProfiledPIDController(kP_X, 0, kD_X, kThetaControllerConstraints);
    private ProfiledPIDController m_yController =
            new ProfiledPIDController(kP_Y, 0, kD_Y, kThetaControllerConstraints);
    private ProfiledPIDController m_turnController =
            new ProfiledPIDController(kP_Theta, 0, kD_Theta, kThetaControllerConstraints);

    public SwerveSubsystem() {
       gyro.reset();
    }

    public void drive(
            double throttle,
            double strafe,
            double rotation,
            boolean isFieldRelative,
            boolean isOpenLoop) {
        //ToDo See if it is necessary to disable turning while moving.
        throttle *= kMaxSpeedMetersPerSecond;
        strafe *= kMaxSpeedMetersPerSecond;
        rotation *= kMaxRotationRadiansPerSecond;

        ChassisSpeeds chassisSpeeds =
                isFieldRelative
                        ? ChassisSpeeds.fromFieldRelativeSpeeds(
                        throttle, strafe, rotation, getHeadingRotation2d())
                        : new ChassisSpeeds(throttle, strafe, rotation);

        SwerveModuleState[] moduleStates = kSwerveKinematics.toSwerveModuleStates(chassisSpeeds);

        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, kMaxSpeedMetersPerSecond);

        for (SwerveModule module : m_swerveModules.values())
            module.setDesiredState(moduleStates[module.getModuleNumber()], isOpenLoop);
    }

    public void setSwerveModuleStates(SwerveModuleState[] states, boolean isOpenLoop) {
        SwerveDriveKinematics.desaturateWheelSpeeds(states, kMaxSpeedMetersPerSecond);

        for (SwerveModule module : m_swerveModules.values())
            module.setDesiredState(states[module.getModuleNumber()], isOpenLoop);
    }

    public double getHeadingDegrees() {
        return Math.IEEEremainder(gyro.getAngle(), 360);
    }

    public Rotation2d getHeadingRotation2d() {
        return Rotation2d.fromDegrees(getHeadingDegrees());
    }

    public Pose2d getPoseMeters() {
        return m_odometry.getPoseMeters();
    }

    public SwerveModule getSwerveModule(int moduleNumber) {
        return m_swerveModules.get(ModulePosition.values()[moduleNumber]);
    }

    public SwerveModuleState[] getModuleStates() {
        return new SwerveModuleState[] {
                m_swerveModules.get(ModulePosition.FRONT_LEFT).getState(),
                m_swerveModules.get(ModulePosition.FRONT_RIGHT).getState(),
                m_swerveModules.get(ModulePosition.BACK_LEFT).getState(),
                m_swerveModules.get(ModulePosition.BACK_RIGHT).getState()
        };
    }
    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
                m_swerveModules.get(ModulePosition.FRONT_LEFT).getPosition(),
                m_swerveModules.get(ModulePosition.FRONT_RIGHT).getPosition(),
                m_swerveModules.get(ModulePosition.BACK_LEFT).getPosition(),
                m_swerveModules.get(ModulePosition.BACK_RIGHT).getPosition()
        };
    }

    public void updateOdometry() {
        m_odometry.update(getHeadingRotation2d(), getModulePositions());

        for (SwerveModule module : m_swerveModules.values()) {
            var modulePositionFromChassis =
                    kModuleTranslations[module.getModuleNumber()]
                            .rotateBy(getHeadingRotation2d())
                            .plus(getPoseMeters().getTranslation());
            module.setModulePose(
                    new Pose2d(
                            modulePositionFromChassis,
                            module.getHeadingRotation2d().plus(getHeadingRotation2d())));
        }
    }

    public WPI_TalonFX invertMotor(int port) {
        WPI_TalonFX talonFX = new WPI_TalonFX(port);
        talonFX.setInverted(true);
        return talonFX;
    }

    private void updateSmartDashboard() {}

    @Override
    public void periodic() {
        updateOdometry();
        updateSmartDashboard();
    }
}
