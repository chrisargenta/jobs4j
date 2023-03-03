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

package jobs4j.threaded;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jobs4j.Job;
import jobs4j.JobException;
import jobs4j.JobObserver;
import jobs4j.JobQueue;
import jobs4j.JobStatus;
import jobs4j.utils.Config;

public class JobManager implements JobQueue {

	private Config config = null;
	private ExecutorService threadPool = null;
	private boolean active = false;

	private static final int default_thread_count = 1;
	private int threadCount = default_thread_count;

	/*
	 * Singleton Job Manager is a singleton class
	 */
	private static JobManager instance = null;

	public static JobManager getInstance() {
		if (instance == null) {
			instance = new JobManager();
		}
		return instance;
	}

	protected JobManager() {
		config = Config.getInstance();
		threadCount = default_thread_count;
		try {
			threadCount = Integer.parseInt(config.get(Config.PROPERTY_LOCAL_THREAD_COUNT));
		} catch (Exception e) {
			threadCount = default_thread_count;
		}
		threadPool = Executors.newFixedThreadPool(threadCount);
		active = true;
		for (int i = 0; i < threadCount; i++) {
			JobWorker agent = new JobWorker("JobWorker" + i, this);
			threadPool.submit(agent);
		}
	}

	// Keeps a local copy of Jobs that can be observed
	private Hashtable<UUID, Job> jobs = new Hashtable<UUID, Job>();
	private Queue<Job> queue = new LinkedList<Job>();

	/*
	 * Submit a Job to the JobManager returns a JobId
	 */
	public synchronized UUID submit(Job job) throws JobException {
		if (!active)
			throw new JobException("Cannot submit a new Job to closed queue.");
		jobs.put(job.getId(), job);
		queue.add(job);
		job.setStatus(JobStatus.SUBMITTED);
		job.setPercentDone(0.0);
		return job.getId();
	}

	/*
	 * Clears old Jobs When Jobs are completed and the WebApp no longer needs to
	 * track them they should be cleared. Otherwise, these will stick around until
	 * the system is restarted. *
	 */
	public synchronized void clearJob(UUID uuid) {
		if (jobs.containsKey(uuid)) {
			jobs.remove(uuid);
		}
	}

	public synchronized void clearJob(Job job) {
		if (jobs.containsKey(job.getId())) {
			jobs.remove(job.getId());
		}
	}

	public synchronized Job getJob(UUID id) throws JobException {
		if (jobs.containsKey(id)) {
			return jobs.get(id);
		}
		throw new JobException("No such Job");
	}

	public synchronized JobStatus getJobStatus(UUID id) throws JobException {
		if (jobs.containsKey(id)) {
			return jobs.get(id).getStatus();
		}
		throw new JobException("No such Job");
	}

	public synchronized void observeJob(UUID id, JobObserver observer) throws JobException {
		if (jobs.containsKey(id)) {
			jobs.get(id).observe(observer);
		} else {
			throw new JobException("No such Job");
		}
	}

	protected synchronized void updateJob(UUID uuid, JobStatus status) {
		if (jobs.containsKey(uuid)) {
			jobs.get(uuid).setStatus(status);
		}
	}

	protected synchronized void updateJob(UUID id, double percentDone) {
		if (jobs.containsKey(id)) {
			jobs.get(id).setPercentDone(percentDone);
		}
	}

	protected synchronized boolean jobAvailible() {
		return !queue.isEmpty();
	}

	protected synchronized Job getNextJob() {
		Job job = null;
		if (jobAvailible()) {// potential race condition with multiple threads
			job = queue.poll();
		} else {
			job = null;
		}
		return job;
	}

	/*
	 * Don't use this unless you have to... but it seemed useful to have a blocking
	 * technique
	 */
	public Job waitFor(UUID id) throws JobException, InterruptedException {
		while (true) {
			if (!jobs.containsKey(id))
				throw new JobException("No such JobId");
			Job j = jobs.get(id);
			if (j.getStatus().equals(JobStatus.COMPLETED) || j.getStatus().equals(JobStatus.ERROR)) {
				clearJob(id);
				return j;
			}
			Thread.sleep(1000);
		}
	}

	public void waitForAll() throws InterruptedException {
		while (jobAvailible()) {
			Thread.sleep(1000);
		}
	}

	protected class KillJob extends Job {
		@Override
		public void execute() {
		}
	}

	public void close() {
		active = false;
		for (int i = 0; i < threadCount; i++) {
			Job k = new KillJob();
			try {
				submit(k);
			} catch (JobException e) {
				e.printStackTrace();
			}
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isActive() {
		return active;
	}

}
