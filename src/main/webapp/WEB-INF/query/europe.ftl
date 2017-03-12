PREFIX cdm:<http://publications.europa.eu/ontology/cdm#> 
PREFIX skos:<http://www.w3.org/2004/02/skos/core#> 
PREFIX dc:<http://purl.org/dc/elements/1.1/> 
PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> 
PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX owl:<http://www.w3.org/2002/07/owl#> 

SELECT DISTINCT  (group_concat(distinct ?work;separator=",") as ?URI)  
   (group_concat(distinct ?title_;separator=",") as ?title)  
  (group_concat(distinct ?agentName;separator=",") as ?authors)  
  ?date 

WHERE  
{ 
?work cdm:work_date_document ?d. 
?exp cdm:expression_belongs_to_work ?work . 
?exp cdm:expression_title ?title_ . 
OPTIONAL {?work cdm:work_created_by_agent/skos:prefLabel ?agentName . 
filter (lang(?agentName)="en")}. 
?work cdm:work_date_document ?date . 
 ?exp cdm:expression_uses_language <http://publications.europa.eu/resource/authority/language/ENG>. 
FILTER (regex( str(?title_), "${keyword}", "i")). 
} 
GROUP BY ?work ?date
