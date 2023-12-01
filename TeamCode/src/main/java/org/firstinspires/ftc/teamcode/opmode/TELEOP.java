package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.LinearSlide;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ToggleButton;

@TeleOp
public class TELEOP extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        // li_servo stands for "left intake servo"
        // We use the CRServo class because the intake servos are constant rotation servos
        CRServo li_servo = hardwareMap.crservo.get("li_servo");
        CRServo ri_servo = hardwareMap.crservo.get("ri_servo");

        // to_servo stands for "top outtake servo", bo_servo stands for "bottom outtake servo"
        Servo to_servo = hardwareMap.servo.get("to_servo");
        Servo bo_servo = hardwareMap.servo.get("bo_servo");
        Servo outtake_rotate = hardwareMap.servo.get("outtake_rotate");

        LinearSlide linear_slide = new LinearSlide(hardwareMap.dcMotor.get("ls_motor"));

        // setup toggle buttons for controlling outtake
        ToggleButton x_toggle = new ToggleButton();
        ToggleButton b_toggle = new ToggleButton();
        ToggleButton y_toggle = new ToggleButton();

        // vibrate the controller for extra driver feedback when button toggled
        x_toggle.onToggle = () -> gamepad1.rumble(200);
        b_toggle.onToggle = () -> gamepad1.rumble(200);
        y_toggle.onToggle = () -> gamepad1.rumble(200);

        waitForStart();

        while (opModeIsActive()) {
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y / 2,
                            -gamepad1.left_stick_x / 2
                    ),
                    -gamepad1.right_stick_x / 3
            ));

            //this drastically reduces cycle time, but we may still need to use it in future
            //drive.updatePoseEstimate();

            // this controls the intake
            if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {
                li_servo.setPower(-1);
                ri_servo.setPower(1);
            } else if (gamepad1.left_bumper || gamepad1.right_bumper) {
                li_servo.setPower(1);
                ri_servo.setPower(-1);
            } else {
                li_servo.setPower(0);
                ri_servo.setPower(0);
            }

            // this controls linear slide
            if (gamepad1.dpad_up) {
                linear_slide.manualMove(0.8);
            } else if (gamepad1.dpad_down) {
                linear_slide.manualMove(-0.6);
            }  else if (gamepad1.a) {
                linear_slide.setTarget(0);
            } else {
                linear_slide.moveTowardsTarget();
            }

            // this controls outtake
            x_toggle.updateState(gamepad1.x);
            b_toggle.updateState(gamepad1.b);
            y_toggle.updateState(gamepad1.y);

            to_servo.setPosition(x_toggle.toggled ? 0.95 : 0.85);
            bo_servo.setPosition(b_toggle.toggled ? 0.3 : 0);
            outtake_rotate.setPosition(y_toggle.toggled ? 0.5 : 0.925);

        }
    }
}
