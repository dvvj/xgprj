cd ../docker-mysql/
sh ./start.sh /home/devvj/db-backup
cd ../svc/
sh ./start-svc-ssl.sh /home/devvj/prod_assets
