/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raouf;

import java.io.File;
import java.util.Stack;
import java.util.Vector;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 *
 * @author adjroud
 */
public class main {

    public static void main(String[] args) throws Exception {
     //creation d un depot local
        File dataDir = new File("C:\\Users\\adjroud\\Desktop\\nativestore");
        Methods m = new Methods();
        Repository repo = m.CreateNativeStore(dataDir);
        repo.initialize();
        RepositoryConnection con = repo.getConnection();

        String requete = "SELECT * WHERE {?subject ?pridicate ?object} LIMIT 60";

        String[] s = m.getTriplePattern("C:\\Users\\adjroud\\Desktop\\requete.txt");
        String[] s2 = m.getEndPointsList("C:\\Users\\adjroud\\Desktop\\endpoints.txt");

        Vector<Vector> v = new Vector<>();

        v = m.getPertSources(s, s2);

        for (int i = 0; i < v.size(); i++) {
            if (v.elementAt(i).elementAt(2).equals(true)) {
          
                Repository r = new SPARQLRepository(s2[(int) v.elementAt(i).elementAt(1)]);
                r.initialize();
                RepositoryConnection c = r.getConnection();
                

                m.FromSourceToRepo(con, s[(int) v.elementAt(i).elementAt(0)], c);

            }
        }

        m.affichertt(con, requete);

    }

}
