package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	/**
	 * Retrieves the single instance of this class.
	 */
	private List<Report> reportList=new LinkedList<>();
	private AtomicInteger total=new AtomicInteger(0);

	private static class DiaryHolder {
		private static  Diary instance = new Diary();
	}

	public static Diary getInstance() {
		return DiaryHolder.instance;
	}

	public List<Report> getReports() {
		return reportList;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		reportList.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) {

		GsonBuilder builder=new GsonBuilder();
		builder=builder.setPrettyPrinting();
		Map<String,Object> diaryToPrint=new HashMap<>();
		Gson gson=builder.create();
		diaryToPrint.put("Reports",reportList);
		diaryToPrint.put("Total",total);


		try{
			FileWriter file=new FileWriter(filename);
			file.write(gson.toJson(diaryToPrint));
			file.flush();
		} catch (Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){

		return total.get();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		int oldVal, newVal;
		do {
			oldVal = total.intValue();
			newVal = total.intValue() + 1;
		}
		while (!total.compareAndSet(oldVal, newVal));

	}
}
