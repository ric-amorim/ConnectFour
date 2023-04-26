import java.util.Collections;
import java.util.Comparator;

public class UCT {
    public static double uct(int totalVisit, int nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        if(nodeVisit <0) return -1;
        return ((double) nodeWinScore / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node UCTselect(Node node) {
        int paiVisited = node.N;
            return Collections.max(node.filho,
                Comparator.comparing(c -> uct(paiVisited,
                c.Q, c.N)));
    }
}
