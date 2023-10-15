enum Côté { EST, OUEST }

public class Babouin extends Thread{
    static Corde corde = new Corde();
    Côté origine;
    Babouin(Côté origine, int i){
        this.origine = origine;
        if (origine == Côté.EST) setName("E" + i);
        else setName("O" + i);
    }
    public void run(){
        System.out.println("\nLe babouin " + getName() + " arrive sur le côté " + origine);
        try {
            corde.saisir(origine);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("\n Le babouin " + getName() + " commence à traverser.");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n Le babouin" + getName() + " a terminé de traverser");
        corde.lâcher(origine);
        System.out.println("\n Le babouin " + getName() + " a laché la corde et s'en va.");
    }

    public static void main(String[] args){
        for (int i = 1; i<10; i++){
            try { Thread.sleep(2000);} catch(InterruptedException e){ e.printStackTrace();}
            if (Math.random() >= 0.5) new Babouin(Côté.EST, i).start();
            else new Babouin(Côté.OUEST, i).start();
        }
    }
}
