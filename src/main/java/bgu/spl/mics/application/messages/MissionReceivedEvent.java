package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event<Integer> {
    private MissionInfo mission;
    private String senderName;
    private int currentTimeTick;
    private int duration;
    private int timeExpired;


    public MissionReceivedEvent(MissionInfo mission,String senderName,int currentTimeTick) {
        this.mission = mission;
        this.senderName=senderName;
        this.currentTimeTick=currentTimeTick;
        this.duration=mission.getDuration();
        this.timeExpired=mission.getTimeExpired();


    }

    public String getSenderName() {
        return senderName;
    }

    public MissionInfo getMission() {
        return mission;
    }

    public int getCurrentTimeTick() {
        return currentTimeTick;
    }

    public int getDuration() {
        return duration;
    }

    public int getTimeExpired() {
        return timeExpired;
    }
}
