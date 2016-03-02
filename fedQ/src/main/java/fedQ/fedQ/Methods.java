/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fedQ.fedQ;


import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.antlr.runtime.ANTLRFileStream;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.turtle.TurtleWriter;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 *
 * @author adjroud
 */
public class Methods {

//Creation d'un depot local
    public Repository CreateNativeStore() throws RepositoryException {
        File dataDir = new File("nativestore");
        String indexes = "spoc,posc,cosp";
        Repository repository = new SailRepository(new NativeStore(dataDir, indexes));
        repository.initialize();
        return repository;
    }

    //afficher le contenu d'un repo
    public void viewAll(RepositoryConnection connection) throws RepositoryException, MalformedQueryException, QueryEvaluationException {

        String requete = "SELECT * WHERE {?subject ?pridicate ?object} LIMIT 100";

        TupleQuery selectQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, requete);
        TupleQueryResult res = selectQuery.evaluate();

        while (res.hasNext()) {
            System.out.println("****************************************");
            // chaque ligne du résultat est un BindingSet  
            BindingSet aBinding = res.next();
            //parcourer et afficher le contenu
            for (String aBindingName : res.getBindingNames()) {
                System.out.println("La valeur de " + aBindingName + "    est     : " + aBinding.getValue(aBindingName));
            }
        }
    }
//interoger les end points par ASK

    public Boolean askSource(String request, RepositoryConnection connection)
            throws RepositoryException, MalformedQueryException,
            QueryEvaluationException {

        String requteASK = "ASK  {" + request + "}";
        System.out.println(requteASK);

        // on initialise la query.
        BooleanQuery booleanQuery = connection.prepareBooleanQuery(
                QueryLanguage.SPARQL, requteASK);

        // on l'exÃ©cute
        Boolean result = booleanQuery.evaluate();

        return result;

    }

    //recuperer les triples patternes
    public String[] getTriplePattern() throws IOException {
        ANTLRFileStream inputQuery = new ANTLRFileStream("ressources\\requete.txt");
        String req = inputQuery.toString();
        return req.split(System.getProperty("line.separator"));

    }

    // Recuperer les endPoints
    public String[] getEndPointsList() throws IOException {
        ANTLRFileStream inputSource = new ANTLRFileStream("ressources\\endpoints.txt");
        String sources = inputSource.toString();
        String[] linesSources = sources.split(System
                .getProperty("line.separator"));
        System.out.println(linesSources.length);

        return linesSources;
    }

    //recuperer les resultats comme graphes et les ajouter au depot local
    public void FromSourceToRepo(RepositoryConnection con, String request,
            RepositoryConnection connection)
            throws Exception, MalformedQueryException {

        String constructQuery = "CONSTRUCT  { " + request + "} where  { "
                + request + "} LIMIT 10";

        GraphQueryResult graphResult;
        graphResult = connection.prepareGraphQuery(QueryLanguage.SPARQL,
                constructQuery).evaluate();

        while (graphResult.hasNext()) {

            //ajouter le graphe au depot local
            con.add(graphResult.next().getSubject(), graphResult.next().getPredicate(), graphResult.next().getObject());
            //graphe suivant
            graphResult.next();

            System.out.println("graphe   :" + graphResult.next());

        }

    }

//interoger les endpoints pour chaque TP
    public HashMap<MapKey, Boolean> askSourceForTriplePattern(
            String[] triplePattern) throws IOException, RepositoryException,
            MalformedQueryException, QueryEvaluationException {

        String[] linesSources = getEndPointsList();
        HashMap<MapKey, Boolean> askResult = new HashMap<MapKey, Boolean>();
        RepositoryConnection con;
        // Parcourir les endPoints pour faire une requete Ask
        for (int i = 0; i < linesSources.length; i++) {
            // Demande de connection à la source

            Repository r = new SPARQLRepository(linesSources[i]);
            r.initialize();
            con = r.getConnection();
            for (int j = 0; j < triplePattern.length; j++) {
                // Récuperer le résultat
                askResult.put(new MapKey(i, j), askSource(triplePattern[j], con));
            }
            con.close();
        }
        return askResult;
    }

    // recuperer les resultats et les ajouter dans le depot local
    public void getResults(RepositoryConnection con,
            HashMap<MapKey, Boolean> askResult)
            throws Exception {
        //recuperer les triples patterns et les endpoints
        String[] linesSources = getEndPointsList();
        String[] triplePattern = getTriplePattern();

        RepositoryConnection connection;

        // Parcourir les endPoints pour faire un Select
        for (int i = 0; i < linesSources.length; i++) {
            // Demande de connection Ã  la source
            Repository r = new SPARQLRepository(linesSources[i]);
            r.initialize();
            connection = r.getConnection();

            for (int j = 0; j < triplePattern.length; j++) {
                // Si le Ask est true, on exucute pour récupérer le résultat
                if (askResult.get(new MapKey(i, j)) != null
                        && askResult.get(new MapKey(i, j))) {
					// nombreDeResultat = nombreDeResultat +

                    // charger les fichier dans un repo && Interroger le depot local
                    FromSourceToRepo(con, triplePattern[j], connection);

                }
            }

            connection.close();
        }

    }
}
