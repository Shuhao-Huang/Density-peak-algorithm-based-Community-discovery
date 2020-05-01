package weka_test;

public class componment_similarity<i> {
    private static int INF = Integer.MAX_VALUE;
    int[][] matrix;
    double[][] jaccard;
    double[][] min_path;
    int [][]  path;
    double[][] Similarity;
    int n;
    double a;

    componment_similarity(int[][] matrix, int[][]  path, int n, double a){
        this.matrix=matrix;
        this.path=path;
        this.n=n;
        this.a=a;
        jaccard=new double[n][n];
        min_path=new double[n][n];
        Similarity=new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                jaccard[i][j]=0;
            }
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                Similarity[i][j]=0;
            }
        }

    }

    public double[][] getJaccard(){
        double sum1=0;
        double sum2=0;
        double max_jac=0;

        for(int i=0;i<n;i++){
            for(int j =i+1;j<n;j++){
                {
                    for(int p=0;p<n;p++){
                        if(matrix[i][p]==matrix[j][p]&&matrix[i][p]!=INF){
                            sum1++;//求交集

                        }
                        if(matrix[i][p]==1||matrix[j][p]==1){
                            sum2++;//求并集
                        }
                    }
                    jaccard[i][j]=sum1/sum2;
                    if(jaccard[i][j]>max_jac){
                        max_jac=jaccard[i][j];
                    }
                }
            }
        }
        for(int i =0;i<n;i++){
            //标准化jaccard系数
            for(int j =0;j<n;j++){
                if(jaccard[i][j]!=0) {
                    jaccard[i][j]=jaccard[i][j]/max_jac;
                }
            }
        }
        for(int i=1;i<n;i++){
            for(int j =0;j<i;j++){
                jaccard[i][j]=jaccard[j][i];
            }
        }
        return jaccard;
    }

    public  double[][] getMin_path() {
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(path[i][j]!=INF) {
                    min_path[i][j]=(double) 1/path[i][j];
                }
                else{
                    min_path[i][j]=0;
                }

            }
        }

        return min_path;

    }
    public double[][] getSimilarity(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                    Similarity[i][j] = ((double)a*jaccard[i][j])+((double)(1-a)*min_path[i][j]);
                }

            }

        return Similarity;
    }



}
