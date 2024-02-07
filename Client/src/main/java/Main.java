import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

  private final static int NUM_OF_RIDES = 200000;
  private final static String LOG_PATH = "/Users/xiaolinzhan/Documents/CS6650/Assignment1/client/log/responseLog.csv";

  private final static int NUM_OF_INITIAL_THREADS = 32;
  private final static int NUM_OF_THREADS = 168;

  private static List<ResponseLog> responseLogList;

  public static void main(String[] args) throws IOException, InterruptedException {
    responseLogList = new CopyOnWriteArrayList<ResponseLog>();
    BlockingQueue<RideEntry> queue = new ArrayBlockingQueue<>(64);

    ExecutorService producerService = new ThreadPoolExecutor(2, 4, 120, TimeUnit.SECONDS,
        new LinkedBlockingDeque<Runnable>());
    for (int i = 0; i < NUM_OF_RIDES; i++) {
      producerService.execute(new RideProducer(queue));
    }

    long start = System.currentTimeMillis();
    List<Thread> threadsList = new ArrayList<>();
    for (int i = 0; i < NUM_OF_INITIAL_THREADS; i++) {
      Thread newThread = new Thread(new RideConsumer(queue, responseLogList));
      threadsList.add(newThread);
      newThread.start();
    }

    int remainingThreads = NUM_OF_RIDES / RideConsumer.NUM_OF_ENTRY - NUM_OF_INITIAL_THREADS;

    boolean isDone = false;
    while (remainingThreads > 0) {
      for (int i = 0; i < threadsList.size(); i++) {
        Thread thread = threadsList.get(i);
        if (thread == null) {
          continue;
        }
        if (!thread.isAlive() && !isDone) {
          isDone = true;
          System.out.println("One of the initial threads is completed.");
          for (int j = NUM_OF_INITIAL_THREADS; j < NUM_OF_THREADS; j++) {
            Thread newThread = new Thread(new RideConsumer(queue, responseLogList));
            threadsList.add(newThread);
            newThread.start();
            remainingThreads--;
          }
          Thread newThread = new Thread(new RideConsumer(queue, responseLogList));
          threadsList.set(i, newThread);
          newThread.start();
          remainingThreads--;
        } else if (!thread.isAlive()) {
          Thread newThread = new Thread(new RideConsumer(queue, responseLogList));
          threadsList.set(i, newThread);
          newThread.start();
          remainingThreads--;
          System.out.println("Number of remaining thread: " + remainingThreads);
        }
      }
    }

    for (Thread thread : threadsList) {
      thread.join();
    }

    long end = System.currentTimeMillis();

    producerService.shutdown();
    producerService.awaitTermination(120, TimeUnit.SECONDS);

    writeFile(LOG_PATH);
    System.out.println("Number of successes POST request: " + getNumOfSuccess());
    System.out.println("Number of failure POST request: " + getNumOfFailure());
    double totalTime = (end - start) / 1000.0;
    System.out.println("Total run time: " + totalTime + " second");
    System.out.printf("Total throughput: %.2f requests/second", NUM_OF_RIDES / totalTime);
    System.out.println();

    System.out.println("Mean response time: " + getMeanResTime() + " ms");
    System.out.println("Median response time: " + getMedianResTime() + " ms");
    System.out.println("Minimum response time: " + getMinResTime() + " ms");
    System.out.println("Maximum response time: " + getMaxResTime() + " ms");
    System.out.println("99th percentile response time: " + getPercentile(99.0) + " ms");

  }

  private static int getPercentile(double percentile) {
    List<Integer> responseTime = sortResponseTime();
    int index = (int) Math.ceil(percentile / 100.0 * responseTime.size());
    return responseTime.get(index - 1);
  }

  private static int getMeanResTime() {
    int sum = 0;
    for (ResponseLog responseLog : responseLogList) {
      sum += (int) responseLog.getLatency();
    }
    return sum / responseLogList.size();
  }

  private static List<Integer> sortResponseTime() {
    List<Integer> responseTime = new ArrayList<>(responseLogList.size());
    for (ResponseLog responseLog : responseLogList) {
      responseTime.add((int) responseLog.getLatency());
    }
    Collections.sort(responseTime);
    return responseTime;
  }

  private static double getMedianResTime() {
    List<Integer> responseTime = sortResponseTime();
    int middleIndex = responseTime.size() / 2;
    if (responseTime.size() % 2 == 0) {
      return (responseTime.get(middleIndex - 1) + responseTime.get(middleIndex)) / 2.0;
    } else {
      return responseTime.get(middleIndex);
    }
  }

  private static int getMinResTime() {
    return sortResponseTime().get(0);
  }

  private static int getMaxResTime() {
    return sortResponseTime().get(sortResponseTime().size() - 1);
  }

  private static int getNumOfSuccess() {
    int numOfSuccess = 0;
    for (ResponseLog responseLog : responseLogList) {
      if (responseLog.getStatusCode() == 201) {
        numOfSuccess += 1;
      }
    }
    return numOfSuccess;
  }

  private static int getNumOfFailure() {
    int numOfFailure = 0;
    for (ResponseLog responseLog : responseLogList) {
      if (responseLog.getStatusCode() != 201) {
        numOfFailure += 1;
      }
    }
    return numOfFailure;
  }

  private static void writeFile(String LOG_PATH)
      throws IOException {
    FileWriter writer = new FileWriter(LOG_PATH);
    String firstLine = "start time, request type, latency, status code,";
    writer.write(firstLine);
    writer.write("\n");
    for (ResponseLog responseLog : responseLogList) {
      String line = responseLog.getStartTime() + ", " + responseLog.getRequestType() + ", "
          + responseLog.getLatency() + ", " + responseLog.getStatusCode();
      writer.write(line);
      writer.write("\n");
    }
    writer.close();
  }
}

