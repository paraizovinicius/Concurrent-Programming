package Prova2022;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PingPong {
  public static void main(String[] args) {
    Balle balle = new Balle(Côté.DROITE) ;
    new Pongiste(balle, Côté.DROITE, "D1").start();  // Le pongiste D1 est à droite
    new Pongiste(balle, Côté.DROITE, "D2").start();  // Le pongiste D2 est à droite
    new Pongiste(balle, Côté.GAUCHE, "G1").start();  // Le pongiste G1 est à gauche
    new Pongiste(balle, Côté.GAUCHE, "G2").start();  // Le pongiste G2 est à gauche
  }
}

enum Côté {DROITE, GAUCHE}            // La balle passe des deux côtés de la table


// On va travailler dans cette classe le problème de syncronization avec AtomicReference
class Balle {

   AtomicReference <Côté> cote = new AtomicReference<>();


  public Balle(Côté côté) {
    this.cote.set(côté);
  }    

  public void renvoyer(Côté position) {
    while(cote.get() != position){
      Thread.onSpinWait();
    }

    if (position == Côté.DROITE) {
      cote.set(Côté.GAUCHE);
      System.out.println(Thread.currentThread().getName() + " fait PING");
    } else {
      cote.set(Côté.DROITE);
      System.out.println(Thread.currentThread().getName() + " fait PONG");
    
  }
}
}
    
class Pongiste extends Thread {
  final long nbRenvois = 3 ;
  final Balle balle ;
  final Côté position ;
  public Pongiste(Balle balle, Côté position, String nom) {
    this.balle = balle ;
    this.position = position ;
    this.setName(nom) ;
  }    
  public void run() {
    for (int i = 0 ; i < nbRenvois ; i++) balle.renvoyer(position) ;
  }
}

