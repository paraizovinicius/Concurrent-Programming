import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Quicksort extends Thread {
    static final int taille = 1000;
    static final int[] tableau = new int[taille];
    static final int borne = 10 * taille;
    public volatile int debut = 0;
    public static volatile int NbTaches = 0;

    private static void echangerElements(int[] t, int m, int n) {
        int temp = t[m];
        t[m] = t[n];
        t[n] = temp;
    }

    private static int partitionner(int[] t, int debut, int fin) {
        int v = t[fin]; // choix(arbitraire) du pivot
        int place = debut; // place du pivot, à droite des elements deplaces
        for (int i = debut; i < fin; i++) { // parcours du reste du tableau
            if (t[i] < v) { // cette valeur t[i] doit etre à droite du pivot
                echangerElements(t, i, place); // on le met à sa place
                place++; // on met à jour la place du pivot
            }
        }
        echangerElements(t, place, fin);
        return place;
    }

    private static void trierRapidement(int[] t, int debut, int fin) {
        if (debut < fin) {
            int p = partitionner(t, debut, fin);
            trierRapidement(t, debut, p - 1);
            trierRapidement(t, p + 1, fin);
        }
    }

    // if ((fin - debut + 1) <= taille / 100)
    private static void afficher(int[] t, int debut, int fin) {
        for (int i = debut; i <= debut + 5; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println("...");
        for (int i = fin - 5; i <= fin; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        Random alea = new Random();
        for (int i = 0; i < taille; i++) { // Remplissage aleatoire du tableau
            tableau[i] = alea.nextInt(2 * borne) - borne;
        }
        // System.out.println("Tableau initial : ");
        // afficher(tableau, 0, taille - 1); // Affiche le tableau à trier

        long debutDuTri = System.nanoTime();

        threadPool.submit(new Task(tableau, 0, taille - 1));
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long finDuTri = System.nanoTime();
        long dureeDuTri = (finDuTri - debutDuTri) / 1000000;

        System.out.println("Tableau trié : ");
        afficher(tableau, 0, taille - 1); // Affiche le tableau obtenu
        System.out.println("Obtenu en " + dureeDuTri + " millisecondes.");

        System.out.println(NbTaches);

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
            if ((fin - debut + 1) <= taille / 100) {
                trierRapidement(tableau, debut, pivot - 1);
                trierRapidement(tableau, pivot + 1, fin);
            } else {
                NbTaches++;
                NbTaches++;
                Task taskLeft = new Task(tableau, debut, pivot - 1);
                taskLeft.run();
                Task taskRight = new Task(tableau, pivot + 1, fin);
                taskRight.run();
            }

        }
    }
}
