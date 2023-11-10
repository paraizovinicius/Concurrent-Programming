import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Quicksort extends Thread{
    static final int taille = 1000000;
    static final int [] tableau = new int[taille];
    static final int borne = 10*taille;

    private static void echangerElements(int [] t, int m, int n){
        int temp = t[m];
        t[m] = t[n];
        t[n] = temp;
    }

    private static int partitionner(int [] t, int debut, int fin) {
        int v = t[fin];                         // choix(arbitraire) du pivot
        int place = debut;                      // place du pivot, à droite des elements deplaces
        for (int i = debut; i<fin; i++){        // parcours du reste du tableau
            if(t[i] < v){                       // cette valeur t[i] doit etre à droite du pivot
                echangerElements(t, i, place);  // on le met à sa place
                place++;                        // on met à jour la place du pivot
            }
        }
        echangerElements(t, place, fin); 
        return place;
    }


    private static void trierRapidement(int[] t, int debut, int fin){

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        int p = partitionner(t, debut, fin);
        if(debut < fin){
            if((fin - debut + 1) <= taille/100){ // si la taille du tableau visé est moins grand ou égal à 1/100 de la taille, faire sequentiellement
                trierRapidement(t, debut, p-1);
                trierRapidement(t, p+1, fin);
            }
            else{ // si la taille du tableau visé est plus grand que 1/100 de la taille, créer une tâche et partitionner
                threadPool.submit(new Task(tableau, debut, p - 1));
                threadPool.submit(new Task(tableau, p + 1, fin));
            }
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void afficher(int[] t, int debut, int fin){
        for(int i = debut; i<= debut+3; i++){
            System.out.println("_" + t[i]);
        }
        System.out.println("...");
        for(int i = fin-3; i<=fin ; i++){
            System.out.println("_" + t[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Random alea = new Random();
        for(int i=0; i<taille; i++){ // Remplissage aleatoire du tableau
            tableau[i] = alea.nextInt(2*borne) - borne;
        }
        System.out.println("Tableau initial : ");
        afficher(tableau, 0, taille-1); // Affiche le tableau à trier

        long debutDuTri = System.nanoTime();
        trierRapidement(tableau, 0, taille-1); //Tri du tableau
        long finDuTri = System.nanoTime();
        long dureeDuTri = (finDuTri - debutDuTri) / 1000000;



        
        System.out.println("Tableau trié : ");
        afficher(tableau, 0, taille-1); // Affiche le tableau obtenu
        System.out.println("Obtenu en " + dureeDuTri + " millisecondes.");

    }

    private static class Task implements Runnable {

        private int[] tableau;
        private int debut;
        private int fin;

        public Task(int[] tableau, int debut, int fin) {
            this.tableau = tableau;
            this.debut = debut;
            this.fin = fin;
        }

        @Override
        public void run() {
            int pivot = partitionner(tableau, debut, fin);
            Task taskLeft = new Task(tableau, debut, pivot - 1);
            Task taskRight = new Task(tableau, pivot + 1, fin);
            taskLeft.run();
            taskRight.run();
        }
    }
}




