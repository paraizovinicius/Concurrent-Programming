import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class BufferRapide {
    private final int taille;
    private final byte[] buffer;
    private final AtomicInteger disponibles = new AtomicInteger(0);
    private final Object verrouDepot = new Object();
    private final Object verrouRetrait = new Object();
    int premier = 0;
    int prochain = 0;
    BufferRapide(int taille){
        this.taille = taille;
        this.buffer = new byte[taille];
    }
    void deposer(byte b) throws InterruptedException{
        synchronized(verrouDepot){
            while(disponibles.get() == taille) verrouDepot.wait();
            buffer[prochain] = b;
            prochain = (prochain + 1) % taille;
            disponibles.incrementAndGet();
        }
        synchronized(verrouRetrait){
            verrouRetrait.notifyAll();
        }
        afficher();
    }
    byte retirer() throws InterruptedException{
        byte element;
        synchronized(verrouRetrait){
            while(disponibles.get() == 0) verrouRetrait.wait();
            element = buffer[premier];
            premier = (premier + 1) % taille;
            disponibles.decrementAndGet();
        }
        synchronized(verrouDepot){
            verrouDepot.notifyAll();
        }
        afficher();
        return element;
    }

    synchronized public void afficher(){
        StringBuffer sb  = new StringBuffer();
        sb.append(" [ ");
        for (int i=0; i<disponibles.get(); i++) sb.append(buffer[(premier + i) % taille] + " ");
        sb.append("] ");
        System.out.println(sb.toString());
    }

    public static void main(String[] argv){
        BufferRapide monBuffer = new BufferRapide(10);
        for (int i=0; i<2; i++) new Producteur(monBuffer).start();
        for (int i=0; i<2; i++) new Consommateur(monBuffer).start();
    }

    static class Producteur extends Thread{
        BufferRapide monBuffer;
        public Producteur(BufferRapide buffer){
            this.monBuffer = buffer;
        }
        public void run(){
            byte donnee =0;
            while (true){
                donnee = (byte) ThreadLocalRandom.current().nextInt(100);
                try{ monBuffer.deposer
            (donnee);} catch(InterruptedException e) {break; }
            }
        }
    }
    static class Consommateur extends Thread{
        BufferRapide monBuffer;
        public Consommateur(BufferRapide buffer){
            this.monBuffer = buffer;
        }
        public void run(){
            while (true){
                try { monBuffer.retirer();} catch (InterruptedException e) {break; }
            }
        }
    }
    
}
