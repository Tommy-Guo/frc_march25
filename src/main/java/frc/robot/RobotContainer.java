// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.swervedrive.drivebase.TeleopDrive;
import frc.robot.subsystems.Elevator.ElevatorSubsystem;
import frc.robot.subsystems.Elevator.MoveToLevelCommand;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

import java.io.File;
import swervelib.SwerveInputStream;

public class RobotContainer {

        // Replace with CommandPS4Controller or CommandJoystick if needed
        final CommandPS4Controller controller = new CommandPS4Controller(0);
        private final ElevatorSubsystem elevator = new ElevatorSubsystem();
        // private final IntakeShooterSubsystem inShoot = new IntakeShooterSubsystem();
        // The robot's subsystems and commands are defined here...
        private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                        "swerve/neo"));

        /**
         * Converts driver input into a field-relative ChassisSpeeds that is controlled
         * by angular velocity.
         */
        SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                        () -> controller.getLeftY() * -1,
                        () -> controller.getLeftX() * -1)
                        .withControllerRotationAxis(controller::getRightX)
                        .deadband(OperatorConstants.DEADBAND)
                        .scaleTranslation(0.8)
                        .allianceRelativeControl(true);

        /**
         * Clone's the angular velocity input stream and converts it to a fieldRelative
         * input stream.
         */
        SwerveInputStream driveDirectAngle = driveAngularVelocity.copy()
                        .withControllerHeadingAxis(controller::getRightX,
                                        controller::getRightY)
                        .headingWhile(true);

        /**
         * Clone's the angular velocity input stream and converts it to a robotRelative
         * input stream.
         */
        SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                        .allianceRelativeControl(false);

        SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                        () -> -controller.getLeftY(),
                        () -> -controller.getLeftX())
                        .withControllerRotationAxis(() -> controller.getRawAxis(
                                        2))
                        .deadband(OperatorConstants.DEADBAND)
                        .scaleTranslation(0.8)
                        .allianceRelativeControl(true);
        // Derive the heading axis with math!
        SwerveInputStream driveDirectAngleKeyboard = driveAngularVelocityKeyboard.copy()
                        .withControllerHeadingAxis(() -> Math.sin(
                                        controller.getRawAxis(
                                                        2) *
                                                        Math.PI)
                                        *
                                        (Math.PI *
                                                        2),
                                        () -> Math.cos(
                                                        controller.getRawAxis(
                                                                        2) *
                                                                        Math.PI)
                                                        *
                                                        (Math.PI *
                                                                        2))
                        .headingWhile(true);

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                // Configure the trigger bindings
                configureBindings();
                configureButtonBindings();
                DriverStation.silenceJoystickConnectionWarning(true);
                NamedCommands.registerCommand("test", Commands.print("I EXIST"));
        }

        /**
         * Use this method to define your trigger->command mappings. Triggers can be
         * created via the
         * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
         * an arbitrary predicate, or via the
         * named factories in
         * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
         * for
         * {@link CommandXboxController
         * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
         * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick
         * Flight joysticks}.
         */
        private void configureBindings() {
                Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
                Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
                Command driveRobotOrientedAngularVelocity = drivebase.driveFieldOriented(driveRobotOriented);
                Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
                                driveDirectAngle);
                Command driveFieldOrientedDirectAngleKeyboard = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
                Command driveFieldOrientedAnglularVelocityKeyboard = drivebase
                                .driveFieldOriented(driveAngularVelocityKeyboard);
                Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
                                driveDirectAngleKeyboard);

                if (RobotBase.isSimulation()) {
                        drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
                        // } else {
                        // drivebase.setDefaultCommand(driveRobotOrientedAngularVelocity);
                        // drivebase.setDefaultCommand(driveFieldOrientedDirectAngle);
                        drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
                }

                // if (Robot.isSimulation()) {
                // driverXbox.start().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new
                // Pose2d(3, 3, new Rotation2d()))));
                // driverXbox.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());

                // }
                // if (DriverStation.isTest()) {
                // drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides
                // drive command above!

                // driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock,
                // drivebase).repeatedly());
                // driverXbox.y().whileTrue(drivebase.driveToDistanceCommand(1.0, 0.2));
                // driverXbox.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
                // driverXbox.back().whileTrue(drivebase.centerModulesCommand());
                // driverXbox.leftBumper().onTrue(Commands.none());
                // driverXbox.rightBumper().onTrue(Commands.none());
                // } else {
                // driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
                // driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
                // driverXbox.b().whileTrue(
                // drivebase.driveToPose(
                // new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0))));
                // driverXbox.start().whileTrue(Commands.none());
                // driverXbox.back().whileTrue(Commands.none());
                // driverXbox.leftBumper().whileTrue(Commands.runOnce(drivebase::lock,
                // drivebase).repeatedly());
                // driverXbox.rightBumper().onTrue(Commands.none());
                // }

        }

        // Speed > 0 means going down
        // Speed < 0 means going up
        // Trigger sensor == false means it's been triggered.
        // Trigger sensor == true means nothing
        private void configureButtonBindings() {
                new Trigger(controller.R1()::getAsBoolean).whileTrue(new RunCommand(() -> {
                        new MoveToLevelCommand(elevator, 3000); // Stop the elevator when released
                }, elevator));

                // Bind L1 to move the elevator up while held
                new Trigger(controller.L1()::getAsBoolean).whileTrue(new RunCommand(() -> {
                        elevator.move(0.1).schedule(); // Move up using the move command
                }, elevator)).whileFalse(new RunCommand(() -> {
                        elevator.stopElevator(); // Stop the elevator when released
                }, elevator));
        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {
                // An example command will be run in autonomous
                return drivebase.getAutonomousCommand("New Auto");
        }

        public void setMotorBrake(boolean brake) {
                drivebase.setMotorBrake(brake);
        }
}
