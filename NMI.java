package weka_test;

import java.util.ArrayList;

public class NMI {
    private static int INF=Integer.MAX_VALUE;
    int[][] com;
    int [] real_com;
    int[] count;
    int[] count2;
    int n;
    double MI;
    NMI(int [][] com,int n,int[] real_com){
        this.n=n;
        this.real_com=real_com;
        this.com=com;
        count=new int[3];
        count2=new int[2];
    }
    public int[] count(){
        for(int i=0;i<3;i++){
            for(int j=0;j<n;j++){
                if(com[i][j]!=INF){
                    count[i]++;
                }
            }

        }
        for(int i=0;i<n;i++){
            if(real_com[i]==0){
                count2[0]++;
            }
            else
                count2[1]++;

        }
        return count;
    }
    public double MutualInfor(){
        double Ha=0;
        double hab=0;
        int [] com_pre=new int[n];
        int[][] count_mi=new int[2][3];

        for(int i=0;i<2;i++){
            for(int j=0;j<3;j++){
                count_mi[i][j]=0;
            }
        }

        for(int i=0;i<n;i++){
            for(int j=0;j<count[0];j++){
                if(com[0][j]==i)
                    com_pre[i]=0;
            }
            for(int p=0;p<count[1];p++){
                if(com[1][p]==i)
                    com_pre[i]=1;
            }
            for(int k=0;k<count[2];k++){
                if(com[2][k]==i)
                    com_pre[i]=2;
            }
        }
        for(int i=0;i<3;i++){
            Ha=Ha-Math.log(((double) count[i]/n));
        }

        for(int i=0;i<n;i++){
            if(real_com[i]==0) {
               switch (com_pre[i]){
                   case 0:
                       count_mi[0][0]++;
                       break;
                   case 1:
                       count_mi[0][1]++;
                       break;
                   case 2:
                       count_mi[0][2]++;
               }
            }
            else{
                switch (com_pre[i]){
                    case 0:
                        count_mi[1][0]++;
                        break;
                    case 1:
                        count_mi[1][1]++;
                        break;
                    case 2:
                        count_mi[1][2]++;
                }
            }
        }
        for(int i=0;i<2;i++){
            for(int j=0;j<3;j++){
                hab=hab-((double)count2[i]/n)*((double) count_mi[i][j]/count2[i])*(Math.log((double) count_mi[i][j]/count2[i]));
            }
        }


        return (Ha-hab)/Ha;
    }

}
