package jobs4j;

import java.util.Random;

public class TestJob extends Job {

	private int runtime = 10;

	public TestJob() {
		super();
		type = "NULL";
	}

	public TestJob(int runtime) {
		super();
		type = "NULL";
		this.runtime = runtime;
	}

	@Override
	public void execute() {
		System.out.println("\t TestJob Start\t" + id);
		Random rand = new Random();
		if (runtime != 10)
			runtime = rand.nextInt(1000);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < runtime; j++) {
				double x = rand.nextDouble();
				double y = rand.nextDouble();
				double z = rand.nextDouble();
				double fma = Math.fma(x, y, z);
			}
			try {
				Thread.sleep(runtime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			setPercentDone(i / 10.0);
		}
		System.out.println("\t TestJob End\t" + id);
	}

}
