//RedLeft
myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-38, -60, Math.toRadians(90)))
        .lineToY(-35)
        .strafeToSplineHeading(new Vector2d(-38, -58), Math.toRadians(0))
        .strafeTo(new Vector2d(12,-58))
        .splineTo(new Vector2d(48,-38), Math.toRadians(0))
        .strafeTo(new Vector2d(48, -60))
        .strafeTo(new Vector2d(60, -60))
        .build());

//RedRight
myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(15, -60, Math.toRadians(90)))
        .lineToY(-35)
        .strafeToSplineHeading(new Vector2d(15, -58), Math.toRadians(0))
        .strafeTo(new Vector2d(48,-38))
        .strafeTo(new Vector2d(48, -60))
        .strafeTo(new Vector2d(60, -60))
        .build());

//BlueLeft
myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-38, 60, Math.toRadians(-90)))
        .lineToY(35)
        .strafeToSplineHeading(new Vector2d(-38, 58), Math.toRadians(0))
        .strafeTo(new Vector2d(12,58))
        .splineTo(new Vector2d(48,38), Math.toRadians(0))
        .strafeTo(new Vector2d(48, 60))
        .strafeTo(new Vector2d(60, 60))
        .build());

//BlueRight
myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(15, 60, Math.toRadians(90)))
        .lineToY(35)
        .strafeToSplineHeading(new Vector2d(15, 58), Math.toRadians(0))
        .strafeTo(new Vector2d(48,38))
        .strafeTo(new Vector2d(48, 60))
        .strafeTo(new Vector2d(60, 60))
        .build());
