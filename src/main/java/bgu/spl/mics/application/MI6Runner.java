package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.application.subscribers.Terminator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {


    public static void main(String[] args) {
        String path = args[0];
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();
        inventory.printToFile(args[1]);
        Gson gson = new Gson();
        try {

            Vector<Thread> threads = new Vector<>();
            JsonReader reader = new JsonReader(new FileReader(path));
            JsonParser parser = gson.fromJson(reader, JsonParser.class);
            //Loading Inventory

            inventory.load(parser.inventory);
            Q mQ=new Q();
            Thread Q=new Thread(mQ);
            Q.setName("Q");
            Q.start();
            threads.add(Q);
            //Loading Services

            //Loading M

            Terminator terminator=new Terminator(parser.services.M);
            Thread y=new Thread(terminator);
            y.setName("terminator");
            y.start();
            threads.add(y);
            for (int i = 0; i < parser.services.M; i++) {
                M m = new M(i);
                Thread M = new Thread(m);
                M.setName("M"+i);
                M.start();
                threads.add(M);
            }
            //Loading MoneyPenny
            for (int i = 0; i < parser.services.Moneypenny; i++) {
                Moneypenny m = new Moneypenny(i);
                Thread monneyPenny = new Thread(m);
                monneyPenny.setName("MoneyPenny"+i);
                monneyPenny.start();
                threads.add(monneyPenny);
            }
            //Loading Intelligence
            for (int i = 0; i < parser.services.intelligence.length; i++) {
                List<MissionInfo> missionInfoList = new LinkedList<>();
                MissionInfo [] currentMission = parser.services.intelligence[i].missions;
                for(int j=0;j<currentMission.length;j++)
                    missionInfoList.add(currentMission[j]);
                Intelligence m = new Intelligence(Integer.toString(i));
                m.load(missionInfoList);
                Thread intelligence = new Thread(m);
                intelligence.setName("intelligence"+i);
                intelligence.start();
                threads.add(intelligence);
            }
            //Loading Squad
            squad.load(parser.squad);
            //LoadingTimeService
            TimeService timeService= new TimeService(parser.services.time);
            Thread timeService0 = new Thread(timeService);
            timeService0.start();
            threads.add(timeService0);

            for (Thread x:threads)
                x.join();

            Diary.getInstance().printToFile(args[2]);
            inventory.printToFile(args[1]);


        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

