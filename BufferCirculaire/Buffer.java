import java.util.concurrent.ThreadLocalRandom;

public class Buffer {
    private final int taille;
    private final byte[] buffer;
    private volatile int disponibles = 0;
    private volatile int prochain = 0;
    private volatile int premier = 0;
    Buffer(int taille){
        this.taille = taille;
        this.buffer = new byte[taille];
    }
    synchronized void deposer(byte b) throws InterruptedException{
        while (disponibles == taille) wait();
        buffer[prochain] = b;
        prochain = (prochain + 1) % taille;
        disponibles ++;
        afficher();
        notifyAll();
    }
    synchronized byte retirer() throws InterruptedException{
        while (disponibles ==0) wait();
        byte element = buffer[premier];
        premier = (premier + 1) % taille;
        disponibles--;
        afficher();
        notifyAll();
        return element;
    }

    synchronized public void afficher(){
        StringBuffer sb  = new StringBuffer();
        sb.append(" [ ");
        for (int i=0; i<disponibles; i++) sb.append(buffer[(premier + i) % taille] + " ");
        sb.append("] ");
        System.out.println(sb.toString());
    }

    public static void main(String[] argv){
        Buffer monBuffer = new Buffer(10);
        for (int i=0; i<2; i++) new Producteur(monBuffer).start();
        for (int i=0; i<2; i++) new Consommateur(monBuffer).start();
    }

    static class Producteur extends Thread{
        Buffer monBuffer;
        public Producteur(Buffer buffer){
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
        Buffer monBuffer;
        public Consommateur(Buffer buffer){
            this.monBuffer = buffer;
        }
        public void run(){
            while (true){
                try { monBuffer.retirer();} catch (InterruptedException e) {break; }
            }
        }
    }
    
}
