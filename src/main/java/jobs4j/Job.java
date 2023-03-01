package jobs4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Job {
	protected JobId id;
	protected String type;
	protected JobStatus status;
	public double percentDone = 0.0;
	private List<JobObserver> observers = new ArrayList<JobObserver>();

	public Date submitted = null;
	public Date completed = null;

	protected JobId getId() {
		return id;
	}

	protected void setJobId(JobId id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
		notifyObservers();
	}

	public double getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(double percentDone) {
		this.percentDone = percentDone;
		notifyObservers();
	}

	public void observe(JobObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		for (JobObserver observer : observers) {
			observer.update(this);
		}
	}

	public abstract void execute();
}
