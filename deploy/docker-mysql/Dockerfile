# Derived from official mysql image (our base image)
FROM mysql
# Add a database
ENV MYSQL_DATABASE xgproj
# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY ./scripts/ /docker-entrypoint-initdb.d/
COPY mysqld_charset.cnf /etc/mysql/conf.d/mysqld_charset.cnf
