import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class PetitDejeuner {
  public static void main(String[] args) {
    final int nbHobbits = 3 ;
    final String nom [] = {"Bilbo", "Frodo", "Peregrin"} ;
    Table table = new Table(nbHobbits) ;
    for(int i = 0; i < nbHobbits; i++) new Hobbit(nom[i], table).start();
  }
}    

class Table { // Les trois Hobbits mangent que quand tous les trois ont faim
  final int nbHobbits;
  private volatile int nbSentados = 0;

  public Table(int nbHobbits) {
    this.nbHobbits = nbHobbits;
  }

  public synchronized void sAsseoir(){
    nbSentados++;
    notifyAll();
    while(nbSentados<nbHobbits && nbSentados!=0){
      try {
      wait();
    } catch (Exception e) {
      e.printStackTrace();
    }

    }
  }

  public synchronized void seLever(){
    nbSentados--;
  }
}

class Hobbit extends Thread {
  Table table ;
  Random aléa = new Random() ;  
  public Hobbit(String nom, Table table) {
    this.setName(nom) ;
    this.table = table ;
  }  
  private void affiche(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("'['hh'h 'mm'mn 'ss','SSS's] '");  
    System.out.println(sdf.format(new Date(System.currentTimeMillis()))
                       + Thread.currentThread().getName() + " " + message);        
  }
  private void petitDejeuner(String repas) {
    affiche("commence son "+repas+" petit-déjeuner.");
    try { sleep(aléa.nextInt(5000)); } catch(InterruptedException e){ e.printStackTrace(); }
    affiche("termine son "+repas+" petit-déjeuner.");
  }
  public void run() {
    try { sleep(aléa.nextInt(3000)); } catch(InterruptedException e){ e.printStackTrace(); }
    affiche("est réveillé. Il a faim!");
    table.sAsseoir() ;
    petitDejeuner("1er") ;
    table.seLever() ;
    try { sleep(aléa.nextInt(3000)); } catch(InterruptedException e){ e.printStackTrace(); }
    affiche("a faim de nouveau!");
    table.sAsseoir() ;
    petitDejeuner("2nd") ;
    table.seLever() ;
  }	
}
