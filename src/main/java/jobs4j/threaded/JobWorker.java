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

import jobs4j.Job;
import jobs4j.JobStatus;
import jobs4j.threaded.JobManager.KillJob;

public class JobWorker implements Runnable {

	private JobManager manager = null;
	private String name = null;

	protected JobWorker(String name, JobManager manager) {
		this.manager = manager;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		if (manager == null) {
			throw new RuntimeException("No Manager assigned to Worker (" + name + ")");
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
					job.notifyObserversOfCompletion();
				} catch (Exception e) {
					job.notifyObservers(e);
					job.setStatus(JobStatus.ERROR);
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
