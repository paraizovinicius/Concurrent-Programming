import java.util.Random;

public class Philosophes extends Thread {

    final Fourchette fd;
    final Fourchette fg;

    public Philosophes(Fourchette fd, Fourchette fg){
        this.fd = fd;
        this.fg = fg;
    }

    public void run() {
        pensar();
        try {
            comer();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pensar(){
        Random alea = new Random();
        int segundos = alea.nextInt(20); // Numero aleatório de 0 a 20
        System.out.println(Thread.currentThread().getName() + " vai pensar! ");
        try {
            Thread.sleep(segundos * 1000);
            System.out.println(Thread.currentThread().getName() + " acabou de pensar e está faminto ");

        } catch (InterruptedException e) {
            // TODO: handle exception
        }
    }

    public void comer() throws InterruptedException{
        try {
        fd.Tomar();
        fg.Tomar();
        System.out.println(Thread.currentThread().getName() + " está comendo");
        wait(2000);    
        } catch (Exception e) {
            // TODO: handle exception
        }
        fd.Largar();
        fg.Largar();
        System.out.println(Thread.currentThread().getName() + " terminou de comer");
    }


}
