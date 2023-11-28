package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(group = "default")
public class Teleop extends LinearOpMode {

    double target = 0;
    boolean reached = true;
    boolean reversed = true;
    int s = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("leftFront");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("leftBack");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("rightFront");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("rightBack");

//        DcMotor LSmotor = hardwareMap.dcMotor.get("LSmotor");

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        sleep(1000);

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_x * -0.5;
            double x = -gamepad1.left_stick_y;
            double rx = -gamepad1.right_stick_x * 0.6;
            double ls = gamepad2.dpad_up ? 0.9 : (gamepad2.dpad_down ? -0.5 : 0.1);

//            if (ls > 0.5 && LSmotor.getCurrentPosition() < -3000) { ls = 0.1; }
//            else if (ls < 0.2 && LSmotor.getCurrentPosition() >= -125) { ls = 0.1; }

            telemetry.addData("y", gamepad1.left_stick_y);
            telemetry.addData("x", gamepad1.left_stick_x);
            telemetry.addData("rx", -gamepad1.right_stick_x);
            telemetry.update();

            if (gamepad1.x) {
                s++;
                if (s > 100) {
                    reversed = !reversed;
                    s = 0;
                }
            }

            //sensitivity adjustments
            y /= 1.6 * (reversed ? -1 : 1);
            x /= 1.6 * (reversed ? -1 : 1);
            rx /= 2;

            if (gamepad2.a) {
                target = 0;
                reached = false;
            }

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

        }
    }
}
