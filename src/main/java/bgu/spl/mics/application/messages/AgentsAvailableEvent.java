package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<Integer> {
    private String senderName;



    private List<String> agents;

    public AgentsAvailableEvent(String senderName, List<String> agents) {
        this.senderName = senderName;
        this.agents=agents;
    }

    public String getSenderName() {
        return senderName;
    }

    public List<String> getAgents() {
        return agents;
    }
}
