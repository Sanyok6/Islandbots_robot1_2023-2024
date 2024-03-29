package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// TODO: THIS CODE CAN BE MADE INTO ONE FUNCTION!!!

public class Autonomous_low {
    public LinearOpMode opMode;

    public StartingPosition startingPosition;

    MecanumDrive drive;
    TeamPropDetector detector;

    CRServo li_servo;
    CRServo ri_servo;
    Servo outtake_rotate;
    Servo to_servo;
    Servo intake_lift;

    LinearSlide linearSlide;

    Servo bo_servo;
    DcMotor sidePanelLED;

    public Autonomous_low(LinearOpMode opMode, StartingPosition startingPosition) {
        this.opMode = opMode;
        HardwareMap hardwareMap = opMode.hardwareMap;

        this.startingPosition = startingPosition;

        // initialize roadrunner
        drive = new MecanumDrive(hardwareMap, new Pose2d(-15,  startingPosition == StartingPosition.RedLeft ? 60 : -60, startingPosition == StartingPosition.RedLeft ? Math.toRadians(-90) : Math.toRadians(90)));
        drive.imu.resetYaw();

        // initialize team prop detector
        detector = new TeamPropDetector(
                hardwareMap.get(DistanceSensor.class, "fl_dist"),
                hardwareMap.get(DistanceSensor.class, "fr_dist"));
        detector.readPosition();

        // initialize servos
        li_servo = hardwareMap.crservo.get("li_servo");
        ri_servo = hardwareMap.crservo.get("ri_servo");
        li_servo.setPower(0);
        ri_servo.setPower(0);

        to_servo = hardwareMap.servo.get("to_servo");
        to_servo.setPosition(0.95);
        bo_servo = hardwareMap.servo.get("bo_servo");
        bo_servo.setPosition(0.5);

        outtake_rotate = hardwareMap.servo.get("outtake_rotate");
        outtake_rotate.setPosition(0.6);

        intake_lift = hardwareMap.servo.get("intake_lift");
        intake_lift.setPosition(0);

        // initialize linear slide
        linearSlide = new LinearSlide(hardwareMap.dcMotor.get("ls_motor"));

        // enable side panels
         sidePanelLED = hardwareMap.dcMotor.get("sidePanelLED");
         sidePanelLED.setPower(-1);
    }


    public void untilStart() {
        detector.readPosition();
        opMode.telemetry.addData("Team prop location", detector.position);
        opMode.telemetry.update();
    }

    public void afterStart() {
        Trajectories trajectories = new Trajectories(drive, startingPosition);

        intake_lift.setPosition(0.22);

        Actions.runBlocking(
                new SequentialAction(
                        trajectories.toPos2,
                        new OuttakeIfAboveCorrectLocation(2),

                        trajectories.toPos3pt1,
                        trajectories.toPos3pt2,

                        new OuttakeIfAboveCorrectLocation(startingPosition == StartingPosition.RedLeft ? 1 : 3),
                        trajectories.toPos1,

                        new OuttakeIfAboveCorrectLocation(startingPosition == StartingPosition.RedLeft ? 3 : 1),
                        trajectories.toPos1pt2,

                        trajectories.toBackdropPos2,
                        trajectories.getBackdropAlignmentTrajectory(detector.position),

                        new PlaceOnBackdrop()
                )
        );

    }

        public void new_afterStart() {

            Trajectories_low trajectories = new Trajectories_low(drive, startingPosition);

            intake_lift.setPosition(0.22);

            Actions.runBlocking(
                    new SequentialAction(

                        trajectories.getTeamPropPlacementTrajectory(detector.position),

                        new OuttakeIfAboveCorrectLocation(detector.position),

                        trajectories.Pos_end,
                        trajectories.toBackdropPos2,
                        trajectories.getBackdropAlignmentTrajectory(detector.position),
                        new PlaceOnBackdrop(),
                        trajectories.getReturnPath(detector.position),

                        trajectories.Pos_end2

                )
        );
    }

    public void new_afterStart_low() {
        Trajectories_low trajectories = new Trajectories_low(drive, startingPosition);

        intake_lift.setPosition(0.22);

        Actions.runBlocking(
                new SequentialAction(

                        trajectories.getTeamPropPlacementTrajectory(detector.position),

                        new OuttakeIfAboveCorrectLocation(detector.position),

                        trajectories.Pos_end,
                        trajectories.toBackdropPos2,
                        trajectories.getBackdropAlignmentTrajectory(detector.position),
                        new PlaceOnBackdrop(),

                        trajectories.Pos_end2

                )
        );
    }

    public class OuttakeIfAboveCorrectLocation implements Action {
        int currentLocation;
        public OuttakeIfAboveCorrectLocation(int currentLocation) {
            this.currentLocation = currentLocation;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (currentLocation == detector.position) {
                li_servo.setPower(1);
                ri_servo.setPower(-1);
                opMode.sleep(1000);
                li_servo.setPower(0);
                ri_servo.setPower(0);
            }
            return false;
        }
    }

    public class PlaceOnBackdrop implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            linearSlide.setTarget(300);

            // while loops should not actually be used
            while (!linearSlide.reachedTarget() && !opMode.isStopRequested()) {
                linearSlide.moveTowardsTarget();
            }
            linearSlide.manualMove(0.22);

            outtake_rotate.setPosition(1.3);
            opMode.sleep(500);
            bo_servo.setPosition(0.3);
            opMode.sleep(1000);
            bo_servo.setPosition(0.5);
            outtake_rotate.setPosition(0.6);
            opMode.sleep(500);

            return false;
        }
    }

}
