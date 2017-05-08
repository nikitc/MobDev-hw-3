package comnikitc.github.mobdev_hw_3;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

public class IOHandlerThread extends Thread{
    @Nullable private Handler ioHandler;

    @Override
    public void run() {
        Looper.prepare();
        ioHandler = new Handler();
        Looper.loop();
    }

    public Handler getIoHandler() {
        return ioHandler;
    }
}
