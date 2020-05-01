package weka_test;
import java.util.ArrayList;
import java.util.List;
public class Floyd_path {
    private static int INF = Integer.MAX_VALUE;
    public List<Integer> result = new ArrayList<Integer>();
    int ver;
    public int[][] dist;
    public int[][] path;

    public Floyd_path(int ver) {
        this.ver = ver;
        path = new int[ver][ver];
        dist = new int[ver][ver];
    }

    public void findCheapestPath(int begin, int end, int[][] matrix) {
        findpath(matrix);
        result.add(begin);
        findPath_2(begin, end);
        result.add(end);
    }

    public void findPath_2(int i, int j) {
        int k = path[i][j];
        if (k == -1) return;
        findPath_2(i, k);
        result.add(k);
        findPath_2(k, j);
    }

    public void findpath(int[][] matrix) {
        int size = ver;
        //initialize dist and path
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                path[i][j] = -1;
                dist[i][j] = matrix[i][j];
            }
        }
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for(int j=0;j<size;j++){
                    if(dist[i][k]!=INF&&
                            dist[k][j]!=INF&&
                            dist[i][k]+dist[k][j]<dist[i][j]) {//dist[i][k]+dist[k][j]>dist[i][j]-->longestPath
                        dist[i][j] = dist[i][k] + dist[k][j];
                        path[i][j] = k;
                    }
                }
            }
        }


    }
}

