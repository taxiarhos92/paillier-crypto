/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paillier.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author taxid
 */
public class PaillierCrypto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       try{
            int ans = 0;
            do {
                BufferedReader ed = new BufferedReader(new InputStreamReader(System.in));//dexetai o client ta mhnymata toy xrhsth
                Methods method= new Methods();
                System.out.println("Wecome to Paillier Cryprosystem.");
                
                System.out.println("How many candidates Nc? ");
                int Nc = Integer.parseInt(ed.readLine());
                System.out.println("How many voters Nv? ");
                int Nv = Integer.parseInt(ed.readLine());
                
                int b = Nv + 1; //base b
                int m_max=0;
                for (int i=Nc-2; i<Nc; i++){
                    m_max += Math.pow(b,i); //calculates m_max for one vote
                }
                int T_max=Nv*m_max;
                
                System.out.println("T-max is "+T_max);
            

                int p,q,n;
                do{
                    p = method.prime(Nc, Nv); //finds a random p
                    q = method.prime(Nc,Nv); //finds a random q 
                    n = p*q; // calculates n    
                }while(n<T_max || p<Math.sqrt(T_max+1)|| q<Math.sqrt(T_max+1)); // n must be greater than Tmax
                System.out.println("p = " + p);
                System.out.println("q = " + q);
                System.out.println("n = " + n);
                
                int λ = (p-1)*(q-1)/method.gcd(p-1,q-1); // calculates λ
                System.out.println("λ = " + λ);
                
                BigInteger g = method.g_calc(n,λ);
                System.out.println("g = " + g);
                
                BigInteger μ = method.μ_calc(g,λ,n);
                System.out.println("μ = " + μ);
                                
                System.out.format("Public key: (%d,%d)\n",n,g);
                System.out.format("Private key: (%d,%d)\n" , p,q);  
                
                System.out.println("++++++++++++++++++++++++++++++++");
                ArrayList m = (ArrayList) method.votes(Nc,Nv,b);
                System.out.println("The votes(m) are: " + m);
                System.out.println("++++++++++++++++++++++++++++++++");
                System.out.println("Encryption is starting");
                List<BigInteger> crypto = new ArrayList<BigInteger>();
                BigInteger P=BigInteger.TEN.pow(0);
                for (int i=0; i<m.size(); i++){
                    BigInteger temp = method.encryption(g,m,n,i);
                    crypto.add(temp);
                    P = P.multiply(crypto.get(i));
                   
                }
                
                System.out.println("The encrypted votes are: "+crypto);
                
                BigInteger T =P.mod(new BigInteger(String.valueOf((long)Math.pow(n,2))));
                System.out.println("T is "+T);
                System.out.println("++++++++++++++++++++++++++++++++");
                System.out.println("Decryption is starting");
              
                int me=method.decryption(μ,n,λ,T);
                System.out.println("The Sum of the decrypted votes is: "+me);
                
                method.results(Nc,b,me);
                
                System.out.println("Press (1) to run again. For exit press (0).");
                ans = Integer.parseInt(ed.readLine());//to metatrepei se integer
            }while( ans != 0);//end of while
        }//end of try
        catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}