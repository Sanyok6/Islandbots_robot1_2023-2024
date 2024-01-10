package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.LinearSlide;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ToggleButton;

@TeleOp
public class TELEOP extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        // li_servo stands for "left intake servo"
        // We use the CRServo class because the intake servos are constant rotation servos
        CRServo li_servo = hardwareMap.crservo.get("li_servo");
        CRServo ri_servo = hardwareMap.crservo.get("ri_servo");

        // to_servo stands for "top outtake servo", bo_servo stands for "bottom outtake servo"
        Servo to_servo = hardwareMap.servo.get("to_servo");
        Servo bo_servo = hardwareMap.servo.get("bo_servo");
        Servo outtake_rotate = hardwareMap.servo.get("outtake_rotate");

        Servo airplane_servo = hardwareMap.servo.get("airplane_servo");

        Servo intake_lift = hardwareMap.servo.get("intake_lift");

        DistanceSensor br_dist = hardwareMap.get(DistanceSensor.class, "br_dist");
        DistanceSensor bl_dist = hardwareMap.get(DistanceSensor.class, "bl_dist");

        LinearSlide linear_slide = new LinearSlide(hardwareMap.dcMotor.get("ls_motor"));

        // setup toggle buttons for controlling outtake
        ToggleButton x_toggle = new ToggleButton();
        ToggleButton b_toggle = new ToggleButton();
        ToggleButton y_toggle = new ToggleButton();

        // vibrate the controller for extra driver feedback when button toggled
        x_toggle.onToggle = () -> {gamepad2.rumble(200); b_toggle.toggled=false;};
        b_toggle.onToggle = () -> gamepad2.rumble(200);
        y_toggle.onToggle = () -> gamepad2.rumble(200);


        waitForStart();

        while (opModeIsActive()) {
            if (!gamepad1.a) {
                double x = -gamepad1.left_stick_y;
                double y = -gamepad1.left_stick_x;
                double h = drive.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                double multiplier = gamepad1.left_trigger > 0.9 ? 0.3 : (gamepad1.right_trigger > 0.9 ? 0.8 : 0.55);

                double rotatedX = x * Math.cos(-h) - y * Math.sin(-h);
                double rotatedY = x * Math.sin(-h) + y * Math.cos(-h);

                drive.setDrivePowers(
                        new PoseVelocity2d(
                                new Vector2d(rotatedX * multiplier, rotatedY * multiplier),
                                -gamepad1.right_stick_x / 2 - rotatedX / 10 - rotatedY / 12
                                // ^^^ compensate for weight unbalance
                        )
                );
            } else {
                double y = gamepad1.left_stick_y/2;
                double d = Math.min(br_dist.getDistance(DistanceUnit.INCH), bl_dist.getDistance(DistanceUnit.INCH));

                double angle = drive.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                angle = angle < -Math.PI/2 ? angle+2*Math.PI : angle;
                angle-=Math.PI/2;

                drive.setDrivePowers(
                        new PoseVelocity2d(
                                new Vector2d(-Math.min((d-2.75)/25, 0.3), y),
                                Math.min(-angle, angle>0 ? -0.5 : 0.5)
                        )
                );
            }

            if (gamepad1.y) {
                drive.imu.resetYaw();
            }

            // this controls the intake
            if (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0) {
                li_servo.setPower(-1);
                ri_servo.setPower(1);
            } else if (gamepad2.left_bumper || gamepad2.right_bumper) {
                li_servo.setPower(1);
                ri_servo.setPower(-1);
            } else {
                li_servo.setPower(0);
                ri_servo.setPower(0);
            }

            // this controls linear slide
            if (gamepad2.dpad_up) {
                linear_slide.manualMove(0.8);
            } else if (gamepad2.dpad_down) {
                linear_slide.manualMove(-0.5);
            }  else if (gamepad2.a) {
                linear_slide.setTarget(0);
            } else {
                linear_slide.moveTowardsTarget();
            }

            // this controls outtake
            x_toggle.updateState(gamepad2.x);
            b_toggle.updateState(gamepad2.b);
            y_toggle.updateState(gamepad2.y);

            to_servo.setPosition(x_toggle.toggled ? 0.95 : 0.4);
            bo_servo.setPosition(b_toggle.toggled ? 0.3 : 0);
            outtake_rotate.setPosition(y_toggle.toggled ? 0.45 : x_toggle.toggled ? 0.6 : 0.95);

            intake_lift.setPosition(x_toggle.toggled ? 0 : 0.5);


            // this controls airplane launcher
            if (gamepad1.dpad_left && gamepad2.dpad_left) {
                airplane_servo.setPosition(0.9);
            } else {
                airplane_servo.setPosition(1);
            }

            telemetry.update();
        }
    }
}
