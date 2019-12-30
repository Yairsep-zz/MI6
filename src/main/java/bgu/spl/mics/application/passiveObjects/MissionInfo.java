package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {

	private String name;
	private String gadget;
	private int timeIssued;
	private int timeExpired;
	private int duration;
	private List<String> serialAgentsNumbers;


	//public  MissionInfo()
	/**
	 * Sets the name of the mission.
	 */
	public void setName(String name) {
		name = name;
	}

	/**
	 * Retrieves the name of the mission.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the serial agent number.
	 */
	public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
		serialAgentsNumbers = serialAgentsNumbers;
	}

	/**
	 * Retrieves the serial agent number.
	 */
	public List<String> getSerialAgentsNumbers() {
		return serialAgentsNumbers;
	}

	/**
	 * Sets the gadget name.
	 */
	public void setGadget(String gadget) {
		gadget = gadget;
	}

	/**
	 * Retrieves the gadget name.
	 */
	public String getGadget() {
		return gadget;
	}
	/**
	 * Sets the time the mission was issued in time ticks.
	 */
	public void setTimeIssued(int timeIssued) {
		timeIssued = timeIssued;
	}

	/**
	 * Retrieves the time the mission was issued in time ticks.
	 */
	public int getTimeIssued() {
		return timeIssued;
	}

	/**
	 * Sets the time that if it that time passed the mission should be aborted.
	 */
	public void setTimeExpired(int timeExpired) {
		timeExpired = timeExpired;
	}

	/**
	 * Retrieves the time that if it that time passed the mission should be aborted.
	 */
	public int getTimeExpired() {
		return timeExpired;
	}

	/**
	 * Sets the duration of the mission in time-ticks.
	 */
	public void setDuration(int duration) {
		duration = duration;
	}

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return duration;
	}
}
