#test query

SELECT 
distinct ?title ?authors
WHERE { 
      [] a bibo:Article ;
       bibo:authorList ?authors ;
       dct:title ?title .
} 
GROUP BY ?title ?authors ?date ?abstract 
LIMIT 10


#

PREFIX bibo: <http://purl.org/ontology/bibo/>  
SELECT * WHERE {
	?a rdf:type bibo:Article .
}
LIMIT 100
		
