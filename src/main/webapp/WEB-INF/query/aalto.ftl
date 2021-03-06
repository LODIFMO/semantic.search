prefix xsd: <http://www.w3.org/2001/XMLSchema#> 
prefix dct: <http://purl.org/dc/terms/> 
prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix skos: <http://www.w3.org/2004/02/skos/core#> 
prefix bibo: <http://purl.org/ontology/bibo/> 
prefix foaf: <http://xmlns.com/foaf/0.1/> 
prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> 
prefix aiiso:<http://purl.org/vocab/aiiso/schema#>
prefix teach:<http://linkedscience.org/teach/ns#>  
  
SELECT distinct ?Title ?Authors ?Date
        WHERE { 
               [] a bibo:Article ;
               bibo:authorList ?Authors ;
               dct:title ?Title ;
               dct:date ?Date .
                FILTER (regex( str(?Title), "${keyword}", "i")).
        }
GROUP BY ?Title ?Authors ?Date