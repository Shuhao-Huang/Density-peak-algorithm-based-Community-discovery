package weka_test;

public class Graph {
    private static int INF = Integer.MAX_VALUE;
    private int vertexsize;
    public int[][] matrix;
    public int[] vertexs;
    private static int maxsize=1000;
    public Graph(int vertexsize){
        this.vertexsize=vertexsize;
        matrix = new int[vertexsize][vertexsize];
        vertexs=new int[vertexsize];
        for(int i=0;i<vertexsize;i++){
            for(int j=0;j<vertexsize;j++){
                matrix[i][j]=INF;

            }


        }
    }


    public int getOutdegree(int num){
        int degree=0;
        for(int i=0;i<vertexsize;i++){
            if(matrix[num][i]!=0){
                degree++;
            }
        }
        return degree;
    }

}

