import java.util.Random;
import java.util.concurrent.*;

public class Quicksort {

    private static int threshold;
    static final int taille = 50;
    static final int[] tableau = new int[taille];
    static final int borne = 10 * taille;


    private static void afficher(int[] t, int debut, int fin) {
        for (int i = debut; i <= debut + 3 && i < t.length; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println("...");
        for (int i = fin - 3; i <= fin && i < t.length; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println();
    }

    private static void swap(int[] tableau, int i, int j) {
        int temp = tableau[i];
        tableau[i] = tableau[j];
        tableau[j] = temp;
    }

    private static int partitionner(int[] t, int debut, int fin) {
        int v = t[fin];
        int place = debut;
        for (int i = debut; i < fin; i++) {
            if (t[i] < v) {
                swap(t, i, place);
                place++;
            }
        }
        swap(t, place, fin);
        return place;
    }

    private static void trierRapidement(int[] t, int debut, int fin) {
        if (debut < fin) {
            int p = partitionner(t, debut, fin);
            trierRapidement(t, debut, p - 1);
            trierRapidement(t, p + 1, fin);
        }
    }

    public static void main(String[] args) {

        Random alea = new Random();
        for (int i = 0; i < taille; i++) {
            tableau[i] = alea.nextInt(2 * borne) - borne;
        }
        int p = 4;

        threshold = (p > 1) ? (1 + taille / (p << 3)) : taille;
        long debutDuTri = System.nanoTime();
        parallelQuicksort(tableau, 0, taille - 1);
        long finDuTri = System.nanoTime();
        long dureeDuTri = (finDuTri - debutDuTri) / 1000000;
        afficher(tableau, 0, taille - 1); // Affiche le tableau obtenu
        System.out.println("Parallel obtenu en " + dureeDuTri + " millisecondes.");
    }

    private static void parallelQuicksort(int[] tableau, int low, int high) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new PQuicksort(tableau, low, high));
        pool.shutdown();
    }

    private static class PQuicksort extends RecursiveAction {
        private int[] tableau;
        private int low, high;

        public PQuicksort(int[] tableau, int low, int high) {
            this.tableau = tableau;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if ((high - low) < threshold) {
                trierRapidement(tableau, low, high);
            } else {
                int i = low, j = high;
                int pivot = tableau[low + (high - low) / 2];

                while (i <= j) {
                    while (tableau[i] < pivot) {
                        i++;
                    }

                    while (tableau[j] > pivot) {
                        j--;
                    }

                    if (i <= j) {
                        swap(tableau, i, j);
                        i++;
                        j--;
                    }
                }

                if (low < j) {
                    ForkJoinTask.invokeAll(new PQuicksort(tableau, low, j));
                }

                if (i < high) {
                    ForkJoinTask.invokeAll(new PQuicksort(tableau, i, high));
                }
            }
        }
    }

}
