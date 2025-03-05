// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import swervelib.math.Matter;

public final class Constants {

  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME = 0.13; // s, 20ms + 110ms sprk max velocity lag
  public static final double MAX_SPEED = Units.feetToMeters(14.5);
  // Maximum speed of the robot in meters per second, used to limit acceleration.

  // public static final class AutonConstants
  // {
  //
  // public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0,
  // 0);
  // public static final PIDConstants ANGLE_PID = new PIDConstants(0.4, 0, 0.01);
  // }

  public static final class DrivebaseConstants {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants {

    // Joystick Deadband
    public static final double DEADBAND = 0.1;
    public static final double LEFT_Y_DEADBAND = 0.1;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT = 6;
  }

  public static class ElevatorConstants {
    public static final double kElevatorKp = 5;// 5
    public static final double kElevatorKi = 0;
    public static final double kElevatorKd = 0;//
    public static final double kMaxVelocity = Meters.of(4).per(Second).in(MetersPerSecond);
    public static final double kMaxAcceleration = Meters.of(8).per(Second).per(Second).in(MetersPerSecondPerSecond);
    public static final double kElevatorkS = 0.02;
    public static final double kElevatorkG = 0.9;
    public static final double kElevatorkV = 3.8;
    public static final double kElevatorkA = 0.17;
    public static final double kElevatorRampRate = 0.1;
    public static final double kElevatorGearing = 12.0;
    public static final double kElevatorCarriageMass = 4.0;
    public static final double kElevatorDrumRadius = Units.inchesToMeters(2.0);
    public static final double kElevatorMinHeightMeters = 0.0;
    public static final double kElevatorMaxHeightMeters = 10.25;
    public static final double kElevatorLength = Inches.of(33).in(Meters);
    public static final Distance kElevatorStartingHeightSim = Meters.of(0.0);
    public static final Angle kElevatorStartingAngle = Degrees.of(-90);
    public static final Distance kLaserCANOffset = Inches.of(3);
    public static final double kElevatorDefaultTolerance = Inches.of(1).in(Meters);

    public static double kLowerToScoreHeight = Units.inchesToMeters(6);;
  }
}
