import java.util.Random;

public class Philosophes extends Thread {

    final Fourchette fd;
    final Fourchette fg;
    Chaise chaise = new Chaise();

    public Philosophes(Fourchette fd, Fourchette fg) {
        this.fd = fd;
        this.fg = fg;

    }

    public void run() {

        penser();
        try {
            manger();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void penser() {
 
        Random alea = new Random();
        int segundos = alea.nextInt(5); // Numero aleatório de 0 a 20
        System.out.println(Thread.currentThread().getName() + " va penser! ");
        try {
            Thread.sleep(segundos * 1000);
            System.out.println(Thread.currentThread().getName() + " venait de penser et a faim ");

        } catch (InterruptedException e) {
            // TODO: handle exception
        }
    }

    public void manger() throws InterruptedException {

        try {
            chaise.Sentar();
            fd.Prendre();
            fg.Prendre();
            System.out.println(Thread.currentThread().getName() + " mange");
        } catch (Exception e) {
            // TODO: handle exception
        }
        fd.Lacher();
        fg.Lacher();
        chaise.Levantar();
        System.out.println(Thread.currentThread().getName() + " a finit de manger");

    }

}
