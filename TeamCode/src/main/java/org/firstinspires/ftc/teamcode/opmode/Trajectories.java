package org.firstinspires.ftc.teamcode.opmode;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
@Autonomous
public class Trajectories extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0,0,0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0) );

        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(beginPose)
                    .turn(Math.toRadians(90))
                    .lineToY(30)
                    .turn(Math.toRadians(90))
                    .lineToX(0)
                    .turn(Math.toRadians(90))
                    .lineToY(0)
                    .turn(Math.toRadians(90))
                    .build());

            while (opModeIsActive()) {
                telemetry.addData("Status", "Running");
                telemetry.update();
        }
        }
    }
