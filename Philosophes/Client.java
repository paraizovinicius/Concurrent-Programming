public class Client {

    public static void main(String[] args) {

        Fourchette[] garfos = new Fourchette[5];
        // Cria 5 garfos
        for (int i = 0; i < 5; i++) {
            garfos[i] = new Fourchette(true);
        }

        Thread[] threads = new Thread[5];
        // Cria 5 threads
        for (int i = 0; i < 5; i++) {
            threads[i] = new Philosophes(garfos[i], garfos[(i+1)%5]);
            threads[i].setName(String.valueOf(i + 1)); // Set the name of the thread
            threads[i].start();
        }

    }

}
