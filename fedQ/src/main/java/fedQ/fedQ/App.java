package fedQ.fedQ;

import static com.oracle.nio.BufferSecrets.instance;
import static com.sun.org.apache.regexp.internal.RETest.test;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import jdk.nashorn.internal.objects.NativeDebug;
import static jdk.nashorn.internal.objects.NativeRegExp.test;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.sail.nativerdf.NativeStore;
import static sun.awt.image.PixelConverter.Ushort4444Argb.instance;

/**
 *
 * @author adjroud
 */
public class main {

    public static void main(String[] args) throws Exception {
        //instancier la class qui conient les methodes
        Methods m = new Methods();

        //creation et initialisation d un depot local
        Repository repo = m.CreateNativeStore();
        repo.initialize();

        //creation d'une connexion
        RepositoryConnection con = repo.getConnection();

        //recuperer les triple paternes
        String[] s = m.getTriplePattern();

        //recuperer le sources pertinantes 
        HashMap<MapKey, Boolean> res = m.askSourceForTriplePattern(s);

        // recuperer les resultats et les ajouter dans le depot local
        m.getResults(con, res);

        //afficher le contenu do depot local   
        m.viewAll(con);

    }

}
