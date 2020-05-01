package weka_test;
import java.util.ArrayList;
import java.util.List;

public class IDPM {
    double[][] Similarity;//相似度矩阵

    double[] min_dist;//最小距离矩阵
    double[] min_dist_sort;
    double[] min_dist_ncnk;

    double[] density;//密度矩阵
    double[] density_sort;//节点密度矩阵
    double[] density_nck;
    int n;
    double e;//阈值
    double sum=0;
    double sc;
    int Nckn=0;
    double b=0;
    List<Integer> list=new ArrayList<Integer>();
    IDPM(double[][] matrix,int n,double Sc,int ncn,double b){
        this.b=b;//阈值参数
        this.n=n;//节点数
        Nckn=ncn;
        this.sc=Sc;
        density_nck=new double[ncn];
        Similarity=new double[n][n];
        density=new double[n];
        min_dist=new double[n];
        min_dist_ncnk=new double[Nckn];
        min_dist_sort=new double[n];
        density_sort=new double[n];
        Similarity=matrix;
    }


    public double[] getDensity(){//密度矩阵计算
        double des=0;

        for(int i=0;i<n;i++){
            sum=0;
            for(int j=0;j<n;j++){
                if(i!=j){
                    des=Math.pow(Math.E,Math.pow(Similarity[i][j]/sc,2));
                    sum=sum+des;
                }
            }
            density[i]=sum;
        }

        return density;

    }
    public double[] getL_density(){//最大的Nckn个节点密度向量
        double temp=0;
        int p=n;
        for(int i=0;i<n;i++){
            density_sort[i]=density[i];
        }
        for(int i=1;i<n;i++){
            for(int j=i;j>0;j--){
                if(density_sort[j]<density_sort[j-1]){
                    temp=density_sort[j];
                    density_sort[j]=density_sort[j-1];
                    density_sort[j-1]=temp;
                }
            }
        }
        for(int i=0;i<17;i++){
            temp=density_sort[i];
            density_sort[i]=density_sort[p-1];
            p--;
        }
        for(int i=0;i<Nckn;i++){
            for(int j=0;j<n;j++){
                if(density_sort[i]==density[j]){
                    density_nck[i]=j;
                }
            }
        }
        return density_nck;
    }

    public double[] getDensity_sort(){
        return density_sort;
    }
    public double threshold(){//阈值条件
        double avg_des=0;
        double dens_sum=0;
        double dens_abs=0;

        for(int i=0;i<Nckn;i++){
            dens_sum=density_sort[i]+dens_sum;
        }
        avg_des=dens_sum/Nckn;
        for(int i=0;i<Nckn;i++){
            dens_abs=dens_abs+Math.abs(density_sort[i]-avg_des);
        }
        e=(b/Nckn)*dens_abs;
        return e;
    }
    public double[] getMin_dist(){
        double max=0;
        double temp=0;
        int min=0;
        int[] count=new int[n];
        int[] list ;
        int p=n;
        int t=0;
        for(int i=0;i<n;i++){
            count[i]=0;
        }
        for(int i=0;i<n;i++){
            if(density[i]>=(density_sort[0]-e)){
                max=Similarity[i][0];
                for(int j=1;j<n;j++){
                    if(Similarity[i][j]>max){
                        max=Similarity[i][j];
                    }
                }
                min_dist[i]=max;
            }
            else{

                for(int k=0;k<n;k++){
                    if(density[k]-e>density[i]){
                        count[i]++;
                    }
                }
                for(int k=0;k<n;k++){
                    if(density_sort[n-count[i]]==density[k]){
                        min=k;
                    }
                }
                min_dist[i]=Similarity[i][min];
            }

        }
        for(int i=0;i<n;i++){
            min_dist_sort[i]=min_dist[i];
        }
        for(int i=1;i<n;i++){
            for(int j=i;j>0;j--){
                if(min_dist_sort[j]<min_dist_sort[j-1]){
                    temp=min_dist_sort[j];
                    min_dist_sort[j]=min_dist_sort[j-1];
                    min_dist_sort[j-1]=temp;
                }
            }
        }
        for(int i=0;i<17;i++){
            temp=min_dist_sort[i];
            min_dist_sort[i]=min_dist_sort[p-1];
            min_dist_sort[p-1]=temp;
            p--;

        }
        for(int i=0;i<Nckn;i++){
            for(int j=0;j<n;j++){
                if(min_dist_sort[i]==min_dist[j]){
                    min_dist_ncnk[i]=j;
                }
            }
        }
        return min_dist_ncnk;
    }
    public double[] getMin_dist_sort(){
        return min_dist_sort;
    }

    public List<Integer> getKeynodes(){
        for(int i=0;i<Nckn;i++){
            for(int j=0;j<Nckn;j++){
                if(min_dist_ncnk[i]==density_nck[j]){
                    list.add((int) min_dist_ncnk[i]);
                }
            }
        }
        return list;
    }
}
