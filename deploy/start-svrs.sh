cd ./docker-mysql/
sh ./start.sh /home/devvj/db-backup
cd ..
rm -rf tomcat
mkdir ./tomcat
cp ../svc/target/svc.war ./tomcat
cp -r ../svc/target/svc ./tomcat/svc
sh ./_start-svc-ssl.sh /home/devvj/prod_assets /home/devvj/tomcat-logs
