import java.util.concurrent.CountDownLatch;

public class Table extends Thread{
    private final int PHILOSOPHER_COUNT= 5;
    private Fork[] forks;
    private Philosopher[] philosophers;
    private CountDownLatch cdl;

    public Table() {
        this.forks = new Fork[PHILOSOPHER_COUNT];
        this.philosophers = new Philosopher[PHILOSOPHER_COUNT];
        this.cdl = new CountDownLatch(PHILOSOPHER_COUNT);
        init();
    }

    @Override
    public void run() {
        System.out.println("Start");
        try {
            thinkingProcess();
            cdl.await();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        System.out.println("Все философы поели");
    }
    public synchronized boolean tryGetFork(int leftFork, int rightFork) {
        if (!forks[leftFork].isUsing() && !forks[rightFork].isUsing()){
            forks[leftFork].setUsing(true);
            forks[rightFork].setUsing(true);
            return true;
        }
        return false;
    }
    public void putForks(int leftFork, int rightFork) {
        forks[leftFork].setUsing(false);
        forks[rightFork].setUsing(false);
    }
    private void init() {
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks[i] = new Fork();
        }
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            philosophers[i] = new Philosopher("Philosoph № " + i, this, i, (i + 1) % PHILOSOPHER_COUNT, cdl);
        }
    }
    private void thinkingProcess() {
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
    }
}
