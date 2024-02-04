package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Trajectories2 {
    public Action a_to_b;
    public Action b_to_a;
    public Action a_to_c;
    public Action c_to_d;

    public StartingPosition startingPosition;



    public Trajectories2(MecanumDrive drive, StartingPosition startingPosition) {
        this.startingPosition = startingPosition;

        a_to_b = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .lineToY(yCoordinate(-33.5))
                .build();
        b_to_a = drive.actionBuilder(new Pose2d(39, yCoordinate(-33.5), angle(90)))
                .lineToY(yCoordinate(-60))
                .build();
        a_to_c = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .turn(angle(-90))
                .build();
        c_to_d = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(0)))
                .lineToX(35)
                .build();
    }

    double yCoordinate ( double c){
        if (startingPosition == StartingPosition.RedLeft || startingPosition == StartingPosition.RedRight) {
            return c * -1;
        } else {
            return c;
        }
    }

    double angle ( double a){
        if (startingPosition == StartingPosition.RedLeft || startingPosition == StartingPosition.RedRight) {
            return Math.toRadians(a * -1);
        } else {
            return Math.toRadians(a);
        }
    }
}
