package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
        private String senderId;
        private int time;

        public TickBroadcast(int time) {
            this.time = time;
            senderId="TimeService";
        }

    public int getTime() {
        return time;
    }

    public String getSenderId() {
            return senderId;
        }
}
