package weka_test;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;

public class Cluster_community {//
    private static int INF = Integer.MAX_VALUE;
    int[] nonkeynode;

    private int n;
    private int key;
    public int[] num;
    double[][] Similarity=new double[34][34];
    int [][] community;
    int [][] merge_com;
    int [][] matrix;
    double [][] neighb;
    double[][] disij;
    List<Integer> basicC=new ArrayList<Integer>();//基本社区关键节点表
    List<Integer> non_basic=new ArrayList<Integer>();
    List<Integer> list=new ArrayList<Integer>();
    Cluster_community(List<Integer> list,int key,double[][] Similarity,
                      int n,int[][] adjustMatrix){
        this.Similarity=Similarity;
        matrix=adjustMatrix;
        this.list=list;//关键节点表
        nonkeynode=new int[n-key];
        this.key=key;
        this.n=n;
        community=new int[list.size()][n];
        num=new int[list.size()];
        for(int i=0;i<list.size();i++){
            num[i]=0;
        }
        for(int i=0;i<key;i++){
            community[i][0]=list.get(i);
            for(int j=1;j<n;j++){
                community[i][j]=INF;
            }
        }
    }
    public int[] getNonkeynode(){//非关键节点数组
        int flag=0;
        int p=0;
        for(int i=0;i<n;i++){
            flag=0;
            for(int j=0;j<key;j++){
                if(i==list.get(j)){
                    flag=1;
                }
            }
            if(flag==0){
                nonkeynode[p]=i;
                p++;
            }
        }
        return nonkeynode;
    }

    public int[][] getCommunity(){//非关键节点的分配
        int max=0;
        int[] count=new int[list.size()];
        for(int i=0;i<n-list.size();i++){
            max=list.get(0);
            for(int j=1;j<list.size();j++){
                if(Similarity[nonkeynode[i]][list.get(j)]>Similarity[nonkeynode[i]][max]){
                    max=list.get(j);
                }
            }
            for(int p=0;p<list.size();p++){
                if(max==list.get(p)){
                    count[p]++;
                }
            }
        }
        for(int i=0;i<n-list.size();i++){
            max=list.get(0);
            for(int j=0;j<list.size();j++){
                if(Similarity[nonkeynode[i]][list.get(j)]>Similarity[nonkeynode[i]][max]){
                    max=list.get(j);//非关键节点与复合相似性最大的关键节点max
                }
            }
            for(int p=0;p<list.size();p++){
                if(max==list.get(p)){
                    community[p][count[p]]=nonkeynode[i];
                    count[p]--;
                }
            }
        }
        return community;
    }
    public List<Integer> basic_com(){
        for(int i=0;i<list.size();i++){
            non_basic.add(list.get(i));
        }
        for(int i=0;i<list.size();i++){
            for(int j=1;j<n;j++){
                if(community[i][j]!=INF){
                    num[i]++;
                }
            }
            if (num[i]>=2){
                basicC.add(list.get(i));
                non_basic.remove(list.get(i));
            }
        }
        return basicC;
    }

    public double[][] Dis_com(){//社区融合
        int count=0;
        int count2=0;
        double sum=0;
        double[] dis1=new double[basicC.size()];
        double[] dis2=new double[non_basic.size()];
        double[][] dis3=new double[basicC.size()][non_basic.size()];
        disij=new double[basicC.size()][non_basic.size()];
        //double[] dis3=new double[];
        for(int i=0;i<list.size();i++){//计算dis1
            if(basicC.contains(community[i][0])){
                for(int j=0;j<num[i]+1;j++)
                {
                    for(int p=0;p<num[i]+1;p++) {
                         sum = sum + Similarity[community[i][j]][community[i][p]];
                    }
                }
                dis1[count]=sum/( (num[i]+1)*(num[i]));
                sum=0;
                count++;
            }
        }
        //计算dis2
        count=0;
        sum=0;
        for(int i=0;i<list.size();i++){
            if(non_basic.contains(community[i][0])){
                for(int j=0;j<num[i]+1;j++){
                    for(int p=0;p<num[i]+1;p++){
                        sum=sum+Similarity[community[i][j]][community[i][p]];
                    }
                }
                if(num[i]!=0) {
                    dis2[count] = sum / ((num[i] + 1) * num[i]);
                }
                else{
                    dis2[count]=sum;
                }
                count++;
                sum=0;
            }
        }
        count=0;
        sum=0;
        //dis3
        for(int i=0;i<list.size();i++){
            if(basicC.contains(community[i][0])){
                count2=0;//所有基本社区关键节点
                for(int j=0;j<list.size();j++){
                    if(non_basic.contains(community[j][0])){//所有非基本社区的关键节点
                        for (int p = 0; p < num[i]+1; p++) {
                            for (int k = 0; k < num[j]+1; k++) {//基本社区i与非基本社区j中的节点相似度之和
                                sum = sum + Similarity[community[i][p]][community[j][k]];
                            }
                        }
                        dis3[count][count2] = sum / ((num[i]+1)*(num[j]+1));
                        sum=0;
                        count2++;
                    }
                }
                count++;
            }
        }
        count=0;
        count2=0;
        sum=0;
        for(int i=0;i<list.size();i++){
            if(basicC.contains(community[i][0])){
                count2=0;
                for(int j=0;j<list.size();j++){
                    if(non_basic.contains(community[j][0])){
                        disij[count][count2]=Math.abs((dis1[count]+dis2[count2])/2-disij[count][count2]);
                        count2++;
                    }
                }
                count++;
            }

        }


        return disij;
    }
    public int[][] mergeCom(){
        int count=0;
        int count2=0;
        int[] belongs=new int[non_basic.size()];
        merge_com=new int[basicC.size()][n];
        int[] non_index=new int[non_basic.size()];
        int min=0;

        for(int i=0;i<non_basic.size();i++){
            min=0;
            for(int j=0;j<basicC.size();j++){
                if(disij[j][i]<disij[min][i]){
                    min=j;
                }

            }
            belongs[i]=min;//min为主社区在dis中的次序
        }
        for(int i=0;i<list.size();i++){//复制主社区中的节点数到merge_com中
            if(basicC.contains(community[i][0])){
                for(int j=0;j<n;j++){

                    merge_com[count][j]=community[i][j];
                }
                count++;
            }else{
                non_index[count2]=i;
                count2++;
            }
        }
        count=0;

        for(int i=0;i<non_basic.size();i++){
            count=0;
            for(int j=0;j<n;j++){
                if(merge_com[belongs[i]][j]==INF){
                    merge_com[belongs[i]][j]=community[non_index[i]][count];
                    count++;
                }
            }
        }
        return merge_com;

    }
    public double[][] neighb(){
        neighb=new double[n][basicC.size()+1];
        for (int i=0;i<n;i++){//初始化邻接点矩阵
            for(int j=0;j<basicC.size()+1;j++){
                neighb[i][j]=0;
            }
        }
        int sum=0;
        for(int i=0;i<n;i++){
            sum=0;
            for (int j=0;j<n;j++){
                if(matrix[i][j]==1){
                    sum++;

                    for(int p=0;p<basicC.size();p++){
                        for(int k=0;k<n;k++){
                            if(merge_com[p][k]==j){
                                neighb[i][p+1]++;
                            }
                        }
                    }
                }
            }
            neighb[i][0]=sum;
            for(int l=1;l<basicC.size()+1;l++){
                neighb[i][l]=neighb[i][l]/sum;
            }
        }
        return neighb;

    }
}
