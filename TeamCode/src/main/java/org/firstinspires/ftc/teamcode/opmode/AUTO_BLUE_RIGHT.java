package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous_high;
import org.firstinspires.ftc.teamcode.StartingPosition;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class AUTO_BLUE_RIGHT extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Autonomous_high autonomous = new Autonomous_high(this, StartingPosition.BlueRight);

        while (!isStopRequested() && !opModeIsActive()) {
            autonomous.untilStart();
        }
        if (isStopRequested()) return;

        autonomous.new_afterStart();

    }
}
