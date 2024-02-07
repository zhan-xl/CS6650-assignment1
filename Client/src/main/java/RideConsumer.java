import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class RideConsumer implements Runnable {

  private final SkierApi client;

  private final BlockingQueue<RideEntry> queue;
  private List<ResponseLog> responseLogList;

  final public static int NUM_OF_ENTRY = 1000;
  final private int NUM_OF_RETRY = 5;

  public RideConsumer(BlockingQueue<RideEntry> queue, List<ResponseLog> responseLogList) {
    client = new SkierApi();
    this.queue = queue;
    this.responseLogList = responseLogList;
  }

  @Override
  public void run() {
    for (int i = 0; i < NUM_OF_ENTRY; i++) {
      int statusCode = 0;
      try {
        long startTime = System.currentTimeMillis();
        RideEntry rideEntry = queue.take();
        for (int j = 0; j < NUM_OF_RETRY; j++) {
          statusCode = client.doPost(rideEntry);
          if (statusCode == 201) {
            break;
          }
        }
        long endTime = System.currentTimeMillis();
        ResponseLog responseLog = new ResponseLog(startTime, "POST", endTime - startTime,
            statusCode);
        responseLogList.add(responseLog);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
