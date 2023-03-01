package jobs4j;

public class JobFactory {
	private JobManager manager = JobManager.getInstance();

	public void doNothing() throws JobException, InterruptedException {
		Job in = new TestJob();
		JobId id = manager.submit(in);
		manager.waitFor(id);
		return;
	}

}
