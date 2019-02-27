docker run -d -p 3306:3306 --name xgsql_i1 \
  -e MYSQL_ROOT_PASSWORD=cPEaKeXnzq8fuRBD87csHdaL \
  -e MYSQL_USER=dbuser \
  -e MYSQL_PASSWORD=dbpass \
  -v $1:/app-backup \
  xgsql
