package bgu.spl.mics.application.passiveObjects;
import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents=new HashMap<>();

	private static class SquadHolder {
		private static Squad instance = new Squad();
	}

	private Squad() {} // empty constructor
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {

		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(int i=0;i<agents.length;i++){
			//TODO change to better solution
			agents[i].release();
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for(int i=0;i<serials.size();i++){
			if (agents.containsKey(serials.get(i))){
				synchronized (agents.get(serials.get(i))) {
					agents.get(serials.get(i)).release();
					agents.get(serials.get(i)).notifyAll();
				}
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public  void sendAgents(List<String> serials, int time){
		try{
			Thread.sleep(time*100);
			releaseAgents(serials);

		}
		catch (InterruptedException e){
			e.printStackTrace();
		}

		}



	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public  boolean getAgents(List<String> serials){
		serials.sort(Comparator.comparingInt(Integer::parseInt));
		for (int i=0;i<serials.size();i++) {
			if (!agents.containsKey(serials.get(i))) {
				releaseAgents(serials);
				return false;
			}
			synchronized (agents.get(serials.get(i))) {
				while (!agents.get(serials.get(i)).isAvailable()) {
					try {
						agents.get(serials.get(i)).wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				agents.get(serials.get(i)).acquire();
			}
		}
		return true;
	}

	/**
	 * gets the agents names
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials){
		List<String> agentsNames=new LinkedList<>();
		for (int i=0;i<serials.size();i++){
			agentsNames.add(agents.get(serials.get(i)).getName());
		}
		return agentsNames;
	}

}