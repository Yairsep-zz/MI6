package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateMonneyPennyAndQBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;


/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private int timeTick;

	public Q() {
		super("Q");


	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, callBackGetTimeTick -> {
			timeTick = callBackGetTimeTick.getTime();
		});
		subscribeEvent(GadgetAvailableEvent.class, callbackGadgetAvailable ->{
			Inventory i=Inventory.getInstance();
			boolean isThere =i.getItem(callbackGadgetAvailable.getGadget());
			if(isThere) {
				complete(callbackGadgetAvailable, timeTick);
			}
			else {
				complete(callbackGadgetAvailable, null);

			}
		});
		subscribeBroadcast(TerminateMonneyPennyAndQBroadCast.class, callBackTerminate->{
			terminate();
		});

	}

}
