package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous_low;
import org.firstinspires.ftc.teamcode.StartingPosition;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class AUTO_RED_RIGHT extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Autonomous_low autonomous = new Autonomous_low(this, StartingPosition.RedRight);

        while (!isStopRequested() && !opModeIsActive()) {
            autonomous.untilStart();
        }
        if (isStopRequested()) return;

        autonomous.new_afterStart();

    }
}
