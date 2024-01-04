package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TeamPropDetector;

@Autonomous
public class AUTO_RED_RIGHT extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // initialize roadrunner
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(35, -60, Math.toRadians(90)));
        drive.imu.resetYaw();

        // initialize team prop detector
        TeamPropDetector detector = new TeamPropDetector(
                hardwareMap.get(DistanceSensor.class, "fl_dist"),
                hardwareMap.get(DistanceSensor.class, "fr_dist"));
        detector.readPosition();

        // generate trajectories

        // start by aligning to 2nd team prop location
        Action toPos2 = drive.actionBuilder(new Pose2d(35, -60, Math.toRadians(90)))
                .lineToY(-34)
                .build();

        // then align to 3rd team prop location
        Action toPos3 = drive.actionBuilder(new Pose2d(35, -34, Math.toRadians(90)))
                .turn(Math.toRadians(-90))
                .build();

        // then align to 1st team prop location
        Action toPos1 = drive.actionBuilder(new Pose2d(35, -34, 0))
                .lineToX(14)
                .build();

        // finally, move the the backdrop
        Action toBackdrop = drive.actionBuilder(new Pose2d(14, -34, 0))
                .lineToX(-51)
                .build();

        // wait for start
        while(!isStopRequested() && !opModeIsActive()) {
            detector.readPosition();
            telemetry.addData("Team prop location", detector.position);
            telemetry.update();
        }
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        toPos2,
                        toPos3,
                        toPos1,

                        (telemetryPacket) -> {
                            telemetry.addLine("test");
                            telemetry.update();
                            return false;
                        },

                        toBackdrop
                )
        );
    }
}
