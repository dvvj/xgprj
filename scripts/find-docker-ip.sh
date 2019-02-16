docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' docker-container-id
