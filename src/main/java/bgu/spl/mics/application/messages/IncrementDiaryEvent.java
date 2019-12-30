package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class IncrementDiaryEvent implements Event<Boolean> {
    private int counter;

    public  IncrementDiaryEvent(int counter){
        this.counter=counter;
    }

    public int getCounter() {
        return counter;
    }
}
