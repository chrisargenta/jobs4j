package jobs4j;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestSubmitJob {

	JobManager jm = null;

	@BeforeAll
	void setup() {
		jm = JobManager.getInstance();
	}

	@Test
	void testSubmit() {
		Job job = new TestJob(10); // 10ms job run time
		try {
			JobId id = jm.submit(job);
			jm.waitFor(id);
		} catch (JobException | InterruptedException e) {
			fail("Job Exception: " + e.getLocalizedMessage());
		}
	}

	@Test
	void testObserve() {
		Job job = new TestJob(10); // 10ms job run time
		try {

			JobId id = jm.submit(job);
			jm.waitFor(id);
		} catch (JobException | InterruptedException e) {
			fail("Job Exception: " + e.getLocalizedMessage());
		}
	}

	@Test
	void testClear() {
		Job job = new TestJob(10000); // LONG 10s job run time
		try {
			JobId id = jm.submit(job);
			jm.clearJob(id);

		} catch (JobException e) {
			fail("Job Exception: " + e.getLocalizedMessage());
		}
	}

	@AfterAll
	void close() {
		if (jm == null)
			fail("No JobManager there to close");
		jm.close();
	}

}
