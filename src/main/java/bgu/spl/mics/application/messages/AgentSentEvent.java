package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.ArrayList;
import java.util.List;

public class AgentSentEvent implements Event<List<String>> {
    private String senderName;
    private List<String> agents;
    private int duration;

    public AgentSentEvent(String senderName, List<String> agents,int duration) {
        this.senderName = senderName;
        this.agents=new ArrayList<>(agents);
        this.duration=duration;
    }

    public String getSenderName() {
        return senderName;
    }

    public List<String> getAgents() {
        return agents;
    }

    public int getDuration() {
        return duration;
    }
}
