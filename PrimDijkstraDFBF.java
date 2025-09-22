// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public int size() {
        return N;
    }
    
    public int get(int i) {
        return a[i];
    }
    


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp(int k) 
    {
        int v = a[k]; // Node ID at index k in heap

        // Bubble up while not at root and v has higher priority (lower dist)
        while (k > 1 && dist[v] < dist[a[k / 2]]) {
                a[k] = a[k / 2];           // Move parent down
                hPos[a[k]] = k;            // Update hPos of the moved node
                k = k / 2;                 // Move up the tree
            }

            a[k] = v;                      // Place v in its correct position
            hPos[v] = k;                   // Update its position in hPos[]
    }


    public void siftDown(int k) 
    {
        int v = a[k]; // Node ID at index k in heap
        int j;
    
        while (2 * k <= N) { // While there is at least a left child
            j = 2 * k; // Left child index
    
            // If right child exists and is smaller than left child
            if (j < N && dist[a[j + 1]] < dist[a[j]]) {
                j++; // Use right child
            }
    
            // If v has lower priority value than the smaller child, we're done
            if (dist[v] <= dist[a[j]]) {
                break;
            }
    
            a[k] = a[j];          // Move child up
            hPos[a[k]] = k;       // Update position of the moved child
            k = j;                // Move down the tree
        }
    
        a[k] = v;                // Place original node in the correct spot
        hPos[v] = k;             // Update its position
    }
    



    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    
    // default constructor	           
    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);
            
        String[] parts = reader.readLine().split(" +");
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for (v = 1; v <= V; ++v)
            adj[v] = z;               
        
        // read the edges
        System.out.println("Reading edges from text file");
        for (e = 1; e <= E; ++e)
        {
            parts = reader.readLine().split(" +");
            //parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

            // insert (v, wgt) at the head of list for u
            t = new Node();
            t.vert = v;
            t.wgt = wgt;
            t.next = adj[u];
            adj[u] = t;

            // insert (u, wgt) at the head of list for v (since undirected)
            t = new Node();
            t.vert = u;
            t.wgt = wgt;
            t.next = adj[v];
            adj[v] = t;
        }	       
        reader.close();
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
    
        System.out.println("\n\nAdjacency List:");
        for (v = 1; v <= V; ++v) {
            System.out.print("\nadj[" + toChar(v) + "] -> ");
            for (n = adj[v]; n != z; n = n.next) {
                System.out.print("|" + toChar(n.vert) + " | " + n.wgt + "|");
                if (n.next != z) {
                    System.out.print(" -> ");
                }
            }
        }
        System.out.println();
    }
    

    public void DF(int s) {
        visited = new int[V + 1];
        id = 0;
        System.out.println("\nDepth-First Traversal starting from " + toChar(s) + ":");
        dfVisit(s);
        System.out.println();
    }

    private void dfVisit(int v) {
        visited[v] = ++id;
        System.out.print(toChar(v) + " ");
        for (Node t = adj[v]; t != z; t = t.next) {
            if (visited[t.vert] == 0)
                dfVisit(t.vert);
        }
    }
    
    public void breadthFirst(int s) {
        visited = new int[V + 1];
        int[] queue = new int[V + 1];
        int front = 0, rear = 0, v, u;

        visited[s] = ++id;
        queue[rear++] = s;

        System.out.println("\nBreadth-First Traversal starting from " + toChar(s) + ":");

        while (front != rear) {
            v = queue[front++];
            System.out.print(toChar(v) + " ");
            for (Node t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                if (visited[u] == 0) {
                    visited[u] = ++id;
                    queue[rear++] = u;
                }
            }
        }
        System.out.println();
    }


    public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        Node t;

        // Create dist, parent, and hPos arrays
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        int[] hPos = new int[V + 1]; // hPos[v] = position of v in heap

        // Initialize
        for (v = 1; v <= V; ++v) {
            dist[v] = Integer.MAX_VALUE; // initially no edge known
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        int step = 0;
        System.out.println("\nStep-by-step Prim's Algorithm from vertex " + toChar(s) + ":");

        while (!h.isEmpty()) {  
            System.out.println("\n--- Step " + step + " ---");
            //printStep(step, h, dist, parent);
            printStepDetails(step, h, dist, parent, "Prim");


            v = h.remove(); // vertex with min dist not in MST
            System.out.println("  Selected from heap: " + toChar(v) + " (dist=" + dist[v] + ")");
            wgt_sum += dist[v];

            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                wgt = t.wgt;

                if (hPos[u] == 0 && dist[u] == Integer.MAX_VALUE) {
                    // Not in heap yet, and this is the first time seeing it
                    dist[u] = wgt;
                    parent[u] = v;
                    h.insert(u);
                } else if (hPos[u] != 0 && wgt < dist[u]) {
                    // Already in heap and found a better edge
                    dist[u] = wgt;
                    parent[u] = v;
                    h.siftUp(hPos[u]); // update heap since dist[u] decreased
                }
            }
            step++;
        }

        // Save MST parent array to use later (e.g., for showMST)
        mst = parent;

        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
    }

    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning Tree (using Prim's algorithm) parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println();
    }

    private void printStepDetails(int step, Heap h, int[] dist, int[] parent, String algoName) {
        System.out.println("\n" + algoName + " - Step " + step + ":");
    
        System.out.print("  Heap: ");
        for (int i = 1; i <= h.size(); ++i) {
            int v = h.get(i);
            System.out.print(toChar(v) + "(" + (dist[v] == Integer.MAX_VALUE ? "∞" : dist[v]) + ") ");
        }
        System.out.println();
    
        System.out.print("  dist[]: ");
        for (int i = 1; i <= V; i++) {
            System.out.print(toChar(i) + "=" + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]) + " ");
        }
        System.out.println();
    
        System.out.print("  parent[]: ");
        for (int i = 1; i <= V; i++) {
            if (parent[i] == 0)
                System.out.print(toChar(i) + "=null ");
            else
                System.out.print(toChar(i) + "=" + toChar(parent[i]) + " ");
        }
        System.out.println();
    }
    
    


    public void SPT_Dijkstra(int s)
    {
        int v, u, wgt;
        Node t;

        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        int[] hPos = new int[V + 1];

        // Initialize distances and parent array
        for (v = 1; v <= V; ++v) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        int step = 0;

        while (!h.isEmpty()) {
            //printDijkstraStep(++step, h, dist, parent); 
            printStepDetails(++step, h, dist, parent, "Dijkstra");

            v = h.remove();
            System.out.println("  Selected from heap: " + toChar(v) + " (dist=" + dist[v] + ")");

            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                wgt = t.wgt;

                if (dist[v] + wgt < dist[u]) {
                    dist[u] = dist[v] + wgt;
                    parent[u] = v;

                    if (hPos[u] == 0) {
                        h.insert(u);      // Not in heap yet
                    } 
                    else {
                        h.siftUp(hPos[u]); // Already in heap, update position
                    }
                }
            }
        }

        // Save SPT to use for displaying
        mst = parent;
        System.out.print("\n\nShortest Path Tree (Dijkstra) from source " + toChar(s) + ":\n");

        for (v = 1; v <= V; ++v) {
            System.out.printf("%s -> %s \tDist: %s\t\tPath: ", toChar(s), toChar(v),
                (dist[v] == Integer.MAX_VALUE ? "∞" : dist[v]));

            printPath(s, v, parent);
            System.out.println();
        }
    }

    private void printPath(int s, int v, int[] parent) {
        if (v == s) {
            System.out.print(toChar(s));
        } else if (parent[v] == 0) {
            System.out.print("No path");
        } else {
            printPath(s, parent[v], parent);
            System.out.print(" -> " + toChar(v));
        }
    }
}

public class PrimDijkstraDFBF {
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter the name of the graph text file: ");
        String fname = reader.readLine();

        System.out.print("Enter the starting vertex (number): ");
        int s = Integer.parseInt(reader.readLine());

        Graph g = new Graph(fname);
        g.display();

        g.DF(s);
        g.breadthFirst(s);
        g.MST_Prim(s);
        g.showMST();
        g.SPT_Dijkstra(s);              
    }
}





