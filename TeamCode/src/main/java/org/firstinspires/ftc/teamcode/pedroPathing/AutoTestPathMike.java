package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestPedroPAthingMike")

public class AutoTestPathMike extends OpMode {
    private final Pose startPose = new Pose(0,0, Math.toRadians(90));
    private final Pose secondPose = new Pose(0,24, Math.toRadians(180));
    private final Pose thirdPose = new Pose(-24, 24, Math.toRadians(270));
    private final Pose fourthPose = new Pose(-24, -24, Math.toRadians(0));
    private final Pose fifthPose = new Pose(24, -24, Math.toRadians(90));
    private final Pose sixthPose = new Pose(24, 24, Math.toRadians(180));
    private final Pose seventhPose = new Pose(0, 24, Math.toRadians(270));
    private Follower follower = null;
    private PathChain testPath = null;
    boolean startPath = false;
    public void buildPaths() {
        testPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, secondPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), secondPose.getHeading())
                .addPath(new BezierLine(secondPose, thirdPose))
                .setLinearHeadingInterpolation(secondPose.getHeading(), thirdPose.getHeading())
                .addPath(new BezierLine(thirdPose, fourthPose))
                .setLinearHeadingInterpolation(thirdPose.getHeading(), fourthPose.getHeading())
                .addPath(new BezierLine(fourthPose, fifthPose))
                .setLinearHeadingInterpolation(fourthPose.getHeading(), fifthPose.getHeading())
                .addPath(new BezierLine(fifthPose, sixthPose))
                .setLinearHeadingInterpolation(fifthPose.getHeading(), sixthPose.getHeading())
                .addPath(new BezierLine(sixthPose, seventhPose))
                .setLinearHeadingInterpolation(sixthPose.getHeading(), seventhPose.getHeading())
                .addPath(new BezierLine(seventhPose, startPose))
                .setLinearHeadingInterpolation(seventhPose.getHeading(), startPose.getHeading())
                .build();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setPose(startPose);
        telemetry.addLine("Init method Triggered");
    }

    @Override
    public void loop() {
        follower.update();

        if (!startPath) {
            telemetry.addLine("Start following test path");
            startPath = true;
            follower.followPath(testPath);
        } else if (!follower.isBusy()) {
            stop();
        } else {
            telemetry.addLine("Following test path in progress...");
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
