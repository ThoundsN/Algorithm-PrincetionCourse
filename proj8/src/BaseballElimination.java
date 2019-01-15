

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;

public class BaseballElimination {
    private int n;
    private int best,winner;
    private ArrayList<String> teams;
    private int[] wins;
    private int[] loses;
    private int[] remains;
    private int[][] g;
    private int ngames;
    private ArrayList<String>[] coe;


    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        best = winner = 0;
        teams = new ArrayList<>();
        wins = new int[n];
        loses = new int[n];
        remains = new int[n];
        g = new int[n][n];
        coe = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            teams.add(i,in.readString());
            wins[i] = in.readInt();
            if (wins[i]>best){
                winner = i;
                best = wins[i];
            }
            loses[i] = in.readInt();
            remains[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
        calcateallFlow();
    }                    // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return n;
    }                        // number of teams

    public Iterable<String> teams() {
        return teams;
    }                                // all teams

    public int wins(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw   new IllegalArgumentException();
        return wins[teams.indexOf(team)];
    }                      // number of wins for given team

    public int losses(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw   new IllegalArgumentException();
        return loses[teams.indexOf(team)];
    }                   // number of losses for given team

    public int remaining(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw   new IllegalArgumentException();
        return remains[teams.indexOf(team)];

    }                 // number of remaining games for given team

    public int against(String team1, String team2) {
        int a = teams.indexOf(team1);
        int b = teams.indexOf(team2);

        if (a < 0||b<0) throw   new IllegalArgumentException();
        return g[a][b];
    }    // number of remaining games between team1 and team2

    private FlowNetwork createnetwork(int teamindex) {
        ngames = (n - 1) * (n - 2) / 2;
        int V = ngames + n + 1;
        int round = 1;
        int ideal =  wins[teamindex] + remains[teamindex];

        FlowNetwork network = new FlowNetwork(V);
        for (int x = 0; x < n - 2; x++) {
            for (int y = x + 1; y < n-1; y++) {
                if (x == teamindex || y == teamindex|| g[x][y] == 0) continue;
                network.addEdge(new FlowEdge(0, round, g[x][y]));
                network.addEdge(new FlowEdge(round, ngames + x+1, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(round++, ngames + y+1, Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == teamindex) continue;
            network.addEdge(new FlowEdge(ngames + i+1, V-1, ideal - wins[i]));
        }
        return network;

    }

    private void  calconeFlow(FlowNetwork G, int teamindex){
        FordFulkerson ff = new FordFulkerson(G,0,G.V()-1);
        coe[teamindex] = new ArrayList<>(n-1);
        for (int i =0; i< n;i++){
            if (ff.inCut(1+ngames+i)) coe[teamindex].add(teams.get(i));
        }
    }

    private void calcateallFlow(){
        for (int i = 0; i<n; i++){
            if (best>(wins[i]+remains[i])){
                coe[i] = new ArrayList<>();
                coe[i].add(teams.get(winner));
                continue;
            }
            FlowNetwork fn = createnetwork(i);
            calconeFlow(fn,i);
        }
    }

    public          boolean isEliminated(String team){
        int index = teams.indexOf(team);
        if (index < 0) throw   new IllegalArgumentException();
        return coe[index] != null;

    }              // is given team eliminated?


    public Iterable<String> certificateOfElimination(String team){
        int index = teams.indexOf(team);
        if (index < 0) throw   new IllegalArgumentException();
        return coe[index];
    }  // subset R of teams that eliminates given team; null if not eliminated


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        FlowNetwork fn = division.createnetwork(4);
        StdOut.println(fn.toString());
    }
}