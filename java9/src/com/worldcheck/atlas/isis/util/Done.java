package com.worldcheck.atlas.isis.util;

public class Done {
	private int _activeThreads = 0;
	private boolean _started = false;

	public synchronized void waitDone() {
		while (true) {
			try {
				if (this._activeThreads > 0) {
					this.wait();
					continue;
				}
			} catch (InterruptedException var2) {
				;
			}

			return;
		}
	}

	public synchronized void waitBegin() {
		while (true) {
			try {
				if (!this._started) {
					this.wait();
					continue;
				}
			} catch (InterruptedException var2) {
				;
			}

			return;
		}
	}

	public synchronized void workerBegin() {
		++this._activeThreads;
		this._started = true;
		this.notify();
	}

	public synchronized void workerEnd() {
		--this._activeThreads;
		this.notify();
	}

	public synchronized void reset() {
		this._activeThreads = 0;
	}
}