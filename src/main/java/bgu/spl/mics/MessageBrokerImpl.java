package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    private Map<Subscriber, BlockingQueue<Message>> subscriberMessageMap;
    private Map<Class<? extends Event<?>>, ConcurrentLinkedQueue<Subscriber>> eventSubscriberMap;
    private Map<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> broadCastSubscriberMap;
    private Map<Event, Future> eventFutureConcurrentHashMap;
    Diary diary=Diary.getInstance();



    private MessageBrokerImpl() {
        subscriberMessageMap = new ConcurrentHashMap<>();
        eventSubscriberMap = new ConcurrentHashMap<>();
        broadCastSubscriberMap = new ConcurrentHashMap<>();
        eventFutureConcurrentHashMap = new ConcurrentHashMap<>();

    }

    private static class MessageBrokerImplHolder {
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {

        return MessageBrokerImplHolder.instance;
    }

    @Override
    public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        if (!eventSubscriberMap.containsKey(type)) {
            ConcurrentLinkedQueue<Subscriber> subscribers = new ConcurrentLinkedQueue<>();
            subscribers.add(m);
            eventSubscriberMap.put(type, subscribers);
        } else
            eventSubscriberMap.get(type).add(m);

    }

    @Override
    public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        synchronized (broadCastSubscriberMap) {
            if (!broadCastSubscriberMap.containsKey(type)) {
                ConcurrentLinkedQueue<Subscriber> subscribers = new ConcurrentLinkedQueue<>();
                subscribers.add(m);
                broadCastSubscriberMap.put(type, subscribers);
            } else
                broadCastSubscriberMap.get(type).add(m);
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        if (eventFutureConcurrentHashMap.get(e) != null)
            eventFutureConcurrentHashMap.get(e).resolve(result);
    }

    @Override
    public  void sendBroadcast(Broadcast b) {
        int counter=0;
        if (broadCastSubscriberMap.get(b.getClass()) != null) {
            for (Subscriber x : broadCastSubscriberMap.get(b.getClass())) {
                counter=0;
                    if (b instanceof TerminateBroadcast) {
                    for (Message m : subscriberMessageMap.get(x)) {
                        if (eventFutureConcurrentHashMap.get(m) != null) {
                            if(m instanceof MissionReceivedEvent)
                                counter++;
                            eventFutureConcurrentHashMap.get(m).resolve(null);
                            subscriberMessageMap.get(x).remove(m);
                        }
                    }
                }
                    if(counter!=0)
                       subscriberMessageMap.get(x).add(new IncrementDiaryEvent(counter));
                subscriberMessageMap.get(x).add(b);
            }
        }
    }

    @Override
    public synchronized <T> Future<T> sendEvent(Event<T> e) {
        if (eventSubscriberMap.get(e.getClass()) == null) {
            return null;
        } else {
            Subscriber toReceive = eventSubscriberMap.get(e.getClass()).poll();
            if(toReceive!=null) {
                Future eventFuture = new Future();
                eventFutureConcurrentHashMap.put(e, eventFuture);
                subscriberMessageMap.get(toReceive).add(e);
                eventSubscriberMap.get(e.getClass()).add(toReceive);
                return eventFuture;
            }
            else
                return null;
        }
    }


    @Override
    public void register(Subscriber m) {
        BlockingQueue<Message> messagesForSubscriber= new LinkedBlockingQueue<>();
        subscriberMessageMap.put(m, messagesForSubscriber);
    }

    @Override
    public void unregister(Subscriber m) {
        if (subscriberMessageMap.get(m) != null) {
            subscriberMessageMap.remove(m);
        }
        if(eventFutureConcurrentHashMap.get(m)!=null)
            eventFutureConcurrentHashMap.get(m).resolve(null);

        for (Map.Entry<Class<? extends Event<?>>, ConcurrentLinkedQueue<Subscriber>> entry : eventSubscriberMap.entrySet())
            entry.getValue().remove(m);

        for (Map.Entry<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> entry : broadCastSubscriberMap.entrySet())
            entry.getValue().remove(m);
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        if (!subscriberMessageMap.containsKey(m))
            throw new InterruptedException("subscriber is nor register");
        //if(subscriberMessageMap.get(m).conta)
        return subscriberMessageMap.get(m).take();
    }
}
