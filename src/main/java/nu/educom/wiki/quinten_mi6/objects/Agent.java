package nu.educom.wiki.quinten_mi6.objects;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Agent {
	
	public Short agentNumber;
	public String agentString;
	public int agentId;
	public char[] code;
	public boolean active;
	public boolean license;
	public LocalDateTime expireDate;
	public long coolOff;
	public LocalDateTime latestCoolOff;
		
	/**
	 * Constructs an agent object.
	 * 
	 * @param int id the agent id.
	 * @param long number the agent number.
	 * @param String code the code of the agent.
	 * @param boolean active whether the user is active or not.
	 * @param boolean license whether the user has a license or not.
	 * @param Timestamp expireDate the date and time the license expires.
	 * @param long coolOff the cool off time in minutes.
	 * @param Timestamp latestCoolOff the date and time of the latest cool off activation.
	 */
	public Agent(int id, short number, String code, 
			boolean active, boolean license, 
			Timestamp expireDate, long coolOff, Timestamp latestCoolOff) {
		this.agentNumber = number;
		this.agentId = id;
		this.code = code.toCharArray();
		this.active = active;
		this.license = license;
		if (expireDate != null) {
			this.expireDate = expireDate.toLocalDateTime();
		}
		this.coolOff = coolOff;
		if (latestCoolOff != null) {
			this.latestCoolOff = latestCoolOff.toLocalDateTime();
		}
	}
}
