package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeamPropDetector {
    public int position;
    private DistanceSensor left_sensor;
    private DistanceSensor right_sensor;

    public TeamPropDetector(DistanceSensor left_sensor, DistanceSensor right_sensor) {
        this.left_sensor = left_sensor;
        this.right_sensor = right_sensor;
    }

    public void readPosition() {
        double left_dist = left_sensor.getDistance(DistanceUnit.CM);
        double right_dist = right_sensor.getDistance(DistanceUnit.CM);

        if (left_dist < 65) {
            position = 1;
        } else if (right_dist < 65) {
            position = 3;
        } else {
            position = 2;
        }
    }

}
