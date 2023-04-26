import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MCTS{
    public static boolean full(int[] max){
        for(int i=0;i<=6;i++){
            if(max[i] <6){
                
                return false;
            }
        }
        return true;
    }
    public static int mcts(Node root,int[] NodeCount){
        int N = 500;
        Node rootN = new Node(root.board);
        for(int k=1;k<=7;k++){
            Board isto = root.board;
            Board novoIsto = new Board(isto.rows,isto.columns ,isto.flag);
            for(int i=0;i<isto.rows;i++){
                
                for(int j=0;j<isto.columns;j++){
                    novoIsto.max[j] = isto.max[j];
                    //System.out.println(novoIsto.max[j]);
                    novoIsto.board[i][j] = isto.board[i][j];
                }
            }

            novoIsto = novoIsto.add(k);
            if(winner(novoIsto, k)){
                return k;
            }
        }
        for(int i=0;i<N;i++){ 
            List<Node> visited = new ArrayList<>();
            Node node = rootN;
            visited.add(node);
            // escolher nó para expandir
            while(node.temFilho() && node.N>0){
                node = UCT.UCTselect(node);
                visited.add(node);
            }
            //adicionar novo filho na tree
           
            Node newSon = expand(node);
            visited.add(newSon);
            int value = randomVal(newSon,NodeCount);
            
            for( Node nodevisited : visited){
                nodevisited.N++;
              
                nodevisited.Q += value;
                //System.out.print(i+"|"+nodevisited.Q + "|" + nodevisited.N+"|  ");
            }
            //System.out.println();
           
        }
        for(Node i : rootN.filho){
            //System.out.println("asdas "+i.Q + "|" + i.N);
        }
        rootN.maiorFilho();
        //System.out.println((rootN.maiorFilho() +1) );
        return rootN.maiorFilho() + 1;
    }

    public static Node expand(Node node) {
        Node tmp;
        Board isto = node.board;
        Board novoIsto;
        //System.out.println("heheheheha");
        for(int k=1;k<=7;k++){
            //System.out.println(node.board.max[k-1]+ " | "+k);
            
            tmp = new Node(new Board(isto.rows,isto.columns ,isto.flag ));
            novoIsto = tmp.board;
            for(int i=0;i<isto.rows;i++){
                novoIsto.max[i] = isto.max[i];
                for(int j=0;j<isto.columns;j++){
                    novoIsto.board[i][j] = isto.board[i][j];
                }
            }
                tmp.board = tmp.board.add(k);
            //tmp.board.printBoard();
            if(node.board.max[k-1] >= 6){
                //System.out.println("sus");
                tmp.N = -10001;
            }
            node.addFilho(tmp);
        }
        return node.filho.get(0);
    }

    /*public static int rollOut(Node node) {
        
    }*/

    public static int randomVal(Node node,int[] NodeCount){
        Random rand = new Random();
        int low = 1;
        int high = 8;
      
        Board isto = node.board;
        Board tmp = new Board(isto.rows,isto.columns ,isto.flag );
        for(int i=0;i<isto.rows;i++){
            tmp.max[i] = isto.max[i];
            for(int j=0;j<isto.columns;j++){
                tmp.board[i][j] = isto.board[i][j];
            }
        }
        int valor;
        valor = rand.nextInt(high-low)+low;
        if(full(tmp.max)) return 0;
        while(tmp.complete(valor)){
            valor = rand.nextInt(high-low)+low;
        }
        
        tmp = tmp.add(valor);
        //System.out.println(tmp.max[valor-1]);
        //tmp.printBoard();
        while(!winner(tmp,valor)){
            NodeCount[0]++;
            if(full(tmp.max)) return 0;
            valor = rand.nextInt(high-low)+low;
           
            while(tmp.complete(valor)){
                //System.out.println(valor);
                valor = rand.nextInt(high-low)+low;
            }
        
            tmp = tmp.add(valor);
        }
        if(tmp.flag%2==0){
            return 1;
        }else{
            return 0;
        }
    }

    public static boolean winner(Board game,int dotx){
        int[][] direction = {{1,0},{0,1},{1,1},{-1,1}};
        dotx --;
        int doty = (game.max[dotx]-1);
        int tmpx = dotx;
        int tmpy = doty;
        //System.out.println(game.board[doty][dotx]);
        char symb = game.board[doty][dotx];                       
        
        for(int i=0;i<4;i++){
            for(int ç=0;ç<4;ç++){
                tmpx = dotx - ç*direction[i][0];
                tmpy =doty - ç*direction[i][1];
                if(tmpx < 0 || tmpy < 0 || tmpy >= game.rows || tmpx >= game.columns){
                   
                    break;
                }
                if( (tmpx+(direction[i][0]*3)) >= game.columns || (tmpx+(direction[i][0]*3)) < 0 || (tmpy+(direction[i][1]*3)) < 0 || (tmpy+(direction[i][1]*3)) >= game.rows){
                    continue;
                }
                int count = 0;
                for(int j=0;j<4;j++){
                    if (game.board[tmpy+(direction[i][1]*j)][tmpx+(direction[i][0]*j)] == symb) count ++;
                }
                //System.out.println(count+" | "+ direction[i][0]+"|"+ direction[i][1] + "|||"+ tmpx+"|"+tmpy);
                if(count == 4) return true;
               
            }
            //System.out.println(dotx + "|" + doty);
        }
        return false;
    }

    public static void main(String[] args){
        Scanner scan =  new Scanner(System.in);
        Board gaming = new Board(6, 7, 0);
        Node root = new Node(gaming);
        int[] res = new int[2];
        System.out.print("Bem Vindo!\nTua vez:");
        int x = scan.nextInt();
        while(x<1 || x>7){
            System.out.print("nuh-uh, tenta outro campeão: ");
            x = scan.nextInt();
        }
        root.board = root.board.add(x);
        //System.out.println(gaming.board[gaming.max[x-1]-1][x-1]);
        int i = 0;
        while(!winner(root.board,x)){
            if (full(gaming.max)){
                break;
            }
            root.board.printBoard();
            if(i%2 == 1){
                
                System.out.print("Tua vez: ");
                x = scan.nextInt();
                while(x<1 || x>7 || gaming.max[x-1] >= gaming.rows){
                    System.out.print("nuh-uh, tenta outro campeão: ");
                    x = scan.nextInt();
 
                }
            }else{
                int[] NodeCount = {0};
                gaming = root.board;
                root = new Node(gaming);
                root.board.max = gaming.max;
                long startTime = System.currentTimeMillis();
                x = mcts(root, NodeCount);
                long endTime = System.currentTimeMillis();
                long timeElapsed = endTime - startTime;
                System.out.println("Nós expandidos: " + NodeCount[0]);
                System.out.println("Tempo: "+timeElapsed+" ms");
               //System.out.println(x);
               
            }
            root.board =  root.board.add(x); 
            System.out.println();
            //System.out.println(root.board.points());
            i++;
        }
         if(!winner(root.board, x)){
            System.out.println("Empate!! 😑");
        }

        else if(i%2 == 1){
            System.out.println("GG Venci!! 😊");
        }
        else{
            System.out.println("WHAT!!!!! HOWWWW!!!!! 😡");
        }
        root.board.printBoard();
        //System.out.println(gaming.points());
    }

}
