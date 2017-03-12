PREFIX bibo: <http://purl.org/ontology/bibo/>  
PREFIX foaf: <http://xmlns.com/foaf/0.1/>  
PREFIX dcterms: <http://purl.org/dc/terms/>  
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 

SELECT 
distinct (group_concat(distinct ?URI_) as ?URI) ?title ?authors ?date ?abstract 
FROM <http://data.open.ac.uk/context/oro> 
WHERE { 
      [] a bibo:Article ;
       bibo:uri ?URI_ ;
       bibo:authorList ?authors ;
       bibo:abstract ?abstract ;
       dcterms:title ?title ;
       dcterms:date ?date .
FILTER (regex( str(?title), "${keyword}", "i")). 
} 
GROUP BY ?title ?authors ?date ?abstract 
LIMIT 10
