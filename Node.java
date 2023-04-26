import java.util.ArrayList;
import java.util.List;

public class Node {
    Board board;
    List<Node> filho = new ArrayList<>();
    Node pai = null;
    int Q;
    int N;
    
    public Node(Board initBoard) {
        board = initBoard;
        Q = 0;
        N = 0;
    }

    int maiorFilho() {
        int maximus = 0;
        int j = 0;
        while(filho.get(0).N == 0 && j < 7){
            maximus = j++;
        }
        for(int i=j+1;i<filho.size();i++){
            if(filho.get(i).N == 0) continue;
            if(((double)(filho.get(maximus).Q)/(double)(filho.get(maximus).N)) < ((double)(filho.get(i).Q)/(double)(filho.get(i).N))){
                maximus = i;
            }
        }
        return maximus;
    }

    boolean isEmpty(){
        return pai == null;
    }
    void addFilho(Node node) {
        filho.add(node);
        node.pai = this;
    }
    boolean temFilho() {
        return !filho.isEmpty();
    }

}
