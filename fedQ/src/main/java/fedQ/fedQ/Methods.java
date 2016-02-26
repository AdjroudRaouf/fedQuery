/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fedQ.fedQ;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.nativerdf.NativeStore;
/**
 *
 * @author adjroud
 */
public class Methods {
    
    public Repository CreateNativeStore(File dataDir) throws RepositoryException{
       

String indexes = "spoc,posc,cosp";
Repository repository = new SailRepository(new NativeStore(dataDir, indexes));
repository.initialize();
        return repository;
    }
   
    
    public void AddFileRdfData(Repository repository,File file,String baseURI) throws IOException{
        
   
try {
    RepositoryConnection con = repository.getConnection();
      con.add(file, baseURI, RDFFormat.RDFXML);

}
catch (OpenRDFException e) {
   // handle exception
}
    }
    
   public void affichertt(Repository repository,String requete) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
      RepositoryConnection connection = repository.getConnection();  

    	TupleQuery selectQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, requete);
    	TupleQueryResult res = selectQuery.evaluate();

    	while(res.hasNext()) {  
            System.out.println("****************************************");
    	    // chaque ligne du résultat est un BindingSet  
    	    BindingSet aBinding = res.next();  

    	    for (String aBindingName : res.getBindingNames()) {  
    		System.out.println("La valeur de "+aBindingName+"    est     : "+aBinding.getValue(aBindingName));  
    	 }  
    	} 
   }
   
   
   
   public void addStm(Repository repository,String a1,String a2,String a3){
       ValueFactory f = repository.getValueFactory();
URI s1 = f.createURI(a1);
URI s2 = f.createURI(a2);
URI s3 = f.createURI(a3);

try { RepositoryConnection con = repository.getConnection();
try {
con.add(s1, s2, s3);


} finally { con.close(); }
} catch (OpenRDFException e) {}
   }
    
    
   
   
    public Vector GetFromRepo(Repository repository,String requete) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
      RepositoryConnection connection = repository.getConnection();  
            

Vector v = new Vector();
System.out.println(requete);
    	TupleQuery selectQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, requete);
    	TupleQueryResult res = selectQuery.evaluate();
    	// on itère sur les résultats  
    	while(res.hasNext()) {        
    	    // chaque ligne du résultat est un BindingSet  
            
    	    BindingSet aBinding = res.next();  

            for (String aBindingName : res.getBindingNames()) {  
    	
                Value s=aBinding.getValue(aBindingName);
                String s2=s.toString();
                System.out.println(s2);
               
          
           v.add(s2);
      	 }
     	} 
        connection.close();
        return v;
        
   }
    
    
    
    
    public void AddToRepo(Vector v,Repository repo) throws RepositoryException{
  
   int i=0;
        while (i<v.size()) {            

    String s1=(String) v.elementAt(i);
    String s2=(String) v.elementAt(i+1);
    String s3=(String) v.elementAt(i+2);
    
   
       addStm(repo, s1, s2, s3);

i=i+3;
        }
    }
    
    
   public Boolean askSource(String request, RepositoryConnection connection)
            throws RepositoryException, MalformedQueryException,
            QueryEvaluationException {

        String requteASK = "ASK  {" + request + "}";
        System.out.println(requteASK);
        
        BooleanQuery booleanQuery = connection.prepareBooleanQuery(
                QueryLanguage.SPARQL, requteASK);

        Boolean result = booleanQuery.evaluate();

        return result;

    }
   
   
   
   
   
   
    public String[] getTriplePattern(String query) throws IOException {

        ANTLRFileStream inputQuery = new ANTLRFileStream(query);
        String req = inputQuery.toString();
        return req.split(System.getProperty("line.separator"));

    }

    public String[] getEndPointsList(String endPoints) throws IOException {
        // RÃ©cupÃ©rer les endPoints
        ANTLRFileStream inputSource = new ANTLRFileStream(endPoints);
        String sources = inputSource.toString();
        String[] linesSources = sources.split(System
                .getProperty("line.separator"));
        System.out.println(linesSources.length);
        Vector<String> vsource = new Vector<String>();

        String[] nvList = new String[vsource.size()];

        for (int j = 0; j < vsource.size(); j++) {
            nvList[j] = vsource.get(j);
        }
        return linesSources;
    }
    
    
    
    
    
    
    public Vector<Vector> getPertSources(String[] s, String[] s2) throws RepositoryException, MalformedQueryException, QueryEvaluationException {

        Vector<Vector> v = new Vector<>();

        for (int i = 2; i < s.length - 1; i++) {
            for (int j = 0; j < s2.length; j++) {

                String rqt = "select * where {" + s[i] + "}";

                Repository rep = new SPARQLRepository(s2[j]);
                rep.initialize();
                RepositoryConnection c = rep.getConnection();

                boolean b = askSource(rqt, c);
                System.out.println(b);

                Vector l = new Vector();
                l.add(i);
                l.add(j);
                l.add(b);
                v.add(l);

            }
        }

        for (int i = 0; i < v.size(); i++) {
            System.out.println(v.elementAt(i).elementAt(0));
            System.out.println(v.elementAt(i).elementAt(1));
            System.out.println(v.elementAt(i).elementAt(2));
            System.out.println("********************************");
        }

        return v;
    }
    
    
    
     public void FromSourceToRepo(RepositoryConnection con, String request,
            RepositoryConnection connection)
            throws Exception, MalformedQueryException {

        String constructQuery = "CONSTRUCT  { " + request + "} where  { "
                + request + "} LIMIT 10";

        GraphQueryResult graphResult;
        graphResult = connection.prepareGraphQuery(QueryLanguage.SPARQL,
                constructQuery).evaluate();

        while (graphResult.hasNext()) {

            graphResult.next();
            System.out.println("graphe   :"  +graphResult.next());

            con.add(graphResult.next().getSubject(), graphResult.next().getPredicate(), graphResult.next().getObject());

        }

    }
    
    
    
    
}
