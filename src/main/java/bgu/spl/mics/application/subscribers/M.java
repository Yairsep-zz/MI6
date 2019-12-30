package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int currentTime;
	private Diary diary;
	private int mId;
	private Boolean missionDone=false;


	public M(int mId) {
		super("M" + Integer.toString(mId));
		diary=Diary.getInstance();
		this.mId=mId;
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		subscribeEvent(IncrementDiaryEvent.class, callBackIncrementDiary ->{
			for(int i = 0; i< callBackIncrementDiary.getCounter(); i++)
				diary.incrementTotal();
			complete(callBackIncrementDiary,true);
		});
		subscribeBroadcast(TickBroadcast.class, callBackTimeTick -> {
					currentTime = callBackTimeTick.getTime();
				});
			subscribeEvent(MissionReceivedEvent.class, callBackMissionReceived ->{

				missionDone=false;
			Future<Integer> agentAvailable=getSimplePublisher().sendEvent(new AgentsAvailableEvent
					(callBackMissionReceived.getSenderName(), callBackMissionReceived.getMission().getSerialAgentsNumbers()));
			Future<List<String>> sendAgents;
			Future<Boolean> releaseAgent;
			Future<Integer> gadgetAvailable;
			if(agentAvailable.get()!=null) {
				gadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(
						callBackMissionReceived.getMission().getGadget(), callBackMissionReceived.getSenderName()));
				if (gadgetAvailable.get() != null) {
					if ( currentTime < callBackMissionReceived.getMission().getTimeExpired()) {
						sendAgents = getSimplePublisher().sendEvent(new AgentSentEvent(
								callBackMissionReceived.getSenderName(),
								callBackMissionReceived.getMission().getSerialAgentsNumbers(),
								callBackMissionReceived.getDuration()));
						missionDone=true;
						complete(callBackMissionReceived, 0);
						Report report = new Report();
						report.setAgentsSerialNumbers(callBackMissionReceived.getMission().getSerialAgentsNumbers());
						report.setAgentsNames(sendAgents.get());
						report.setGadgetName(callBackMissionReceived.getMission().getGadget());
						report.setMissionName(callBackMissionReceived.getMission().getName());
						report.setQTime(gadgetAvailable.get());
						report.setTimeCreated(currentTime);
						report.setTimeIssued(callBackMissionReceived.getMission().getTimeIssued());
						report.setM(mId);
						report.setMoneypenny(agentAvailable.get());
						diary.addReport(report);
					}
				}
			}
			if (!missionDone) {
						releaseAgent=getSimplePublisher().sendEvent(new AgentReleaseEvent(
								callBackMissionReceived.getSenderName(), callBackMissionReceived.getMission().getSerialAgentsNumbers()));

					}




			diary.incrementTotal();



		});
			subscribeBroadcast(TerminateBroadcast.class, callBackTerminate->{
				terminate();
				getSimplePublisher().sendBroadcast(new TerminateMBroadCast());

			});



	}

}
