enum Côté { EST, OUEST }

public class Corde {
    volatile int maximumBabouins = 5;
    volatile int leste, oeste = 0;
    

    synchronized void saisir(Côté origine) throws InterruptedException{
        if(origine == Côté.EST){ leste = 1; }
        else{ oeste = 1; }
    
        while (maximumBabouins==0 || (origine == Côté.EST && oeste == 1) || (origine == Côté.OUEST && leste == 1)){
            wait();
        }
        maximumBabouins--;

    }

    synchronized void lâcher(Côté origine){
        notifyAll();
        maximumBabouins++;
        if(maximumBabouins == 5 && origine == Côté.EST){
            leste = 0;
        }
        if(maximumBabouins == 5 && origine == Côté.OUEST){
            oeste = 0;
        }
    }
}
