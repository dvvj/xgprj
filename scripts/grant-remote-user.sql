CREATE USER dbuser@'%' IDENTIFIED BY 'dbpass';
GRANT ALL ON xgproj.* TO dbuser@'%';
