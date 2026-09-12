package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.util.Timer;


@TeleOp(name = "AutoMove")

public class SampleAutoPathingJP extends OpMode {


    private final Pose StartPose = new Pose(0,0, Math.toRadians(90));
    private final Pose SecondPose = new Pose (0,261, Math.toRadians(90));
    //private final Pose ThirdPose = new Pose ()
    private PathChain driveStartPosToSecondPos;
    private Follower follower;
    private Timer pathTimer, opModeTimer;
public void buildPath(){
    driveStartPosToSecondPos=follower.pathBuilder()
            .addPath(new BezierLine(StartPose,SecondPose))
            .setLinearHeadingInterpolation(StartPose.getHeading(),SecondPose.getHeading())
            .build();
}

public void statePathUpdate(){

    switch (pathState){
        case START_POS:
            follower.followPath(driveStartPosToSecondPos,true);
            pathState = PathState.SECOND_POS;
        case SECOND_POS:
            if(!follower.isBusy()){
                telemetry.addLine("Done first path");
                stop();
            }
            break;
        default:
            telemetry.addLine("No State Command");
    }
}

    public enum PathState {
        //START POSITION_ POSITION 1
        //NORTH > WEST
        //WEST>

        START_POS,SECOND_POS,THIRD_POS,FOURTH_POS




    }

    PathState pathState;





    @Override
    public void init() {
        pathState = PathState.START_POS;
        follower = Constants.createFollower(hardwareMap);
        buildPath();
        follower.setPose(StartPose);
        telemetry.addLine("Init method Triggerd");

    }

    @Override
    public void loop() {
follower.update();
statePathUpdate();

    }

    @Override
    public void stop() {
        super.stop();
    }
}
