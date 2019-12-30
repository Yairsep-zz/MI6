package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Terminator extends Subscriber {
    private AtomicInteger mToKill;

    public Terminator(int mTokill) {
        super("Terminator");
        this.mToKill= new AtomicInteger(mTokill);
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateMBroadCast.class, callBack->{
            int newVal, oldVal;
            do{
                oldVal = mToKill.intValue();
                newVal = mToKill.intValue()-1;

            }while (!mToKill.compareAndSet(oldVal,newVal));
            if (mToKill.intValue()==0){
                getSimplePublisher().sendBroadcast(new TerminateMonneyPennyAndQBroadCast());
                terminate();
            }


        });
    }
    }