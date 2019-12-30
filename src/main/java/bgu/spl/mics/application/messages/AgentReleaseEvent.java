package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentReleaseEvent implements Event<Boolean > {
    private String senderName;
    private List<String> agents;

    public AgentReleaseEvent(String senderName, List<String> agents) {
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
