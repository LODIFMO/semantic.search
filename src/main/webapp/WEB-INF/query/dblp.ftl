SELECT DISTINCT ?title ?date ?author ?work WHERE {
 [] a swrc:Article;
 dc:title ?title;
 dcterms:issued ?date;
dcterms:bibliographicCitation ?work;
 dc:creator ?author.
FILTER (regex( str(?title), "data mining", "i")).
}
GROUP BY ?title ?date ?author ?work


//Query below doesn't work because of group_concat :-(

SELECT DISTINCT (group_concat(distinct ?work;separator=",") as ?cellarURIs) 
   (group_concat(distinct ?title_;separator=",") as ?title) 
  (group_concat(distinct ?creator;separator=",") as ?authors) 
  ?date WHERE {
 [] a swrc:Article;
 dc:title ?title_;
 dcterms:issued ?date;
dcterms:bibliographicCitation ?work;
 dc:creator ?creator.
FILTER (regex( str(?title), "data mining", "i")).
}