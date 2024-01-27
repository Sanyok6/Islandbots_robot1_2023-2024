package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

public class Trajectories2 {

    public StartingPosition startingPosition;

    public Action toPos2pt1;
    public Action toPos2pt2;
    public Action toPos2pt3;
    public Action toBackdropPt1;
    public Action toPos1pt1;
    public Action toPos1pt2;

    public Action toPos3pt1;
    public Action toPos3pt2;
    public Action toBackdropPos1;
    public Action toBackdropPos2;
    public Action toBackdropPos3;

    public Trajectories2(MecanumDrive drive, StartingPosition startingPosition) {
        this.startingPosition = startingPosition;

        // start by aligning to 2nd team prop location
        toPos2pt1 = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .lineToY(yCoordinate(-33.5))
                .build();

        // then align to 3rd team prop location
        toPos1pt1 = drive.actionBuilder(new Pose2d(39, yCoordinate(-33.5), angle(90)))
                .turn(angle(-90))
                .build();
        //place here
        toPos1pt2 = drive.actionBuilder(new Pose2d(39, yCoordinate(-35), angle(0)))
                .lineToX(-33)
                .turn(angle(90))
                .build();

        toPos3pt1 = drive.actionBuilder(new Pose2d(39, yCoordinate(-33), angle(90)))
                .turn(angle(90))
                .build();
        // place here
        toPos3pt2 = drive.actionBuilder(new Pose2d(-33, yCoordinate(-33), angle(0)))
                .lineToX(-34)
                .turn(angle(-90))
                .build();



        // then align to 1st team prop location
        toPos2pt2 = drive.actionBuilder(new Pose2d(36, yCoordinate(-35), angle(0)))
                .lineToY(yCoordinate(12))
                .turn(angle(180))
                .build();

        // place here

        toPos2pt3 = drive.actionBuilder(new Pose2d(36, yCoordinate(-35), angle(0)))
                .lineToY(yCoordinate(12))
                .turn(angle(90))
                .build();

        toBackdropPt1 = drive.actionBuilder(new Pose2d(36, yCoordinate(-35), angle(0)))
                .lineToX(18)
                .turn(angle(180))
                .build();

        // finally, move the the backdrop depending on team prop position
        toBackdropPos2 = drive.actionBuilder(new Pose2d(14, yCoordinate(-35), angle(0)))
                .lineToX(-53)
                .build();
        toBackdropPos1 = drive.actionBuilder(new Pose2d(-53, yCoordinate(-35), angle(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(yCoordinate(-42))
                .build();
        toBackdropPos3 = drive.actionBuilder(new Pose2d(-53, yCoordinate(-35), angle(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(yCoordinate(-27))
                .build();
    }

    public Action getBackdropAlignmentTrajectory(int teamPropLocation) {

        if (teamPropLocation == 1) {
            if (startingPosition == StartingPosition.RedLeft) {
                return toBackdropPos3;
            } else {
                return toBackdropPos1;
            }
        } else if (teamPropLocation == 3) {
            if (startingPosition == StartingPosition.RedLeft) {
                return toBackdropPos1;
            } else {
                return toBackdropPos3;
            }
        } else {
            return (t) -> false;
        }
    }

    // TODO: Use roadrunner builtin pose map
    double yCoordinate(double c) {
        if (startingPosition == StartingPosition.RedLeft || startingPosition == StartingPosition.RedRight) {
            return c * -1;
        } else {
            return  c;
        }
    }

    double angle(double a) {
        if (startingPosition == StartingPosition.RedLeft || startingPosition == StartingPosition.RedRight) {
            return Math.toRadians(a*-1);
        } else {
            return  Math.toRadians(a);
        }
    }

}
