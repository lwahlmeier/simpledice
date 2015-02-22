package com.github.lwahlmeier.simpledice;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by lwahlmeier on 2/21/15.
 */

public class SimpleExecutor extends Thread {
    LinkedBlockingDeque<Runnable> lbq = new LinkedBlockingDeque<Runnable>();

    public SimpleExecutor() {
        start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                lbq.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable run) {
        lbq.add(run);
    }
}
