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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class Job {
	private UUID id = UUID.randomUUID();
	private JobStatus status = JobStatus.NEW;
	private double percentDone = 0.0;
	private List<JobObserver> observers = new ArrayList<JobObserver>();

	public Date submitted = null;
	public Date completed = null;

	public UUID getId() {
		return id;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
		for (JobObserver observer : observers) {
			observer.onStatus(id, status);
		}
	}

	public double getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(double percentDone) {
		this.percentDone = percentDone;
		for (JobObserver observer : observers) {
			observer.onPercentDone(id, percentDone);
		}
	}

	public void observe(JobObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers(String message) {
		for (JobObserver observer : observers) {
			observer.onMessage(id, message);
		}
	}

	public void notifyObservers(Exception e) {
		for (JobObserver observer : observers) {
			observer.onException(id, e);
		}
	}

	public void notifyObserversOfCompletion() {
		for (JobObserver observer : observers) {
			observer.onCompletion(this);
		}
	}

	// Implement Job work performed as execute() method
	public abstract void execute();

}
