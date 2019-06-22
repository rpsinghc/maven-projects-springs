package com.worldcheck.atlas.isis.util;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.Collection;

public class ThreadPool {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.util.ThreadPool");
	protected Thread[] threads = null;
	Collection assignments = new ArrayList(3);
	protected Done done = new Done();

	public ThreadPool(int size) {
		this.logger.debug("Initializing thread pool::::::::::::::::::::::::::::::::::::::::" + size);
		this.threads = new WorkerThread[size];

		for (int i = 0; i < this.threads.length; ++i) {
			this.threads[i] = new WorkerThread(this);
			this.threads[i].start();
		}

	}

	public synchronized void assign(Runnable r) {
		this.done.workerBegin();
		this.assignments.add(r);
		this.notify();
	}

	public synchronized Runnable getAssignment() {
		try {
			while (!this.assignments.iterator().hasNext()) {
				this.wait();
			}

			Runnable r = (Runnable) this.assignments.iterator().next();
			this.assignments.remove(r);
			return r;
		} catch (InterruptedException var2) {
			this.done.workerEnd();
			return null;
		}
	}

	public void complete() {
		this.done.waitBegin();
		this.done.waitDone();
	}

	protected void finalize() {
		this.done.reset();

		for (int i = 0; i < this.threads.length; ++i) {
			this.threads[i].interrupt();
			this.done.workerBegin();
			this.threads[i].destroy();
		}

		this.done.waitDone();
	}
}