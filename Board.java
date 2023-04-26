import java.util.Scanner;

public class Board {
    char[][] board;
    int rows,columns, flag;
    char[] rp1 = {'O','X'};
    int[] max;
    Board(int rowws,int cols,int first){
        board = new char[rowws][cols];
        for (int i=0;i<rowws;i++){
            for(int j=0;j<cols;j++){
                board[i][j] = ' ';
            }
        }
        rows = rowws;
        columns = cols;
        flag = first;
        max = new int[cols];
        for(int i=0;i<cols;i++){
            max[i] = 0;
        }
    }
    public Board add(int cols){
        Board tempMat = new Board(rows,columns,flag);
        for(int i=0;i<columns;i++){
            tempMat.max[i] = max[i];
        }
        for (int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                tempMat.board[i][j] = board[i][j];
            }
        }
        cols --;
        if(max[cols] >= 6) return this;
        tempMat.board[max[cols]][cols] = rp1[flag%2];
        //System.out.println(tempMat.board[max[cols]][cols] + "as");
        tempMat.flag ++;
        tempMat.max[cols] ++;
        return tempMat;
    }

    public void sub(int cols){
        cols --;
        board[max[cols]][cols] = ' ';
        flag --;
        max[cols] --;
    }
    public boolean complete(int i){
        if (i>7 || i<1) return false;
        return (max[i-1] >= 6);
    }
    public int points(){
        int soma = 0;
        for (int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(board[i][j] == ' ') continue;
                soma += contar(i,j,1,0,0);   //horizontal
                soma += contar(i,j,1,1,0);   //diagonal cima
                soma += contar(i,j,0,1,0);   //vertical 
                soma += contar(i,j,1,-1,0); //diagonal baixo
            }
        }
        return soma;
    }

    public int contar(int doty,int dotx,int dirx,int diry,int done){
        int flag2 = 1;
        if( (dotx+(dirx*3)) >= columns || (dotx+(dirx*3)) < 0 || (doty+(diry*3)) < 0 || (doty+(diry*3)) >= rows){
            //System.out.println((dotx +(dirx*3))+ "|" + (doty+(diry*3)) + "||" + rows + "|" + columns);
            flag2 = 0;
        }
        int extra;
        if((dotx-(dirx*3)) >= columns || (dotx-(dirx*3)) < 0 || (doty-(diry*3)) < 0 || (doty-(diry*3)) >= rows){
                extra = 0;
        }else if(board[doty-(diry*3)][dotx-(dirx*3)] == ' ' && done == 0){
                //print("a!!!!!",dotx,"|",doty,"||",dirx,"||",diry)
                extra = contar(doty,dotx,-dirx,-diry,1);
        }else extra = 0;
        if (flag2 == 0) return extra;
        char symb = board[doty][dotx];
        int flag = -1;
        if(symb == 'X'){
            flag = 1;
        }
        int count = 1;
        for(int i=1;i<4;i++){
            if (board[doty+(diry*i)][dotx+(dirx*i)] == symb) count ++;
            else if (board[doty+(diry*i)][dotx+(dirx*i)] == ' ') continue;
            else return 0;
        }
        switch (count) {
            case 1:
                return (1*flag)+extra;
            case 2:
                return (10*flag)+extra;
            case 3:
                return (50*flag)+extra;
            case 4:
                return (512*flag)+extra;
            default:
                return 0;
        }
    }

    public void printBoard(){
        for(int i=rows-1;i>=0;i--){
            System.out.print("| ");
            for(int j=0;j<columns;j++){
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println("------------------------------");
        System.out.println("  1   2   3   4   5   6   7");
    }



    public static void main(String[] args){
        Scanner scan =  new Scanner(System.in);
        Board gaming = new Board(6, 7, 0);
        gaming.add(2);
        System.out.println(gaming.points());
        gaming.printBoard();
    }
}
