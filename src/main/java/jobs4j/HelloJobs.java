/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package jobs4j;

import java.util.Random;
import java.util.UUID;

import jobs4j.threaded.JobManager;

public class HelloJobs extends JobObserver {

	private static final int JOB_COUNT = 10;

	// A simple Job that sleeps for awhile then says hello

	public static class WasteTimeJob extends Job {
		private int runtime = 10;
		private int increments = 10;
		public String payload;
		private static int count = 0;

		public WasteTimeJob() {
			Random rand = new Random();
			runtime = rand.nextInt(10000);
			payload = "Hello from 'Job #" + (++count) + "'";
		}

		// Job.execute() implements to work being done within job

		@Override
		public void execute() {
			long start = System.currentTimeMillis();
			for (int i = 1; i <= increments; i++) {
				try {
					// sleeping is a great job if you can get it...
					Thread.sleep(runtime / increments);
				} catch (InterruptedException e) {
					notifyObservers(e);
					return;
				}
				setPercentDone((double) i / increments);
			}
			long stop = System.currentTimeMillis();
			payload += ", I wasted " + (stop - start) + "ms sleeping.";
		}
	}

	// Main for starting up a threaded JobManager and starting JOB_COUNT jobs

	public static void main(String[] args) {
		HelloJobs demo = new HelloJobs();
		JobManager queue = JobManager.getInstance();
		try {
			for (int i = 0; i < JOB_COUNT; i++) {
				Job j = new HelloJobs.WasteTimeJob();
				try {
					UUID id = queue.submit(j);
					queue.observeJob(id, demo);
				} catch (JobException e) {
					e.printStackTrace();
				}
				queue.waitForAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queue.close();
		}
	}

	// Methods for the Observation of jobs (overrides JobObserver)

	@Override
	protected void onStatus(UUID id, JobStatus status) {
		System.out.println("Observed Status Update\t" + id + "\t" + status);
	}

	@Override
	protected void onPercentDone(UUID id, double percentDone) {
		System.out.println("Observed Percent Done\t" + id + "\t" + percentDone);
	}

	@Override
	public void onCompletion(Job job) {
		System.out.println(
				"Observed Completion of\t" + job.getId() + "\tPayload: " + ((HelloJobs.WasteTimeJob) job).payload);
		JobManager.getInstance().clearJob(job); // TODO: no timeout on how long jobs sit in queue after being done
												// waiting to be cleared
	}

}
