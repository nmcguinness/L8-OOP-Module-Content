package t24_pathfinding.exercises.ex05;

import t24_pathfinding.common.AStarPathfinder;
import t24_pathfinding.common.DijkstraPathfinder;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Heuristic;
import t24_pathfinding.common.Maps;
import t24_pathfinding.common.SearchResult;

public class Exercise {

    public static void run() {
        Grid room = Maps.openRoom();
        SearchResult manhattan = new AStarPathfinder(Heuristic.manhattan()).findPath(room);
        SearchResult zero = new AStarPathfinder(Heuristic.zero()).findPath(room);
        SearchResult dijkstra = new DijkstraPathfinder().findPath(room);

        print("A* Manhattan:", manhattan);
        print("A* zero:     ", zero);
        print("Dijkstra:    ", dijkstra);
    }

    private static void print(String label, SearchResult result) {
        System.out.println(label + " cost=" + result.cost() + " expanded=" + result.expanded());
    }
}
