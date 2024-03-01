package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public class Trajectories_high {
    public Action Pos1;
    public Action Pos2;
    public Action Pos3;
    public Action Pos_end;
    public Action Pos_end2;

    public Action toBackdropPos2;

    public Action BackdropToMid2;
    public Action BackdropToMid1;
    public Action BackdropToMid3;

    public Action toBackdropPos1;

    public Action toBackdropPos3;
    public StartingPosition startingPosition;


    public Trajectories_high(MecanumDrive drive, StartingPosition startingPosition) {
        this.startingPosition = startingPosition;

        Pos3 = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .strafeToSplineHeading(new Vector2d(39, yCoordinate(-35)), angle(0))
                .build();

        Pos2 = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .lineToY(yCoordinate(-35))
                .build();

        Pos1 = drive.actionBuilder(new Pose2d(39, yCoordinate(-60), angle(90)))
                .strafeToSplineHeading(new Vector2d(35, -35), angle(180))
                .build();

        Pos_end = drive.actionBuilder(new Pose2d(39, yCoordinate(-35), angle(90)))
                .strafeToSplineHeading(new Vector2d(39, yCoordinate(-58)), angle(0))
                .strafeTo(new Vector2d(-12,yCoordinate(-58)))
                .splineTo(new Vector2d(-48,yCoordinate(-38)), angle(180))
                .build();

        toBackdropPos2 = drive.actionBuilder(new Pose2d(-48, yCoordinate(-38), angle(0)))
                .strafeTo(new Vector2d(-54,yCoordinate(-35)))
                .build();
        toBackdropPos1 = drive.actionBuilder(new Pose2d(-53, yCoordinate(-35), angle(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(-42)
                .build();
        toBackdropPos3 = drive.actionBuilder(new Pose2d(-53, yCoordinate(-35), angle(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(-27)
                .build();

        BackdropToMid2 = drive.actionBuilder(new Pose2d(-51, yCoordinate(-35), angle(0)))
                .strafeTo(new Vector2d(-48,yCoordinate(-38)))
                .build();

        BackdropToMid1 = drive.actionBuilder(new Pose2d(-48, yCoordinate(-42), angle(0)))
                .strafeTo(new Vector2d(-48,yCoordinate(-38)))
                .build();

        BackdropToMid3 = drive.actionBuilder(new Pose2d(-48, yCoordinate(-27), angle(0)))
                .strafeTo(new Vector2d(-48, yCoordinate(-38)))
                .build();

        Pos_end2 = drive.actionBuilder(new Pose2d(-48, yCoordinate(-38), angle(0)))
                .strafeTo(new Vector2d(-48, yCoordinate(-60)))
                .strafeTo(new Vector2d(-60, yCoordinate(-60)))
                .build();


    }

    public Action getTeamPropPlacementTrajectory(int teamPropLocation) {

        if (teamPropLocation == 1) {
            return Pos1;
        } else if (teamPropLocation == 2){
            return Pos2;
        } else if (teamPropLocation == 3){
            return Pos3;
        } else {
            return (t) -> false;
        }
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

    public Action getReturnPath(int teamPropLocation) {

        if (teamPropLocation == 1) {
            if (startingPosition == StartingPosition.RedLeft) {
                return BackdropToMid3;
            } else {
                return BackdropToMid1;
            }
        } else if (teamPropLocation == 3) {
            if (startingPosition == StartingPosition.RedLeft) {
                return BackdropToMid3;
            } else {
                return BackdropToMid1;
            }
        } else {
            return BackdropToMid2;
        }
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
