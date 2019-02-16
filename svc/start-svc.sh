docker run -it \
  -p 8080:8080 \
  -v $(pwd)/target/svc.war:/usr/local/tomcat/webapps/svc.war \
  -v $(pwd)/target/svc:/usr/local/tomcat/webapps/ROOT \
  tomcat
