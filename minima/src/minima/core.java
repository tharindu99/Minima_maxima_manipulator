package minima;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class core {
static int countme = 1;
static String company = "V_max_";
static int min_counter = 1;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 CSVReader reader;
		 ArrayList<Double> PRC = new ArrayList<Double>();
		 ArrayList Date = new ArrayList();
	     String [] nextLine;
	     int count = 0;
	     
	     try {
	    	 reader = new CSVReader(new FileReader("src/minima/data/2004-92611.csv"));
			while ((nextLine = reader.readNext()) != null) {
			    // nextLine[] is an array of values from the line
				if(count++!= 0){
				double temp = Double.parseDouble(nextLine[2]);
				PRC.add(temp);
				Date.add(nextLine[0]);
				}
			    //System.out.println(nextLine[0]+" " + nextLine[1] );
			 }
			find_d_t_D(PRC,Date);
			//System.out.println(PRC.size());
			System.out.println("completed..");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void find_d_t_D(ArrayList<Double> p,ArrayList Date) {
		int id_d=0,id_t=0,id_D=0;
		double d=0,t=0,D=0;
		double thshld= 0.05;
		int temp_id = 0;
		boolean con_foundT = false, con_foundD =false;
		//System.out.println(p);
			int i = 0;
		do {
			id_d = i;
			d = p.get(i);
			for (int j = i+1; j < p.size(); j++) {
				con_foundT = false;
				con_foundD =false;
				if(p.get(i)*(1+thshld)<=p.get(j)){
					id_t = j;
					t = p.get(j);
					con_foundT = true;
					//System.out.println("T :"+id_t);
					
					for (int k = id_t+1; k < p.size(); k++) {
						//System.out.println(p.get(i));
						if(p.get(id_t)*(1-thshld)>=p.get(k)){
							id_D = k;
							D = p.get(k);
							con_foundD = true;
							//System.out.println("D :"+D);
							break;
						}
					}
					
					break;
				}
			}
				
			
			if(con_foundT & con_foundD ){
				double Pmin_pre = minfinder(p, id_d, id_t);
				double Pmin_post = minfinder(p, id_t, id_D);
				double[] t_data = maxfinder(p, id_d, id_D);
				
				/*System.out.println("output : d: "+ d +" t: "+t+" D: "+D );
				System.out.println("output : d:     "+ id_d +" t:    "+id_t+" D:    "+id_D );
				System.out.println("pre min :"+Pmin_pre+" post min :"+Pmin_post);
				System.out.println("t : "+t_data[0]+" t id : "+t_data[1]);*/
				temp_id = i;
				i=id_D+1;
				double t_val = t_data[1];
				int tt = (int) t_val;
				//System.out.println("i :"+i+ " length : "+p.size());
				//System.out.println(Date.get(id_d)+", "+Date.get(tt)+" "+Date.get(id_D));
				
				boolean valid_minima = minimaVerifier(p, tt, Pmin_pre, Pmin_post, thshld, thshld);
				//System.out.println(min_counter+++": validated :"+valid_minima);
				//System.out.println(".......................................................");
				if(valid_minima){
				System.out.print(company+min_counter+++"=[['days',");
					for (int s = id_d; s < id_D; s++) {
						System.out.print("'"+Date.get(s)+"', ");
					}
				System.out.println("'"+Date.get(id_D)+"' ],");
				
				System.out.print("['PseudoPRC',");
				for (int s = id_d; s < id_D; s++) {
					System.out.print(p.get(s)+", ");
				}
					System.out.println(p.get(id_D)+" ]];");
				}
				
				
				if(temp_id==i)break;
			}else{
				i++;
			}
			
		} while (i < p.size());
		
		
	}
	
	public static double minfinder(ArrayList<Double>P , int start,int end){
		double min = P.get(start);
		double id_t = 0;
		for (int i = start+1; i <= end; i++) {
			 min = Math.min(P.get(i), min);
			 
		}
		
		return min;
	}
	public static double[] maxfinder(ArrayList<Double>P , int start,int end) {
		double max = P.get(start);
		double[] rtn = new double [2] ;
		for (int i = start+1; i < end; i++) {
			 max = Math.max(P.get(i), max);
		}
		for (int i = start+1; i < end; i++) {
			if(P.get(i)==max){
				rtn[1] = i;
				break;
			}
		}
		rtn[0] = max;
		return rtn;
	}
	
	public static boolean minimaVerifier(ArrayList<Double> P, int t,double Pmin_pre,double Pmin_post,double d, double D) {
		
		double pre_con = (P.get(t)-Pmin_pre)/P.get(t);
		double post_con = (P.get(t)-Pmin_post)/P.get(t);
		boolean a ;
		if (pre_con>=d & post_con>=D) {
			//System.out.println(countme+++" : valid minima...");
			//System.out.println("t : "+P.get(t)+" Pmin_pre :"+Pmin_pre+" Pmin_post :"+Pmin_post);
			//System.out.println("pre_con : "+pre_con+" post con :"+post_con);
			a= true;
		}else{
			//System.out.println(" not a minima ..");
			//System.out.println("t : "+P.get(t)+" Pmin_pre :"+Pmin_pre+" Pmin_post :"+Pmin_post);
			//System.out.println("pre_con : "+pre_con+" post con :"+post_con);
			a=false;
		}
		
		return a;
	
				
	}
}
