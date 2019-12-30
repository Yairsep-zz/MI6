package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.IncrementDiaryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int duration;
	private AtomicInteger currentTime;
	private Timer timer;

	public TimeService(int time) {
		super("TimeService");
		this.duration = time;
		timer = new Timer();
		currentTime = new AtomicInteger(0);
	}

	@Override
	protected void initialize() {


	}

	@Override
	public void run() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (currentTime.intValue() <= duration) {
					getSimplePublisher().sendBroadcast(new TickBroadcast(currentTime.intValue()));
					int oldVal, newVal;
					do {
						oldVal = currentTime.intValue();
						newVal = currentTime.intValue() + 1;
					}
					while (!currentTime.compareAndSet(oldVal, newVal));
				} else {

					timer.cancel();
					getSimplePublisher().sendBroadcast(new TerminateBroadcast(currentTime.intValue()));
				}
			}
		};
		timer.schedule(task, 0, 100);
	}
}

