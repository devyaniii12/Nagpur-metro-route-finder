import java.util.*;

class Pair implements Comparable<Pair> {
    String station;
    int distance;

    Pair(String station, int distance) {
        this.station = station;
        this.distance = distance;
    }

    @Override
    public int compareTo(Pair other) {
        // Min-heap: smaller distance = higher priority
        return other.distance - this.distance;
    }

    @Override
    public String toString() {
        return "(" + station + ", " + distance + ")";
    }
}

public class Graph_Algo {

    private static class Edge {
        String v;
        int wt;

        Edge(String v, int wt) {
            this.v = v;
            this.wt = wt;
        }
    }

    private HashMap<String, ArrayList<Edge>> map;

    public Graph_Algo() {
        map = new HashMap<>();
        addStationsAndEdges();
    }

    // Utility to clean station names
    private String cleanName(String name) {
        return name.replace("~A", "").replace("~O", "").replace("~OA", "").trim();
    }

    // Add edge in both directions
    private void addEdge(String u, String v, int wt) {
        map.putIfAbsent(u, new ArrayList<>());
        map.putIfAbsent(v, new ArrayList<>());
        map.get(u).add(new Edge(v, wt));
        map.get(v).add(new Edge(u, wt));
    }

    // Build Nagpur Metro Graph (stations + distances approx.)
    private void addStationsAndEdges() {
        addEdge("Sitabuldi~OA", "Zero Mile~O", 1);
        addEdge("Sitabuldi~OA", "Congress Nagar~O", 2);
        addEdge("Sitabuldi~OA", "LIC Square~O", 2);
        addEdge("Zero Mile~O", "Kasturchand Park~A", 1);
        addEdge("Congress Nagar~O", "Rahate Colony~O", 2);
        addEdge("LIC Square~O", "Jhansi Rani Square~A", 2);
        addEdge("Jhansi Rani Square~A", "Shankar Nagar~A", 3);
        addEdge("Congress Nagar~O", "Airport~O", 6);
        addEdge("Sitabuldi~OA", "Subhash Nagar~A", 3);
        addEdge("Subhash Nagar~A", "Lokmanya Nagar~A", 5);
        addEdge("Sitabuldi~OA", "Kadbi Square~O", 4);
        addEdge("Kadbi Square~O", "Kamptee Road~O", 6);
        addEdge("Kamptee Road~O", "Automotive Square~O", 2);
        addEdge("Automotive Square~O", "Indora Square~O", 3);
        addEdge("Indora Square~O", "Gaddi Godam~A", 3);
    }

    // Display all stations
    public void displayStations() {
        System.out.println("Nagpur Metro Stations:");
        for (String station : map.keySet()) {
            System.out.println(station);
        }
    }

    // Shortest Path using Dijkstra + custom Heap
    public void shortestPath(String src, String dest) {
        String source = null, destination = null;
        for (String station : map.keySet()) {
            if (cleanName(station).equalsIgnoreCase(cleanName(src))) {
                source = station;
            }
            if (cleanName(station).equalsIgnoreCase(cleanName(dest))) {
                destination = station;
            }
        }

        if (source == null || destination == null) {
            System.out.println("Invalid station name. Please try again.");
            return;
        }

        HashMap<String, Integer> dist = new HashMap<>();
        HashMap<String, String> parent = new HashMap<>();

        for (String station : map.keySet()) {
            dist.put(station, Integer.MAX_VALUE);
        }

        Heap<Pair> heap = new Heap<>();
        dist.put(source, 0);
        heap.add(new Pair(source, 0));

        while (!heap.isEmpty()) {
            Pair curr = heap.remove();
            String u = curr.station;

            for (Edge e : map.get(u)) {
                int newDist = dist.get(u) + e.wt;
                if (newDist < dist.get(e.v)) {
                    dist.put(e.v, newDist);
                    parent.put(e.v, u);
                    heap.add(new Pair(e.v, newDist));
                }
            }
        }

        if (dist.get(destination) == Integer.MAX_VALUE) {
            System.out.println("No path found between " + src + " and " + dest);
            return;
        }

        // Reconstruct path
        LinkedList<String> path = new LinkedList<>();
        String temp = destination;
        while (temp != null) {
            path.addFirst(temp);
            temp = parent.get(temp);
        }

        System.out.println("\n Shortest Path from " + cleanName(src) + " to " + cleanName(dest) + ":");
        for (String st : path) {
            System.out.print(cleanName(st));
            if (!st.equals(destination)) System.out.print(" -> ");
        }
        System.out.println("\nTotal Distance: " + dist.get(destination) + " km\n");
    }

    // Menu
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph_Algo metro = new Graph_Algo();

        System.out.println("Welcome to Nagpur Metro Route Finder");

        while (true) {
            System.out.println("\n1. Display All Stations");
            System.out.println("2. Find Shortest Path");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    metro.displayStations();
                    break;
                case 2:
                    System.out.print("Enter Source Station: ");
                    String src = sc.nextLine();
                    System.out.print("Enter Destination Station: ");
                    String dest = sc.nextLine();
                    metro.shortestPath(src, dest);
                    break;
                case 3:
                    System.out.println("Thank you for using Nagpur Metro Finder!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
