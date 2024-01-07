package org.firstinspires.ftc.teamcode.opmode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.LinearSlide;
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

        // initialize servos
        CRServo li_servo = hardwareMap.crservo.get("li_servo");
        CRServo ri_servo = hardwareMap.crservo.get("ri_servo");
        li_servo.setPower(0);
        ri_servo.setPower(0);

        Servo outtake_rotate = hardwareMap.servo.get("outtake_rotate");
        outtake_rotate.setPosition(0.6);

        Servo to_servo = hardwareMap.servo.get("to_servo");
        to_servo.setPosition(0.95);
        Servo bo_servo = hardwareMap.servo.get("bo_servo");
        bo_servo.setPosition(0);

        Servo intake_lift = hardwareMap.servo.get("intake_lift");
        intake_lift.setPosition(0);

        // initialize linear slide
        LinearSlide linearSlide = new LinearSlide(hardwareMap.dcMotor.get("ls_motor"));

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
        Action toPos1 = drive.actionBuilder(new Pose2d(35, -34, Math.toRadians(0)))
                .lineToX(14)
                .build();

        // finally, move the the backdrop depending on team prop position
        Action toBackdropPos2 = drive.actionBuilder(new Pose2d(14, -34, Math.toRadians(0)))
                .lineToX(-51)
                .build();
        Action toBackdropPos1 = drive.actionBuilder(new Pose2d(-51, -34, Math.toRadians(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(-42)
                .build();
        Action toBackdropPos3 = drive.actionBuilder(new Pose2d(-51, -34, Math.toRadians(0)))
                .setTangent(Math.toRadians(-90))
                .lineToY(-30)
                .build();



        // wait for start
        while(!isStopRequested() && !opModeIsActive()) {
            detector.readPosition();
            telemetry.addData("Team prop location", detector.position);
            telemetry.update();
        }
        if (isStopRequested()) return;

        // lower intake
        intake_lift.setPosition(0.5);

        // start movement
        Actions.runBlocking(
                new SequentialAction(
                        toPos2,
                        new outtakeIfAboveCorrectLocation(li_servo, ri_servo, 2, detector.position),

                        toPos3,
                        new outtakeIfAboveCorrectLocation(li_servo, ri_servo, 3, detector.position),

                        toPos1,
                        new outtakeIfAboveCorrectLocation(li_servo, ri_servo, 1, detector.position),

                        toBackdropPos2,

                        detector.position == 1 ? toBackdropPos1 : toBackdropPos3,

                        // TODO: create a class to handle this
                        (telemetryPacket) -> {
                            linearSlide.setTarget(300);

                            // while loops should not actually be used
                            while (!linearSlide.reachedTarget() && !isStopRequested()) {
                                linearSlide.moveTowardsTarget();
                            }
                            linearSlide.manualMove(0.2);

                            outtake_rotate.setPosition(0.45);
                            sleep(500);
                            bo_servo.setPosition(0.3);
                            sleep(1000);
                            bo_servo.setPosition(0);
                            outtake_rotate.setPosition(0.6);
                            sleep(500);

                            return false;
                        }

                )
        );
    }

    // Places purple pixel in correct location
    class outtakeIfAboveCorrectLocation implements Action {
        public int currentPos;
        public int teamPropPos;

        public CRServo li_servo;
        public CRServo ri_servo;

        public outtakeIfAboveCorrectLocation(CRServo li_servo, CRServo ri_servo, int currentPos, int teamPropPos) {
            this.currentPos = currentPos;
            this.teamPropPos = teamPropPos;
            this.li_servo = li_servo;
            this.ri_servo = ri_servo;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (currentPos == teamPropPos) {
                li_servo.setPower(1);
                ri_servo.setPower(-1);
                sleep(1000);
                li_servo.setPower(0);
                ri_servo.setPower(0);
            }
            return false;
        }
    }

}
