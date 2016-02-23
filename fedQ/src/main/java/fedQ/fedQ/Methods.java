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
    
    
    
    
    
    
}
