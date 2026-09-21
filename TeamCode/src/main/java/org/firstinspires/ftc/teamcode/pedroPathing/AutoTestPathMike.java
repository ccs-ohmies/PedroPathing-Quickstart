package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TestPedroPAthingMike")

public class AutoTestPathMike extends OpMode {
    private final Pose firstPose = new Pose(0,0, Math.toRadians(90));
    private final Pose secondPose = new Pose(0,24, Math.toRadians(90));
    private final Pose secondTurnPose = new Pose(0,24, Math.toRadians(180));
    private final Pose thirdPose = new Pose(-24, 24, Math.toRadians(270));
    private final Pose fourthPose = new Pose(-24, -24, Math.toRadians(0));
    private final Pose fifthPose = new Pose(24, -24, Math.toRadians(90));
    private final Pose sixthPose = new Pose(24, 24, Math.toRadians(180));
    private final Pose seventhPose = new Pose(0, 24, Math.toRadians(270));
    private Follower follower = null;

    private Path firstPath = null;
    private Path firstTurnPath = null;
    private Path secondPath = null;
    private Path thirdPath = null;
    private Path fourthPath = null;
    private Path fifthPath = null;
    private Path sixthPath = null;
    private Path seventhPath = null;
    private enum PathState {
        PATH_START,
        PATH_TO_FIRST_POSITION,
        FIRST_POSITION_TURN,
        PATH_TO_SECOND_POSITION,
        PATH_TO_THIRD_POSITION,
        PATH_TO_FOURTH_POSITON,
        PATH_TO_FIFTH_POSITON,
        PATH_TO_SIXTH_POSITON,
        PATH_TO_SEVENTH_POSITON,
        PATH_FINISHED
    }
    private PathState pathState;
    private ElapsedTime pathTime;
    public static final double POSITION_WAIT_TIME_MS = 5000;

    private void buildPaths() {
        firstPath = new Path(new BezierLine(firstPose, secondPose));
        firstPath.setLinearHeadingInterpolation(firstPose.getHeading(), secondPose.getHeading());
        firstTurnPath = new Path(new BezierLine(secondPose, secondTurnPose));
        firstTurnPath.setLinearHeadingInterpolation(secondPose.getHeading(), secondTurnPose.getHeading());
        secondPath = new Path(new BezierLine(secondPose, thirdPose));
        secondPath.setLinearHeadingInterpolation(secondPose.getHeading(), thirdPose.getHeading());
        thirdPath = new Path(new BezierLine(thirdPose, fourthPose));
        thirdPath.setLinearHeadingInterpolation(thirdPose.getHeading(), fourthPose.getHeading());
        fourthPath = new Path(new BezierLine(fourthPose, fifthPose));
        fourthPath.setLinearHeadingInterpolation(fourthPose.getHeading(), fifthPose.getHeading());
        fifthPath = new Path(new BezierLine(fifthPose, sixthPose));
        fifthPath.setLinearHeadingInterpolation(fifthPose.getHeading(), sixthPose.getHeading());
        sixthPath = new Path(new BezierLine(sixthPose, seventhPose));
        sixthPath.setLinearHeadingInterpolation(sixthPose.getHeading(), seventhPose.getHeading());
        seventhPath = new Path(new BezierLine(seventhPose, firstPose));
        seventhPath.setLinearHeadingInterpolation(seventhPose.getHeading(), firstPose.getHeading());
    }

    private void updatePathState(Path nextPath, PathState nextPathState) {
        if (pathTime.milliseconds() >= POSITION_WAIT_TIME_MS) {
            pathState = nextPathState;
            if (pathState != null) {
                follower.followPath(nextPath);
            }
            pathTime.reset();
            telemetry.addData("New Path State=", nextPathState);
        } else {
            telemetry.addData("Waiting for milliseconds=", POSITION_WAIT_TIME_MS - pathTime.milliseconds());
        }
    }

    @Override
    public void init() {
        pathState = PathState.PATH_START;
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setPose(firstPose);
        pathTime = new ElapsedTime();
        telemetry.addLine("Init method Triggered");
    }

    @Override
    public void loop() {
        follower.update();

        if (follower.isBusy()) {
            telemetry.addData("Robot is busy following a path state=", pathState);
            pathTime.reset();
            return;
        }

        switch (pathState) {
            case PATH_START:
                pathState = PathState.PATH_TO_FIRST_POSITION;
                follower.followPath(firstPath);
                break;
            case PATH_TO_FIRST_POSITION:
                updatePathState(firstTurnPath, PathState.FIRST_POSITION_TURN);
                break;
            case FIRST_POSITION_TURN:
                updatePathState(secondPath, PathState.PATH_TO_SECOND_POSITION);
                break;
            case PATH_TO_SECOND_POSITION:
                updatePathState(thirdPath, PathState.PATH_TO_THIRD_POSITION);
                break;
            case PATH_TO_THIRD_POSITION:
                updatePathState(fourthPath, PathState.PATH_TO_FOURTH_POSITON);
                break;
            case PATH_TO_FOURTH_POSITON:
                updatePathState(fifthPath, PathState.PATH_TO_FIFTH_POSITON);
                break;
            case PATH_TO_FIFTH_POSITON:
                updatePathState(sixthPath, PathState.PATH_TO_SIXTH_POSITON);
                break;
            case PATH_TO_SIXTH_POSITON:
                updatePathState(seventhPath, PathState.PATH_TO_SEVENTH_POSITON);
                break;
            case PATH_TO_SEVENTH_POSITON:
                updatePathState(null, PathState.PATH_FINISHED);
                break;
            case PATH_FINISHED:
                stop();
                break;
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
