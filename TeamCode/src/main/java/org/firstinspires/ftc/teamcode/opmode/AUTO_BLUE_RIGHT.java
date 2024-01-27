package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.StartingPosition;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class AUTO_BLUE_RIGHT extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Autonomous autonomous = new Autonomous(this, StartingPosition.BlueRight);

        while (!isStopRequested() && !opModeIsActive()) {
            autonomous.untilStart();
        }
        if (isStopRequested()) return;

        autonomous.afterStart();

    }
}
