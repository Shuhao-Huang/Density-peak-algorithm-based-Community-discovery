package weka_test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class GetS_MandM{
    private static int INF = Integer.MAX_VALUE;
    public static void main(String args[]) throws Exception {

        Graph karate = new Graph(34);
        File file = new File("Karate.txt");
        InputStreamReader f2 = new InputStreamReader(new FileInputStream(file), "GBK");
        BufferedReader bf = new BufferedReader(f2);
        String line;
        double e;
        int n=34;
        int i = 0,j;
        int flag1 = 0;
        double[][] Jaccard;
        double[][] min_path;
        double[][] Similarity;
        double[] density_sort;
        double[] density_list;
        double[] density;
        double[] min_dist_list;
        double[] min_dist_sort;
        double[][] disij;
        double[][] neighb;

        int[][] merge_com;
        int[] nonkeynode;
        int[][] community;
        List<Integer> list_key=new ArrayList<>();
        List<Integer> basicC=new ArrayList<>();
        int [] realcommunity={0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1};
        for (int p = 0; p < 93*5+2; p++) {
            line = bf.readLine();
            //包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
            if (line.length() >=12) {
                if (flag1 == 0) {
                    if(line.length()==12) {
                        i = Integer.parseInt(String.valueOf((line.charAt(11))));
                    }
                    else{
                        i=Integer.parseInt(String.valueOf((line.charAt(11))))*10+Integer.parseInt(String.valueOf((line.charAt(12))));
                    }

                    flag1 = 1;
                } else if (flag1 == 1) {
                    if(line.length()==12) {
                        j = Integer.parseInt(String.valueOf(line.charAt(11)));
                    }
                    else{
                        j=Integer.parseInt(String.valueOf(line.charAt(11)))*10+Integer.parseInt(String.valueOf(line.charAt(12)));
                    }
                    flag1 = 0;
                    karate.matrix[i-1][j-1]=1;
                }
            }
        }


        Floyd_path s_path = new Floyd_path(34);
        int begin=0;
        int end=33;
        s_path.findCheapestPath(begin,end,karate.matrix);
        List<Integer> list=s_path.result;


        componment_similarity sim=new componment_similarity(karate.matrix,s_path.dist,34,(double) 0.5);
        Jaccard=sim.getJaccard();
        min_path=sim.getMin_path();
        Similarity=sim.getSimilarity();

        IDPM idpm=new IDPM(Similarity,34,0.7,13,1);
        density=idpm.getDensity();
        density_sort=idpm.getDensity_sort();
        density_list=idpm.getL_density();
        min_dist_list=idpm.getMin_dist();
        min_dist_sort=idpm.getMin_dist_sort();
        e=idpm.threshold();
        System.out.println("threhold condition:"
                +e+'\n');

        list_key=idpm.getKeynodes();
        System.out.println(list_key);
        System.out.println(list_key.size());

        int[] test=new int[list.size()];
        Cluster_community clus=new Cluster_community(list_key,list_key.size(),Similarity,n,karate.matrix);
        nonkeynode=clus.getNonkeynode();
        for(int p=0;p<n-list_key.size();p++){
            System.out.print(nonkeynode[p]+" ");
        }
        System.out.println("\n");
        community=clus.getCommunity();
        System.out.println("主次社区节点矩阵");
        for(int p=0;p<list_key.size();p++){
            System.out.println("社区"+p);
            for(int k=0;k<14;k++){
                System.out.print(community[p][k]+"  ");
            }
            System.out.println("\n");
        }
        basicC=clus.basic_com();
        //test=clus.basic_com();
        System.out.println("主社区节点");
        for(int p=0;p<3;p++){
            System.out.println(basicC.get(p));
        }//主社区与非主社区

        disij=clus.Dis_com();
        merge_com=clus.mergeCom();
        neighb=clus.neighb();
        System.out.println("社区融合矩阵");
        for(int p=0;p<3;p++){
            System.out.print(basicC.get(p)+" ");
            for(int k=0;k<3;k++){
                System.out.print(disij[p][k]+ "  ");
            }
            System.out.print("\n");
        }//主社区节点
        System.out.println("节点的邻接点在各主社区占比");
        for(int p=0;p<n;p++){
            for(int k=0;k<basicC.size()+1;k++){
                System.out.print(p+" "+neighb[p][k]+"  ");
            }
            System.out.print("\n");
        }//邻接点各主社区占比
        System.out.println("社区融合：");
        for(int p=0;p<basicC.size();p++){
            for (int k=0;k<n;k++){
                System.out.print(merge_com[p][k]+"  ");
            }
            System.out.print("\n");
        }//主、次社区融合
        NMI nmi=new NMI(merge_com,n,realcommunity);
        int[] count;
        count=nmi.count();
        double num;
        num=  nmi.MutualInfor();
        System.out.println("real community:");

        for (int k=0;k<n;k++){
            if(realcommunity[k]==0){
                System.out.print(k+"  ");
            }
        }
            System.out.print("\n");
        for(int k=0;k<n;k++){
            if(realcommunity[k]==1){
                System.out.print(k+"  ");
            }
        }
        System.out.print("\n");

        /*
        for(int p=0;p<2;p++){
            for(int k=0;k<3;k++) {
                System.out.println(num[p][k]);
            }
        }*/

        System.out.println("NMI:"+num);
    }
}

