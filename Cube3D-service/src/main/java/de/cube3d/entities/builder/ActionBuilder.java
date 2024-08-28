package de.cube3d.entities.builder;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.Builder;

import de.cube3d.entities.Action;

public class ActionBuilder implements Builder<Action> {

	private Action action;
	
    public ActionBuilder() { 	
    	action = new Action();
    }
    
	public ActionBuilder name(String name) {
		this.action.setName(name);
		return this;
	}

	public ActionBuilder startDate(Timestamp startDate) {
		this.action.setStartDate(startDate);
		return this;
	}
	
	public ActionBuilder endDate(Timestamp endDate) {
		this.action.setEndDate(endDate);
		return this;
	}
    
	public ActionBuilder finishedPlanning(boolean finishedPlanning) {
		this.action.setFinishedPlanning(finishedPlanning);
		return this;
	}
	
	public ActionBuilder finished(boolean finished) {
		this.action.setFinished(finished);
		return this;
	}
	
	@Override
	public Action build() {
        return action;
    }
}
