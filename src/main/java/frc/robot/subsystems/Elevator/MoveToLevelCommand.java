package frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj2.command.Command;

public class MoveToLevelCommand extends Command {

  private final ElevatorSubsystem elevator;
  private final double targetDistance;

  private static final double THRESHOLD = 100.0;

  private static final double min_speed = 0.1;
  private static final double max_speed = 0.3;

  public MoveToLevelCommand(ElevatorSubsystem elevator, double targetDistance) {
    this.elevator = elevator;
    this.targetDistance = targetDistance;
    addRequirements(elevator);
  }

  @Override
  public void execute() {
    if (elevator.isElevatorStopped()) {
      return; // Do nothing if the elevator is stopped
    }

    double currentDistance = elevator.getEncoderDistance();
    double distanceToTarget = targetDistance - currentDistance;
    double speed = calculateSpeed(distanceToTarget);

    System.out.print("Distance To Target: ");
    System.out.println(distanceToTarget);

    System.out.print("Calculated Speed: ");
    System.out.println(speed);

    if (distanceToTarget > 0) {
      System.out.print("GOING UP");
      elevator.testMove(-0.1);
    } else if (distanceToTarget < 0) {
      System.out.print("GOING DOWN");
      elevator.testMove(0.1);
    }
    // }
    elevator.printEncoder();
    // }
  }

  private double calculateSpeed(double distanceToTarget) {
    double absDistance = Math.abs(distanceToTarget);
    if (absDistance > targetDistance / 2) {
      return max_speed;
    } else {
      double speed = min_speed + (max_speed - min_speed) * (absDistance / (targetDistance / 2));
      if (speed > max_speed) {
        return max_speed;
      } else if (speed < min_speed) {
        return min_speed;
      }
      return speed;
    }
  }

  @Override
  public boolean isFinished() {
    if (elevator.isElevatorStopped()) {
      return true; // Do nothing if the elevator is stopped
    }
    double currentDistance = elevator.getEncoderDistance();
    return Math.abs(currentDistance - targetDistance) <= THRESHOLD;
  }

  @Override
  public void end(boolean interrupted) {
    elevator.stopElevator();
  }
}