
package paillier.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Methods {
    public static int prime(int Nc, int Nv){
        List<Integer> primes = new ArrayList<Integer>();
        int max_count=200;
        if (Nc>=8 && Nv==5){
            max_count=1300;
        }
        int count = 0, i;

        for(int num=1; count<max_count; num++){
            for(i=2; num%i != 0; i++);

            if(i == num){
                primes.add(num);
                count++;
            }
        }
        Random rnd = new Random();
        return primes.get(rnd.nextInt(max_count));
    }
    
    
    public static int gcd(int a, int b) {
        int gcd = 1;
        for(int i = 1; i <= a && i <= b; ++i){
            // Checks if i is factor of both integers
            if(a % i==0 && b % i==0)
                gcd = i;
        }
        return gcd;
    }
 
    
    public static BigInteger g_calc(long n,int λ){
        BigInteger random_g;
        BigInteger one = new BigInteger("1");
        while(true){
            long g = ThreadLocalRandom.current().nextLong(n);
            BigInteger ni = BigInteger.valueOf(n);
            BigInteger x = BigInteger.valueOf(g);
            BigInteger n2 = new BigInteger(String.valueOf((long)Math.pow(n,2)));
            random_g= new BigInteger(String.valueOf(   ((x.pow(λ)).mod(n2).subtract(one)).divide(ni)   ));
            if(gcd(random_g.intValue(), (int) n) == 1 ){
                break;
            } 
        }
        return random_g;    
    }
     
    
    public static BigInteger μ_calc(BigInteger g, int λ, long n){
        BigInteger one = new BigInteger("1");
        BigInteger ni = BigInteger.valueOf(n);
        BigInteger n2 = new BigInteger(String.valueOf((long)Math.pow(n,2)));
        BigInteger μ = new BigInteger(String.valueOf(((g.pow(λ).mod(n2)).subtract(one).divide(ni)).modInverse(ni)));
     
        return μ;
    }
    
    
    public static List votes(int Nc, int Nv, int b){
        ArrayList<Integer> m_list = new ArrayList<Integer>();
        int sum=0;
        int fin=0;
        int [] s=new int[2];
        for (int voter=1; voter<=Nv; voter++){
            for (int vot=0; vot<2; vot++){
                //random decision which to vote
                Random v = new Random();
                int vote = v.nextInt(Nc);
                s[vot] = (int) Math.pow(b, vote);
            }
            if(s[0]==s[1]){
                voter-=1;
            }else{;
                fin=s[0]+s[1];
                sum+=fin;
                m_list.add(fin);
            }
        }
        System.out.println("The Sum of the votes is: "+sum);
 
        return m_list;
    }
    
    public static void results(int Nc, int b, int sum){
        
        ArrayList<votes> votes=new ArrayList<votes>();
        
        for(int candidate=Nc-1; candidate>=0; candidate--){
            System.out.println("Total votes for candidate number "+candidate+" is "+sum/(int) Math.pow(b, candidate));
            votes.add(new votes(candidate,sum/(int) Math.pow(b, candidate)));
            sum=sum%(int) Math.pow(b, candidate);
        }
        
        ArrayList<Integer> thesi=new ArrayList<Integer>();
       
        int max=0;
        int maxTwo=0;
       
        for(int v=Nc-1; v>=0;v--){
            if(votes.get(v).vote>=max){
                if (votes.get(v).vote==max){
                    
                }else{
                    maxTwo = max;
                    max=votes.get(v).vote;
                }
            }
             else if (votes.get(v).vote>=maxTwo){
                 if (votes.get(v).vote==maxTwo){
                     
                }else{
                    maxTwo=votes.get(v).vote;          
                 }
          }
        }
        
        for (int a=0; a<votes.size(); a++){
            if(votes.get(a).vote==max||votes.get(a).vote==maxTwo){
                thesi.add(a);
            }
        }  

        for(int x=0; x<thesi.size();x++){
            System.out.println("---Το στέλεχος του ιού της γρίπης για το οποίο πρέπει να εμβολιαστεί ο πληθυσμός είναι το: "+votes.get(thesi.get(x)).stelexos);
         }
    }
  
    
    public static BigInteger encryption(BigInteger g, ArrayList m_list, int n, int i){
       Random rnd = new Random(); 
       int r = 1+rnd.nextInt((int) n);
       BigInteger y = BigInteger.valueOf(r);
       System.out.println("r is: "+r);
       BigInteger num = new BigInteger(String.valueOf(g.pow((int) m_list.get(i)).multiply(y.pow(n))));
       System.out.println("num is: "+num);
       BigInteger n2 = new BigInteger(String.valueOf((long)Math.pow(n,2)));
      
       BigInteger c=num.mod(n2);
       System.out.println(c);
       return c;
   }
   
    
    public static int decryption(BigInteger μ, int n, int λ, BigInteger c){
       BigInteger m,n2,x,z,y;
      
       BigInteger one = new BigInteger("1");
       BigInteger n1 = BigInteger.valueOf(n);
       n2 = new BigInteger(String.valueOf((long)Math.pow(n,2)));
       x = (c.pow(λ));
       y = new BigInteger(String.valueOf(((x.mod(n2)).subtract(one)).divide(n1)));
       z = new BigInteger(String.valueOf(y.multiply(μ)));
       m = new BigInteger(String.valueOf(z.mod(n1)));
       int m1 = m.intValue();
       return m1;
   }
}
