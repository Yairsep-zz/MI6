package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

    private List<MissionInfo> missionInfoList;
    private int currentTimeTick;
    private String id;

    public Intelligence(String id) {
        super("Intelligence" + id);
        this.id = id;
        this.missionInfoList = new LinkedList<>();
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeBroadcast(TickBroadcast.class, callBackTimeTick -> {
            currentTimeTick = callBackTimeTick.getTime();
                while (!missionInfoList.isEmpty() && missionInfoList.get(0).getTimeIssued() == currentTimeTick) {
                    MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(
                            missionInfoList.remove(0), this.getName(), currentTimeTick);
                    getSimplePublisher().sendEvent(missionReceivedEvent);
                }

        });
        subscribeBroadcast(TerminateBroadcast.class, callBackTerminate->{
            terminate();
        });
    }

    public void load(List<MissionInfo> missionInfos) {
        for (int i = 0; i < missionInfos.size(); i++) {
            missionInfoList.add(missionInfos.get(i));
        }
        missionInfoList.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));
    }
}
