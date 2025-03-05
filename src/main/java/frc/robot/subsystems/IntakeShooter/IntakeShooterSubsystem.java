package frc.robot.subsystems.IntakeShooter;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeShooterSubsystem extends SubsystemBase {
    private final SparkMax shooterMotor1 = new SparkMax(33, MotorType.kBrushless);
    private final SparkMax shooterMotor2 = new SparkMax(34, MotorType.kBrushless);

    public Command move(double speed) {
        return new RunCommand(() -> {
            spin(speed);
        }, this);
    }

    public Command stop() {
        return new RunCommand(() -> {
            shooterMotor1.set(0);
            shooterMotor2.set(0);
        }, this);
    }

    public void spin(double speed) {
        shooterMotor1.set(speed);
        shooterMotor2.set(speed * -1);
    }
}