package decryption.dm;

import java.util.concurrent.ThreadFactory;

public class CustomThreadFactory implements ThreadFactory {
    private int count = 0;
    @Override
    public Thread newThread(Runnable r) {
        ++count;
        Thread thread = newThread(r);
        thread.setName("Thread/Agent number " + count);
        return thread;
    }
}
