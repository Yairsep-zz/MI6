package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    private String senderName;


    public GadgetAvailableEvent(String gadget, String senderName) {
        this.gadget = gadget;
        this.senderName = senderName;
    }

    public String getGadget() {
        return gadget;
    }


    public String getSenderName() {
        return senderName;
    }
}

