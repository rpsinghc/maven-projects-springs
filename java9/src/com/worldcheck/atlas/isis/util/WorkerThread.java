package com.worldcheck.atlas.isis.util;

class WorkerThread extends Thread {
	public boolean busy;
	public ThreadPool owner;

	WorkerThread(ThreadPool o) {
		this.owner = o;
	}

	public void run() {
		Runnable target = null;

		do {
			target = this.owner.getAssignment();
			if (target != null) {
				target.run();
				this.owner.done.workerEnd();
			}
		} while (target != null);

	}
}