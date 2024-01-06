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

        Servo intake_lift = hardwareMap.servo.get("intake_lift");
        intake_lift.setPosition(0);

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

        // finally, move the the backdrop
        Action toBackdrop = drive.actionBuilder(new Pose2d(14, -34, Math.toRadians(0)))
                .lineToX(-51)
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

                        toBackdrop
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
