package org.example;

import org.example.Server.ScoreBoard;
import org.example.raft.PeerInfo;
import org.example.raft.RaftNode;
import org.example.raft.RaftServer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String server0 = "pi.cs.oswego.edu";
    private static final String server1 = "gee.cs.oswego.edu";
    private static final String server2 = "wolf.cs.oswego.edu";
    private static final int port = 26930;
    private static final List<PeerInfo> allNodes = Arrays.asList(
            new PeerInfo(0, server0, port),
            new PeerInfo(1, server1, port),
            new PeerInfo(2, server2, port)
    );

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("Usage: java raft.Main <nodeId>   (nodeId must be 0, 1, or 2)");
            System.exit(1);
        }

        int nodeId = Integer.parseInt(args[0]);
        if (nodeId < 0 || nodeId >= allNodes.size()) {
            System.err.println("nodeId must be 0, 1, or 2");
            System.exit(1);
        }

        PeerInfo self = allNodes.get(nodeId);
        List<PeerInfo> peers = allNodes.stream().filter(p -> p.getNodeId() != nodeId).collect(Collectors.toList());

        RaftNode node = new RaftNode(nodeId, peers);
        RaftServer server = new RaftServer(node, self.getPort());
        server.start();
        node.start();
        System.out.println("Node " + nodeId + " running on " + self.getHost() + ":" + self.getPort());
        System.out.println("Peers: " + peers);

        Thread.currentThread().join();
    }

//    public static void main(String[] args) {
//        ScoreBoard board = new ScoreBoard();
//        board.registerPlayers(List.of("Alice", "Bob", "Carlos"));
//
//        board.recordCorrectAnswer("Alice", 3000);   // answered in 3 seconds
//        board.recordCorrectAnswer("Bob", 8000);     // answered in 8 seconds
//        board.recordWrongAnswer("Carlos");
//
//        board.printStandings();
//        System.out.println("Winner: " + board.getWinner().getNickname());
//    }
}