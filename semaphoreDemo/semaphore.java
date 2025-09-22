package semaphoreDemo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

class MyQueue {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity;

    private final Semaphore empty; // controla espaços disponíveis
    private final Semaphore full;  // controla itens disponíveis
    private final Semaphore mutex = new Semaphore(1); // controla exclusão mútua

    public MyQueue(int capacity) {
        this.capacity = capacity;
        this.empty = new Semaphore(capacity);
        this.full = new Semaphore(0);
    }

    public void put(int item) {
        try {
            empty.acquire(); // espera espaço livre
            mutex.acquire(); // entra em seção crítica
            buffer.add(item);
            System.out.println("Producer produced item: " + item + " | Buffer size: " + buffer.size());
            mutex.release(); // sai da seção crítica
            full.release();  // sinaliza que há um item disponível
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void get() {
        try {
            full.acquire();  // espera um item disponível
            mutex.acquire(); // entra em seção crítica
            int item = buffer.poll();
            System.out.println("Consumer consumed item: " + item + " | Buffer size: " + buffer.size());
            mutex.release(); // sai da seção crítica
            empty.release(); // sinaliza que há espaço disponível
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private final MyQueue myqueue;
    private final Random random = new Random();

    public Producer(MyQueue myqueue) {
        this.myqueue = myqueue;
    }

    public void run() {
        while (true) {
            int data = random.nextInt(100);
            myqueue.put(data);
            try {
                Thread.sleep(random.nextInt(2000)); // aleatório para simular concorrência
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private final MyQueue myqueue;

    public Consumer(MyQueue myqueue) {
        this.myqueue = myqueue;
    }

    public void run() {
        while (true) {
            myqueue.get();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class semaphore {
    public static void main(String[] args) {
        int bufferSize = 5;  // agora temos buffer de 5 posições
        MyQueue myqueue = new MyQueue(bufferSize);

        // criar múltiplos produtores e consumidores
        for (int i = 0; i < 3; i++) {
            new Thread(new Producer(myqueue), "Producer-" + i).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(new Consumer(myqueue), "Consumer-" + i).start();
        }
    }
}


// Por que precisamos do mutex?
// Onde estão as regiões críticas?
// Podemos ter mais de N threads bloqueadas em wait(empty)?
// Quanto vale empty + full (valor dos contadores)? -- depende, pode ser N, N+1, N-1
// Podemos trocar a ordem das chamadas de wait dos semáforos mutex e empty/full?
