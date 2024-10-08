package wait;

public final class Waiter {

    private static final Waiter waiter = new Waiter();

    private Waiter() {}

    public static Waiter getInstance() {
        return waiter;
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds* 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
