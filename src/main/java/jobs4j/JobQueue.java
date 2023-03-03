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

import java.util.UUID;

public interface JobQueue {

	// Add jobs to queue
	public UUID submit(Job job) throws JobException;

	// Clear out completed jobs from queue
	public void clearJob(UUID id);

	public void clearJob(Job job);

	// querying the queue and jobs
	public Job getJob(UUID id) throws JobException;

	public JobStatus getJobStatus(UUID id) throws JobException;

	public void observeJob(UUID id, JobObserver observer) throws JobException;

	// Blocking calls for job completion, consider using a job observer instead
	public Job waitFor(UUID id) throws JobException, InterruptedException;

	public void waitForAll() throws InterruptedException;

	// to shutdown the queue
	public void close();

}
