package frc.robot.subsystems.Elevator;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// going down is positive
// going up is negative
// sensor == false means it's triggered
// sensor = =true mean its not triggered

public class ElevatorSubsystem extends SubsystemBase {
    private final SparkMax elevatorMotor1 = new SparkMax(31, MotorType.kBrushless);
    private final SparkMax elevatorMotor2 = new SparkMax(32, MotorType.kBrushless);

    Encoder elevatorEncoder = new Encoder(0, 1);

    DigitalInput bottomSwitch = new DigitalInput(2);

    private boolean isStopped = false;

    public Command printt(Object obj) {
        return new RunCommand(() -> {
            System.out.println(obj.toString());
        }, this);
    }

    public void resetPos() {
        if (bottomSwitch.get() == false) {
            elevatorMotor1.set(0);
            elevatorMotor2.set(0);
            elevatorEncoder.reset();
        }
        if (bottomSwitch.get() == true) {
            move(0.15);
        }
    }

    public Command resetPosition() {
        return new RunCommand(() -> {
            resetPos();
        }, this);
    }

    public boolean isElevatorStopped() {
        return isStopped;
    }

    // if the direction is positive then move down
    // unless the switch is triggered, then don't move at all
    // if the direction is negative then move up
    // unless the encoder position is above 10000

    public Command move(double speed) {
        return new RunCommand(() -> {
            printEncoder();
            if (speed > 0) {
                if (bottomSwitch.get() == false) {
                    stopElevator();
                    resetEncoder();
                    System.out.println("Exit 1");
                } else {
                    setElevatorSpeed(speed);
                    System.out.println("Exit 2");
                }
            } else if (speed < 0) {
                if (getEncoderDistance() > 10000) {
                    System.out.println("Exit 3");
                    stopElevator();
                    resetEncoder();
                } else {
                    System.out.println("Exit 4");
                    setElevatorSpeed(speed);
                }
            }
        }, this);

    }

    public void testMove(double speed) {
        printEncoder();
        if (speed > 0) {
            if (bottomSwitch.get() == false) {
                stopElevator();
                resetEncoder();
                System.out.println("Exit 1");
            } else {
                setElevatorSpeed(speed);
                System.out.println("Exit 2");
            }
        } else if (speed < 0) {
            if (getEncoderDistance() > 10000) {
                System.out.println("Exit 3");
                stopElevator();
                resetEncoder();
            } else {
                System.out.println("Exit 4");
                setElevatorSpeed(speed);
            }
        }
    }

    public Command stop() {
        return new RunCommand(() -> {
            stopElevator();
            printEncoder();
        }, this);
    }

    public void setElevatorSpeed(double speed) {
        elevatorMotor1.set(speed);
        elevatorMotor2.set(speed * -1);
        isStopped = false;
    }

    public void stopElevator() {
        elevatorMotor1.set(0);
        elevatorMotor2.set(0);
        isStopped = true;
    }

    public void resetEncoder() {
        elevatorEncoder.reset();
    }

    public void printEncoder() {
        System.out.print("Encoder Value: ");
        System.out.println(getEncoderDistance());
    }

    private final double encoderResolution = 0.75;

    public double getEncoderDistance() {
        return elevatorEncoder.getDistance() * encoderResolution;
    }
}