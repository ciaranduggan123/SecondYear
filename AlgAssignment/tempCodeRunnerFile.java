import java.io.*;
import java.util.*;

class Dijkstra {

    static class Node {
        int vert;
        int wgt;
        Node next;
    }

    private int V, E;
    private Node[] adj;
    private Node z;

    public Dijkstra(String graphFile) throws IOException {
        int u, v, wgt;
        Node t;

        BufferedReader reader = new BufferedReader(new FileReader(graphFile));
        String line = reader.readLine();
        String[] parts = line.trim().split("\\s+");
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        z = new Node();
        z.next = z;

        adj = new Node[V + 1];
        for (v = 1; v <= V; ++v) {
            adj[v] = z;
        }

        for (int e = 1; e <= E; ++e) {
            line = reader.readLine();
            parts = line.trim().split("\\s+");
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            wgt = parts.length > 2 ? Integer.parseInt(parts[2]) : 1; // If weights missing, default to 1

            t = new Node();
            t.vert = v;
            t.wgt = wgt;
            t.next = adj[u];
            adj[u] = t;

            t = new Node();
            t.vert = u;
            t.wgt = wgt;
            t.next = adj[v];
            adj[v] = t;
        }

        reader.close();
    }

    private class Heap {
        private int[] a;
        private int[] hPos;
        private int[] dist;
        private int N;

        public Heap(int size, int[] dist, int[] hPos) {
            a = new int[size + 1];
            this.dist = dist;
            this.hPos = hPos;
            N = 0;
        }

        boolean isEmpty() {
            return N == 0;
        }

        void insert(int x) {
            a[++N] = x;
            siftUp(N);
        }

        int remove() {
            int v = a[1];
            hPos[v] = 0;
            a[1] = a[N--];
            siftDown(1);
            return v;
        }

        void siftUp(int k) {
            int v = a[k];
            while (k > 1 && dist[v] < dist[a[k / 2]]) {
                a[k] = a[k / 2];
                hPos[a[k]] = k;
                k = k / 2;
            }
            a[k] = v;
            hPos[v] = k;
        }

        void siftDown(int k) {
            int v = a[k];
            int j;
            while (2 * k <= N) {
                j = 2 * k;
                if (j < N && dist[a[j + 1]] < dist[a[j]]) j++;
                if (dist[v] <= dist[a[j]]) break;
                a[k] = a[j];
                hPos[a[k]] = k;
                k = j;
            }
            a[k] = v;
            hPos[v] = k;
        }
    }

    public void runDijkstra(int s) {
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        int[] hPos = new int[V + 1];

        for (int v = 1; v <= V; ++v) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        while (!h.isEmpty()) {
            int v = h.remove();
            for (Node t = adj[v]; t != z; t = t.next) {
                int u = t.vert;
                int wgt = t.wgt;
                if (dist[v] + wgt < dist[u]) {
                    dist[u] = dist[v] + wgt;
                    parent[u] = v;
                    if (hPos[u] == 0) h.insert(u);
                    else h.siftUp(hPos[u]);
                }
            }
        }

        System.out.println("\nShortest Path Tree (Dijkstra) from source " + s + ":");
        for (int v = 1; v <= V; ++v) {
            if (dist[v] == Integer.MAX_VALUE)
                System.out.printf("%d -> %d \tDist: ∞\n", s, v);
            else
                System.out.printf("%d -> %d \tDist: %d\n", s, v, dist[v]);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the graph filename (e.g., roadNet.txt): ");
        String fname = scan.nextLine();

        System.out.print("Enter the starting vertex number: ");
        int s = scan.nextInt();

        Dijkstra g = new Dijkstra(fname);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Suggest garbage collection
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        g.runDijkstra(s);

        long endTime = System.nanoTime();
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        long memUsedMB = (memAfter - memBefore) / (1024 * 1024);
        long timeMs = (endTime - startTime) / 1_000_000;

        System.out.println("\nExecution Time: " + timeMs + " ms");
        System.out.println("Memory Used: " + memUsedMB + " MB");
    }
}
