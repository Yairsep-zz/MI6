package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int moneyPennyId;
    private int time;
    private Squad s;

	public Moneypenny(int moneyPennyId) {
		super("moneyPenny"+moneyPennyId);
		this.moneyPennyId=moneyPennyId;


		// TODO Implement this
	}

	@Override
	protected void initialize() {
		s = Squad.getInstance();
		subscribeBroadcast(TickBroadcast.class, callbackTimeTick ->{
			time= callbackTimeTick.getTime();
		});
		subscribeBroadcast(TerminateMonneyPennyAndQBroadCast.class, callBackTerminate->{
			terminate();
		});
		if (moneyPennyId%2==0) {
			subscribeEvent(AgentsAvailableEvent.class, callbackAgentAvailable -> {

				boolean isAgentsAvailable = s.getAgents(callbackAgentAvailable.getAgents());
				if (isAgentsAvailable) {
					complete(callbackAgentAvailable, moneyPennyId);

				} else {
					complete(callbackAgentAvailable, null);

				}
			});
		}
		else {
		subscribeEvent(AgentSentEvent.class, callbackSendAgents ->{
			s.sendAgents(callbackSendAgents.getAgents(),callbackSendAgents.getDuration());
			complete(callbackSendAgents,s.getAgentsNames(callbackSendAgents.getAgents()));
		});
		subscribeEvent(AgentReleaseEvent.class, callbackReleaseEvent ->{
			s.releaseAgents(callbackReleaseEvent.getAgents());
			complete(callbackReleaseEvent,true);

		});
		}

		// TODO Implement this
		
	}

}
