docker run -it \
  -p 8443:8443 \
  -v $(pwd)/target/svc.war:/usr/local/tomcat/webapps/svc.war \
  -v $(pwd)/target/svc:/usr/local/tomcat/webapps/ROOT \
  -v $(pwd)/ssl/ssl.crt:/usr/local/tomcat/conf/ssl.crt \
  -v $(pwd)/ssl/ssl.key:/usr/local/tomcat/conf/ssl.key \
  -v $(pwd)/ssl/server.xml:/usr/local/tomcat/conf/server.xml \
  tomcat
