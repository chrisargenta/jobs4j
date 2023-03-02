package jobs4j;

import jobs4j.JobManager.KillJob;

public class JobAgent implements Runnable {

	private JobManager manager = null;
	private String name = null;

	protected JobAgent(String name, JobManager manager) {
		this.manager = manager;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		if (manager == null) {
			System.err.println("No JobManager assigned to JobAgent (" + name + ")");
			return;
		}
		Job job = null;

		while (manager.isActive()) {
			job = manager.getNextJob();
			if (job != null) {
				try {
					if (job instanceof KillJob)
						return;
					job.setStatus(JobStatus.ACTIVE);
					job.execute();
					job.setStatus(JobStatus.COMPLETED);
				} catch (Exception e) {
					manager.updateJob(job.getId(), JobStatus.ERROR);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
