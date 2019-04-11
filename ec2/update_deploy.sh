cd ..
git pull
cd ec2
sh cp_ssl.sh
cd ../deploy/docker-mysql/
sh docker-build.sh
cd ../..
mvn clean package -pl svc --am
