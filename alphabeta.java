import java.util.Scanner;
import java.util.Arrays;  
import java.util.concurrent.TimeUnit;

public class alphabeta {
    public static boolean full(int[] max){
        for(int i=0;i<=6;i++){
            if(max[i] <6){
                
                return false;
            }
        }
        return true;
    }

    public static int[] recursive(Board game, int depth,int play,int flag,int flag2,int alpha,int beta,int[] nodeCount){

        nodeCount[0]++;
        
        if (depth == 0)
            return new int[] {play,game.points()};
        int max = 10000*flag;
        int place = 0;
        int[] temp = {0,max};
        //game.printBoard();
       
        if(full(game.max)){
            //System.out.println("heheheha");
            return new int[] {play,max};
        } 
        for(int i=1;i<=7;i++){
            if(flag2 == 0){
                play = i;
            }
            if(game.max[i-1]<6)
                temp = recursive(game.add(i),depth-1,play,flag*(-1),1,alpha,beta,nodeCount);
            else
                continue;
            if(flag == 1 && max>temp[1]){ //min
                max = temp[1];
                place = temp[0];
                beta = Math.min(beta,max);
                if(alpha >= beta)
                    break;
            }else if(flag == -1 && max<temp[1]){ //max
                max = temp[1];
                place = temp[0];
                alpha = Math.max(alpha,max);
                if(alpha >= beta)
                    break;
            }  
        }
        //System.out.println(place + "|" + max);
        return new int[] {place,max};
        



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
        int[] res = new int[2];
        System.out.print("Bem Vindo!\nTua vez:");
        int x = scan.nextInt();
        gaming = gaming.add(x);
        //gaming.printBoard();
        //System.out.println(gaming.board[gaming.max[x-1]-1][x-1]);
        int i = 0;
        while(!winner(gaming,x)){
            if (full(gaming.max)){
                //System.out.println("heheheheha");
                break;
            }

            gaming.printBoard();
            if(i%2 == 1){
                System.out.print("Tua vez: ");
                x = scan.nextInt();
                while(x<0 || x>7 || gaming.max[x-1] >= gaming.rows){
                    System.out.print("nuh-uh, tenta outro campeão: ");
                    x = scan.nextInt();
                }
            }else{
                int[] nodeCount = {0};
                long startTime = System.currentTimeMillis();
                x = recursive(gaming, 6, 0, -1, 0,-2147483648,2147483647,nodeCount)[0];
                long endTime = System.currentTimeMillis();
                long timeElapsed = endTime - startTime;
                System.out.println("Nós expandidos: "+nodeCount[0]);
                System.out.println("Tempo :"+ timeElapsed + " ms");

            }
            gaming = gaming.add(x);
            System.out.println();
            //System.out.println(gaming.points());
            i++;
        }
        if(!winner(gaming, x)){
            System.out.println("Empate!! 😑");
        }
        else if(i%2 == 1){
            System.out.println("GG Venci!! 😊");
        }
        else{
            System.out.println("WHAT!!!!! HOWWWW!!!!! 😡");
        }
        gaming.printBoard();
        //System.out.println(gaming.points());
    }
}

