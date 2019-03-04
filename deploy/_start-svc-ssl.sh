docker run -it \
  -p 8443:8443 \
  --name svc_i1 \
  -v $(pwd)/tomcat/svc.war:/usr/local/tomcat/webapps/svc.war \
  -v $(pwd)/tomcat/svc:/usr/local/tomcat/webapps/ROOT \
  -v $(pwd)/ssl/ssl.crt:/usr/local/tomcat/conf/ssl.crt \
  -v $(pwd)/ssl/ssl.key:/usr/local/tomcat/conf/ssl.key \
  -v $(pwd)/ssl/server.xml:/usr/local/tomcat/conf/server.xml \
  -v $(pwd)/svc_cfg.json:/usr/local/tomcat/webapps/svc/svc_cfg.json \
  -v $(pwd)/hibernate.cfg.xml:/usr/local/tomcat/webapps/svc/hibernate.cfg.xml \
  -v $1:/appdata/product_assets \
  -v $2:/usr/local/tomcat/logs \
  tomcat
