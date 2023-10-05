public class Fourchette {

    boolean disponibilidade;
    String id;

    public Fourchette(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
        }

    public synchronized void Tomar () throws InterruptedException{
        while(disponibilidade == false){
            wait();
        }
        this.disponibilidade = false;
    }

    public synchronized void Largar(){
        this.disponibilidade = true;
        notifyAll();

    }
    
}
