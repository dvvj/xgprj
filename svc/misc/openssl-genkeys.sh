openssl req -new -newkey rsa:4096 -days 3650 -nodes -x509 -subj \
    "/C=CN/ST=AH/L=HEFEI/O=XG/CN=xg.org" -keyout \
    ./ssl.key -out ./ssl.crt
