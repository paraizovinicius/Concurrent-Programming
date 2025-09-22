enum Side { EST, OUEST };


class Corde {
    volatile int maximumBabouins = 5;
    volatile int leste, oeste = 0;
    

    synchronized void saisir(Side origine) throws InterruptedException{
        if(origine == Side.EST){ leste = 1; }
        else{ oeste = 1; }
    
        while (maximumBabouins==0 || (origine == Side.EST && oeste == 1) || (origine == Side.OUEST && leste == 1)){
            wait();
        }
        maximumBabouins--;

    }

    synchronized void lâcher(Side origine){
        notifyAll();
        maximumBabouins++;
        if(maximumBabouins == 5 && origine == Side.EST){
            leste = 0;
        }
        if(maximumBabouins == 5 && origine == Side.OUEST){
            oeste = 0;
        }
    }
}


public class Babouin extends Thread{
    static Corde corde = new Corde();
    Side origine;
    Babouin(Side origine, int i){
        this.origine = origine;
        if (origine == Side.EST) setName("E" + i);
        else setName("O" + i);
    }
    public void run(){
        System.out.println("\nThe babouin " + getName() + " arrives on the side " + origine);
        try {
            corde.saisir(origine);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("\n The babouin " + getName() + " starts to traverse.");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n The babouin " + getName() + " finished traversing");
        corde.lâcher(origine);
        System.out.println("\n The babouin " + getName() + " released the rope and goes away.");
    }

    public static void main(String[] args){
        for (int i = 1; i<10; i++){
            try { Thread.sleep(2000);} catch(InterruptedException e){ e.printStackTrace();}
            if (Math.random() >= 0.5) new Babouin(Side.EST, i).start();
            else new Babouin(Side.OUEST, i).start();
        }
    }
}
