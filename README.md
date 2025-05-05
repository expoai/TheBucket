"# TheBucket" 

Project Structure

the-bucket/
├── spring-app/            # Control les flux routes REST
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── docker-compose.yml     # Deploy spring + minio + postgres
├── nginx/                 # Control le proxy inversé
│   └── default.conf
├── postgres-data/         # Docker volume (ignored by Git)
├── minio-data/            # Docker volume (ignored by Git)
├── .gitignore
└── README.md


Commands
docker-compose down -v
docker-compose up postgres
