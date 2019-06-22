package com.rpsingh.modules;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }

    public void decrement() {
        c.decrementAndGet();
    }

    public int value() {
        return c.get();
    }

    public static void main(String[] args) {
    	AtomicCounter ac = new AtomicCounter();
    	System.out.println(ac.value());
    	ac.increment();
		System.out.println(ac.value());
		ac.decrement();;
		System.out.println(ac.value());
	}
}