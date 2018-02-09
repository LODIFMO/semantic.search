FROM tomcat

MAINTAINER ovgerasin@gmail.com

RUN echo "org.apache.el.parser.SKIP_IDENTIFIER_CHECK=true" >> $CATALINA_HOME/conf/catalina.properties

COPY semantic_search.war $CATALINA_HOME/webapps/

CMD ["catalina.sh", "run"]