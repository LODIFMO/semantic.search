PREFIX pubblicazioni:   <http://www.cnr.it/ontology/cnr/pubblicazioni.owl#>

SELECT distinct ?Title ?Authors
        WHERE { 
               [] a pubblicazioni:Pubblicazione ;
               pubblicazioni:autore ?Authors ;
               pubblicazioni:titolo ?Title .
        }
GROUP BY ?Title ?Authors
LIMIT 10


try it: http://data.cnr.it/sparql/

No data, sad..