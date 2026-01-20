# TheBucket

Application Spring Boot dédiée à la gestion de requêtes de génération avec support de stockage d'objets MinIO et base de données PostgreSQL.

## Structure du Projet

```
TheBucket/
├── bucket/                    # Application Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/expoai/bucket/
│   │   │   │   ├── controller/      # Contrôleurs REST
│   │   │   │   ├── service/         # Logique métier
│   │   │   │   ├── repository/      # Couche d'accès aux données
│   │   │   │   ├── entity/          # Entités JPA
│   │   │   │   ├── dto/             # Objets de transfert de données
│   │   │   │   ├── mapper/          # Mappers entité ↔ DTO
│   │   │   │   └── config/          # Configuration Spring
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/migration/    # Scripts Flyway
│   │   └── pom.xml
├── SpringBucketTest/          # Collection Bruno pour tests API
├── docker-compose.yml         # Orchestration Docker
├── nginx/                     # Configuration proxy inversé
├── .env.sample               # Variables d'environnement exemple
└── README.md
```

## Démarrage Rapide

### Prérequis

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Bruno (pour tester les API)

### Configuration

1. **Copier le fichier d'environnement**
   ```bash
   cp .env.sample .env
   ```

2. **Ajuster les variables d'environnement** (optionnel)
   ```bash
   # Voir .env pour la configuration complète
   ```

3. **Démarrer les services Docker**
   ```bash
   docker-compose up -d postgres minio
   ```

4. **Lancer l'application Spring**
   ```bash
   cd bucket
   mvn spring-boot:run
   ```

L'application sera accessible sur `http://localhost:8081`

## Fonctionnalités Principales

### GenerationRequest API

API complète pour gérer les requêtes de génération avec plusieurs exemples d'implémentation à des fins pédagogiques.

#### Endpoints Disponibles

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| POST | `/api/generation-request` | Créer une requête | `@PreAuthorize("hasRole('ADMIN')")` |
| GET | `/api/generation-request` | Liste avec pagination offset | `@Secured("ROLE_ADMIN")` |
| GET | `/api/generation-request/cursor` | Liste avec pagination curseur | `@RolesAllowed("ADMIN")` |
| GET | `/api/generation-request/{id}` | Obtenir par ID | `@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")` |
| GET | `/api/generation-request/search` | Recherche multi-critères | `@PreAuthorize("hasAnyRole('ADMIN', 'USER')")` |
| PATCH | `/api/generation-request/{id}/status` | Mettre à jour le statut | `@PreAuthorize("hasAnyRole('ADMIN', 'API')")` |
| DELETE | `/api/generation-request/{id}` | Supprimer une requête | `@Secured({"ROLE_ADMIN"})` |

#### Exemples de Pagination

**Pagination Offset (traditionnelle)**
```http
GET /api/generation-request?page=0&size=10&sortBy=createdAt&direction=DESC
```

**Pagination Curseur (optimisée pour données temps réel)**
```http
GET /api/generation-request/cursor?cursor=10&size=10
```

#### Recherche Avancée

Recherche multi-critères avec correspondance partielle :

```http
GET /api/generation-request/search?descriptionKeyword=mountain&status=COMPLETED&userId=1&page=0&size=10
```

**Critères supportés :**
- `promptKeyword` - Recherche partielle insensible à la casse dans le prompt
- `descriptionKeyword` - Recherche partielle dans la description
- `status` - Correspondance exacte (PENDING, PROCESSING, COMPLETED, FAILED)
- `userId` - Filtre par utilisateur
- `createdAfter` / `createdBefore` - Plage de dates (ISO 8601)

## Sécurité

L'application démontre plusieurs approches pour la sécurité des méthodes :

1. **@PreAuthorize** - Le plus flexible, supporte les expressions SpEL
2. **@Secured** - Simple, spécifique à Spring (requiert le préfixe "ROLE_")
3. **@RolesAllowed** - Standard JSR-250, portable

### Authentification

- JWT pour l'authentification utilisateur
- API Keys pour l'authentification service-to-service
- Bearer token requis pour tous les endpoints protégés

## Base de Données

### Migrations Flyway

Les migrations se trouvent dans `src/main/resources/db/migration/` :

- `V1__Initial_Schema.sql` - Tables users, roles, auth
- `V2__Api_Tokens.sql` - Tokens API
- `V3__Image_Storage.sql` - Stockage d'images
- `V4__Generation_Request.sql` - Table generation_request
- `V5__Test_Data_Generation_Request.sql` - 40 enregistrements de test

### Entités Principales

- **User** - Utilisateurs de l'application
- **Role** - Rôles (ADMIN, USER, API, SCRAPPER)
- **GenerationRequest** - Requêtes de génération avec statuts et timestamps
- **ApiToken** - Tokens pour authentification API

## Tests avec Bruno

Les tests API se trouvent dans `SpringBucketTest/GenerationRequest/` :

1. **Create Request** - Créer une nouvelle requête
2. **Get All - Offset Pagination** - Pagination traditionnelle
3. **Get All - Cursor Pagination (Auto)** - Pagination curseur avec sauvegarde automatique du nextCursor
4. **Get By ID** - Récupérer une requête spécifique
5. **Advanced Search** - Recherche multi-critères
6. **Update Status** - Modifier le statut
7. **Delete Request** - Supprimer une requête

### Configuration Bruno

Les variables d'environnement sont définies dans les environnements Local/Prod :
- `{{connectionPath}}` - URL de base de l'API
- `{{adminToken}}` - Token JWT pour authentification
- `{{apiToken}}` - Token API pour services
- `{{nextCursor}}` - Curseur pour pagination (auto-géré)

## Architecture Technique

### Patterns Implémentés

- **Repository Pattern** - Accès aux données avec Spring Data JPA
- **Specification Pattern** - Requêtes dynamiques multi-critères
- **DTO Pattern** - Séparation entités/transfert de données
- **Mapper Pattern** - Conversion entité ↔ DTO
- **Builder Pattern** - Construction d'objets immutables

### Stack Technique

- **Spring Boot 3.4.5** - Framework principal
- **Spring Security** - Authentification et autorisation
- **Spring Data JPA** - Persistance
- **Hibernate** - ORM
- **Flyway** - Migrations de base de données
- **PostgreSQL 17** - Base de données
- **MinIO** - Stockage d'objets S3-compatible
- **Lombok** - Réduction du boilerplate
- **JWT** - Tokens d'authentification

## Concepts Pédagogiques Démontrés

Ce projet sert d'exemple pour l'enseignement de :

1. **Pagination**
   - Offset-based (simple mais peut avoir du drift)
   - Cursor-based (performante pour données temps réel)

2. **Recherche Avancée**
   - JPA Specifications pour requêtes dynamiques
   - Correspondance partielle avec LIKE
   - Combinaison de critères avec AND

3. **Sécurité des Méthodes**
   - Trois types d'annotations comparées
   - Expressions SpEL pour logique complexe
   - Rôles multiples et conditions OR/AND

4. **Architecture en Couches**
   - Controller → Service → Repository
   - DTOs pour isolation
   - Mappers pour conversion

5. **Migrations de Base de Données**
   - Versioning avec Flyway
   - Scripts SQL réutilisables
   - Données de test

## Commandes Utiles

```bash
# Démarrer uniquement les dépendances
docker-compose up -d postgres minio

# Voir les logs
docker-compose logs -f postgres
docker-compose logs -f minio

# Arrêter et supprimer les volumes
docker-compose down -v

# Reconstruire l'application
cd bucket && mvn clean install

# Accéder à MinIO UI
# http://localhost:9090 (minioadmin / minioadmin)

# Accéder à la base de données
docker exec -it postgres psql -U myuser -d media
```

## Variables d'Environnement

Voir `.env.sample` pour la liste complète. Principales variables :

- `SPRING_PORT` - Port de l'application (défaut: 8081)
- `POSTGRES_*` - Configuration PostgreSQL
- `MINIO_*` - Configuration MinIO
- `JWT_SECRET` - Secret pour signature JWT
- `CORS_ALLOWED_ORIGINS` - Origines autorisées

## Contribution

Ce projet est utilisé à des fins pédagogiques. Les contributions sont les bienvenues pour :
- Ajouter de nouveaux exemples de patterns
- Améliorer la documentation
- Corriger les bugs
- Optimiser les performances

## Licence

Projet éducatif - ExpoAI
