package fedQ.fedQ;

//import Methods;
//import Repository;
//import SPARQLRepository;

import java.io.File;
import java.util.Vector;

import org.openrdf.repository.Repository;
import org.openrdf.repository.sparql.SPARQLRepository;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception 
    {
        //System.out.println( "Hello World!" );
    
		File dataDir = new File(UserConfig.storePath+"\\nativestore");
		Methods m=new Methods();
		Repository repo= m.CreateNativeStore(dataDir);
		repo.initialize();

		//        
		//        File file = new File("C:\\Users\\adjroud\\Downloads\\export.rdf");
		//String baseURI = "http://example.org/example/local";
		//        m.AddRdfData(repo, file, baseURI);

		Repository repository = new SPARQLRepository("http://www.linkedmdb.org/sparql");
		//Repository repository = new SPARQLRepository("http://localhost:8080/openrdf-sesame/repositories/raffa");
		repository.initialize(); 

		String requete ="SELECT * WHERE {?subject ?pridicate ?object} LIMIT 50";




		Vector v= m.GetFromRepo(repository, requete);
		System.out.println(v.size());


		m.AddToRepo(v, repo);

		m.affichertt(repo, requete);

    }
}
