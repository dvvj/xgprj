cd ./docker-mysql/
sh ./start.sh $PWD/../backup
cd ..
rm -rf tomcat
mkdir ./tomcat
cp ../svc/target/svc.war ./tomcat
cp -r ../svc/target/svc ./tomcat/svc
cp ./svc_cfg.json ./tomcat/svc/WEB-INF/
cp ./hibernate.cfg.xml ./tomcat/svc/WEB-INF/
sh ./_start-svc-ssl.sh $PWD/../prod_assets /tmp/tomcat-logs
