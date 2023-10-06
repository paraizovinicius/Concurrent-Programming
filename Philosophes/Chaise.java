public class Chaise {

    boolean sit = false;
    volatile int ChaisesDisponibles = 4;

    synchronized void Sentar() throws InterruptedException {
        while (ChaisesDisponibles == 0) {
            wait();
        }
        ChaisesDisponibles--;
    }

    synchronized void Levantar() {
        notifyAll();
        ChaisesDisponibles++;
        
    }

}
