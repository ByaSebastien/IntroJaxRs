# IntroJaxRs - Guide Pédagogique complet

## 📚 Table des matières

1. [Introduction à JAX-RS](#introduction-à-jax-rs)
2. [Architecture du projet](#architecture-du-projet)
3. [Flux général de l'application](#flux-général-de-lapplication)
4. [Les concepts clés](#les-concepts-clés)
5. [Guide pratique des endpoints](#guide-pratique-des-endpoints)
6. [Système d'authentification et d'autorisation](#système-dauthentication-et-dautorisation)
7. [Gestion des commandes](#gestion-des-commandes)
8. [Installation et exécution](#installation-et-exécution)

---

## 🎯 Introduction à JAX-RS

### Qu'est-ce que JAX-RS ?

**JAX-RS** (Java API for RESTful Web Services) est une spécification Java qui permet de créer facilement des **API REST**. Elle fournit des annotations pour transformer une classe Java en ressource web accessible via HTTP.

### Concept-clé : REST

REST signifie **Representational State Transfer**. C'est un style architectural basé sur :

| Concept | Description |
|---------|-------------|
| **Ressources** | Les données gérées par votre API (produits, commandes, utilisateurs) |
| **URLs** | Identifient les ressources (`/api/product`, `/api/order/123`) |
| **Méthodes HTTP** | Opérations sur les ressources (GET, POST, PATCH, DELETE) |
| **Représentation** | Format des données échangées (JSON, XML) |

### Analogie simple

```
REST est comme un restaurant :
- GET    = demander le menu
- POST   = commander un plat
- PATCH  = modifier votre commande
- DELETE = annuler votre commande
```

---

## 🏗️ Architecture du projet

### Structure en couches

```
┌─────────────────────────────────────────────┐
│   PRESENTATION LAYER (API REST)             │
│   Resources (Endpoints JAX-RS)              │
├─────────────────────────────────────────────┤
│   SERVICE LAYER (Logique métier)            │
│   Services d'authentification et commandes  │
├─────────────────────────────────────────────┤
│   PERSISTENCE LAYER (Accès aux données)     │
│   DAOs et Entités JPA Hibernate             │
├─────────────────────────────────────────────┤
│   DATABASE                                  │
│   Données persistantes                      │
└─────────────────────────────────────────────┘
```

### Composants principaux

```mermaid
graph TB
    A["📱 Client HTTP<br/>(Navigateur/Postman)"]
    B["🔐 Filters de Sécurité<br/>(AuthFilter, IsAuthenticatedFilter)"]
    C["🎯 Ressources JAX-RS<br/>(AuthResource, ProductResource, OrderResource)"]
    D["⚙️ Services Métier<br/>(AuthService, OrderService)"]
    E["💾 DAOs<br/>(ProductDao, OrderDao, etc.)"]
    F["📊 Entités JPA<br/>(Product, Order, User, etc.)"]
    G["🗄️ Base de Données<br/>(Hibernate ORM)"]
    
    A -->|HTTP Request| B
    B -->|valide| C
    C -->|utilise| D
    D -->|utilise| E
    E -->|manipule| F
    F -->|persiste| G
    G -->|retourne| F
    F -->|remonte| E
    E -->|remonte| D
    D -->|remonte| C
    C -->|HTTP Response| A
```

---

## 🔄 Flux général de l'application

### 1. Cycle de vie d'une requête HTTP

```mermaid
sequenceDiagram
    participant Client as 📱 Client
    participant Filter as 🔐 Filtre de Sécurité
    participant Resource as 🎯 Ressource JAX-RS
    participant Service as ⚙️ Service
    participant DAO as 💾 DAO
    participant DB as 🗄️ Base de Données

    Client->>Filter: 1. Envoie requête HTTP
    Filter->>Filter: 2. Vérifie le JWT
    Filter->>Filter: 3. Crée SecurityContext
    Filter->>Resource: 4. Appelle la méthode
    Resource->>Resource: 5. Valide les données
    Resource->>Service: 6. Appelle la logique métier
    Service->>DAO: 7. Demande les données
    DAO->>DB: 8. Requête SQL
    DB-->>DAO: 9. Retourne les données
    DAO-->>Service: 10. Traite les données
    Service-->>Resource: 11. Retourne le résultat
    Resource-->>Client: 12. Envoie réponse HTTP JSON
```

### 2. Flux d'authentification

```mermaid
graph LR
    A["📝 Client POST /api/auth/login<br/>{ login: user, password: Test1234= }"]
    B["🔍 AuthService.login()<br/>1. Cherche l'utilisateur<br/>2. Vérifie le password BCrypt"]
    C["✅ Authentification OK?"]
    D["🎫 JwtUtils.generateToken()<br/>Crée un JWT signé<br/>Valide 15 minutes"]
    E["📤 Client reçoit Token JWT<br/>{ token: eyJhbGc... }"]
    F["❌ Erreur 400"]
    
    A --> B
    B --> C
    C -->|OUI| D
    C -->|NON| F
    D --> E
```

### 3. Flux d'utilisation des tokens

```mermaid
graph LR
    A["🔐 Client GET /api/product<br/>Header: Authorization: Bearer eyJhbGc..."]
    B["🛡️ Filtre AuthFilter<br/>1. Extrait le token<br/>2. Valide la signature<br/>3. Crée SecurityContext"]
    C["✅ Token valide?"]
    D["✔️ Ressource accessible"]
    E["❌ Erreur 403"]
    F["📊 Récupère les produits"]
    
    A --> B
    B --> C
    C -->|OUI| D
    C -->|NON| E
    D --> F
```

### 4. Flux complet d'une commande

```mermaid
sequenceDiagram
    participant Client as 👤 Client (USER)
    participant Post as POST /api/order
    participant Validate as PATCH /api/order/validate/{id}
    participant Service as OrderService
    participant Stock as Database Stock

    Client->>Post: 1. Crée une commande PENDING
    rect rgb(200, 220, 255)
        Post->>Service: Valide les produits existants
        Post->>Service: Vérifie le stock disponible
    end
    Post-->>Client: ✅ 201 CREATED
    
    Note over Client: Quelques heures plus tard...
    
    Client->>Validate: 2. Valide la commande (rôle MANDAY)
    rect rgb(220, 200, 255)
        Validate->>Service: Traite items complétés/incomplets
        Validate->>Stock: Déduit le stock pour items complétés
        Validate->>Service: Crée commande réapprovisionnement si nécessaire
    end
    Validate-->>Client: ✅ 202 ACCEPTED
```

---

## 💡 Les concepts clés

### Annotations JAX-RS principales

#### 1. `@Path` - Définit l'URL de la ressource

```java
@Path("/product")  // Cette classe sera accessible à /api/product
public class ProductResource {
    
    @Path("{id}")   // Paramètre d'URL : /api/product/{id}
    public Response getById(@PathParam("id") UUID id) { }
}
```

#### 2. `@GET`, `@POST`, `@PATCH`, `@DELETE` - Méthodes HTTP

```java
@GET                    // Lire des données (sans effet secondaire)
@POST                   // Créer une nouvelle ressource
@PATCH                  // Modifier une ressource existante
@DELETE                 // Supprimer une ressource
@PUT                    // Remplacer entièrement une ressource
```

#### 3. `@Produces` et `@Consumes` - Format des données

```java
@Produces(MediaType.APPLICATION_JSON)   // La réponse sera en JSON
@Consumes(MediaType.APPLICATION_JSON)   // La requête doit être en JSON
public Response create(@Valid OrderRequest request) { }
```

#### 4. `@PathParam`, `@QueryParam`, `@HeaderParam` - Paramètres

```java
@GET
@Path("/{id}")
public Response getById(
    @PathParam("id") UUID id,              // Dans l'URL : /product/123
    @QueryParam("category") String cat,    // Dans la query : ?category=electronics
    @HeaderParam("Authorization") String token  // Dans les headers
) { }
```

### Cycle de vie d'une annotation personnalisée

```mermaid
graph TB
    A["@IsAuthenticated<br/>(Annotation personnalisée)"]
    B["IsAuthenticatedFilter<br/>(Lit l'annotation)"]
    C["Vérifie le JWT<br/>en Authorization Header"]
    D["Token valide?"]
    E["Crée SecurityContext<br/>avec utilisateur"]
    F["Permet accès à la ressource"]
    G["Lance ForbiddenException"]
    
    A --> B
    B --> C
    C --> D
    D -->|OUI| E
    E --> F
    D -->|NON| G
```

---

## 🔌 Guide pratique des endpoints

### 🔓 Endpoint Authentification (Public)

#### Enregistrement

```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "john@example.com",
  "username": "john",
  "password": "SecurePass123="
}
```

**Réponse 200 OK**
```json
{}
```

#### Connexion

```http
POST /api/auth/login
Content-Type: application/json

{
  "login": "john",           // email ou username
  "password": "SecurePass123="
}
```

**Réponse 200 OK**
```json
{
  "user": {
    "id": 1,
    "username": "john",
    "email": "john@example.com",
    "roles": ["USER"]
  },
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIi4uLiJ9.signature"
}
```

### 📦 Endpoint Produits (Public)

#### Lister les produits

```http
GET /api/product
```

**Réponse 200 OK**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "iPhone 15 Pro",
    "brand": "Apple",
    "price": 129999,
    "category": "Électronique"
  },
  ...
]
```

#### Détails d'un produit

```http
GET /api/product/{id}
```

**Réponse 200 OK**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "iPhone 15 Pro",
  "description": "Smartphone haut de gamme Apple",
  "brand": "Apple",
  "price": 129999,
  "category": "Électronique",
  "stock": {
    "quantity": 45,
    "threshold": 10,
    "orderQuantity": 50
  }
}
```

### 🛍️ Endpoint Commandes (Sécurisé)

#### Crée une commande (Rôle: USER)

```http
POST /api/order
Authorization: Bearer {token}
Content-Type: application/json

{
  "orderLines": [
    {
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "quantity": 2
    },
    {
      "productId": "660e8400-e29b-41d4-a716-446655440111",
      "quantity": 1
    }
  ]
}
```

**Réponse 201 CREATED**

#### Valide une commande (Rôle: MANDAY)

```http
PATCH /api/order/validate/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "complete": {
    "orderLines": [
      {
        "productId": "550e8400-e29b-41d4-a716-446655440000",
        "quantity": 1
      }
    ]
  },
  "incomplete": {
    "orderLines": [
      {
        "productId": "660e8400-e29b-41d4-a716-446655440111",
        "quantity": 1
      }
    ]
  }
}
```

**Réponse 202 ACCEPTED**

---

## 🔐 Système d'authentification et d'autorisation

### Architecture de sécurité

```mermaid
graph TB
    A["Utilisateur"]
    B["POST /auth/login"]
    C["JWT généré<br/>15 minutes"]
    D["Bearer Token dans<br/>Authorization Header"]
    E["Filtre JAX-RS<br/>intercepte requête"]
    F["Extrait & Vérifie JWT"]
    G["Crée SecurityContext"]
    H["@IsAuthenticated<br/>vérifie présence"]
    I["@HasAuthority<br/>vérifie les rôles"]
    
    A -->|1. Envoie credentials| B
    B -->|2. Crée| C
    C -->|3. Retourne| A
    A -->|4. Stocke & envoie| D
    D -->|5. Chaque requête| E
    E -->|6. Valide| F
    F -->|7. Ajoute au contexte| G
    G -->|8. Annotations| H
    H -->|9. Annotations| I
```

### Types d'authentification

| Annotation | Rôle | Exemple d'utilisation |
|-----------|------|----------------------|
| `@IsAnonymous` | Aucune authentification requise | `/auth/login`, `/auth/register` |
| `@IsAuthenticated` | Utilisateur doit être connecté | `/hello` |
| `@HasAuthority(roles="USER")` | Utilisateur doit avoir un rôle spécifique | Créer une commande |
| `@HasAuthority(roles="MANDAY")` | Responsable de validation | Valider une commande |

### Cycle de traitement du JWT

```mermaid
sequenceDiagram
    participant Client as Client
    participant Filter as AuthFilter
    participant JwtUtils as JwtUtils
    participant SecurityContext as SecurityContext
    participant Resource as Ressource

    Client->>Filter: Envoie requête<br/>Authorization: Bearer token
    Filter->>Filter: Extrait le token
    Filter->>JwtUtils: Appelle isValid(token)
    JwtUtils->>JwtUtils: Vérifie la signature
    JwtUtils->>JwtUtils: Vérifie l'expiration
    JwtUtils-->>Filter: ✅ Token valide
    Filter->>JwtUtils: Appelle getUser(token)
    JwtUtils-->>Filter: UserResponse (id, roles)
    Filter->>SecurityContext: setPrincipal(userResponse)
    Filter->>Resource: Appelle la méthode
    Resource->>SecurityContext: getFakeUserPrincipal()
    Resource-->>Client: Réponse avec données utilisateur
```

---

## 🛒 Gestion des commandes

### États possibles d'une commande

```mermaid
graph LR
    A["PENDING<br/>(En attente)"]
    B["SEND<br/>(Envoyée)"]
    C["CANCELED<br/>(Annulée)"]
    D["WAITING<br/>(En attente approvision.)"]
    
    A -->|Complètement<br/>disponible| B
    A -->|Rejet par MANDAY| C
    A -->|Partiellement<br/>disponible| D
    D -->|Stock reçu| B
```

### Cas d'utilisation : Commande partielle

```mermaid
graph LR
    A["Commande originale<br/>PENDING"]
    B["Client commande:<br/>- 2x Produit A (OK)<br/>- 3x Produit B (OUT OF STOCK)"]
    C["MANDAY valide"]
    D["Commande complète<br/>SEND<br/>2x Produit A"]
    E["Commande incomplète<br/>WAITING<br/>3x Produit B"]
    F["Commande réapprov.<br/>WAITING<br/>automatique pour<br/>le gestionnaire"]
    
    A --> B
    B --> C
    C --> D
    C --> E
    C --> F
```

### Flux complet du stock management

```mermaid
sequenceDiagram
    participant Stock as Stock Actuel<br/>100 unités
    participant Order as Commande<br/>50 unités
    participant Threshold as Seuil<br/>10 unités
    participant ReOrder as Qty à commander<br/>50 unités

    Stock->>Order: Déduit 50
    Note right of Stock: Stock = 50
    alt Stock > Seuil
        Stock->>Stock: ✅ Pas d'alerte
    else Stock ≤ Seuil
        Stock->>Threshold: ⚠️ Alerte!
        Threshold->>ReOrder: Crée auto commande
        Note right of ReOrder: Commande auto<br/>pour 50 unités
    end
```

---

## 🚀 Installation et exécution

### Prérequis

```bash
Java 17+
Maven 3.8+
```

### Configuration de la base de données

Le projet utilise Hibernate avec une base de données H2 (en mémoire par défaut).

Configuration dans `persistence.xml` :

```xml
<persistence-unit name="IntroJaxRs">
    <properties>
        <property name="jakarta.persistence.jdbc.url" 
                  value="jdbc:h2:mem:testdb"/>
        <property name="jakarta.persistence.jdbc.driver" 
                  value="org.h2.Driver"/>
        <property name="hibernate.hbm2ddl.auto" value="create"/>
    </properties>
</persistence-unit>
```

### Build et exécution

```bash
# Compiler le projet
mvn clean compile

# Packager en WAR
mvn package

# Déployer (sur Tomcat/GlassFish)
mvn tomcat:run
# ou
mvn cargo:run
```

### Accédez à l'API

```
http://localhost:8080/IntroJaxRs/api

OpenAPI/Swagger UI:
http://localhost:8080/IntroJaxRs/swagger-ui/index.html
```

---

## 📝 Utilisateurs de test

```
Utilisateurs pré-créés au démarrage:

USER:
  Email: user@test.be
  Username: user
  Password: Test1234=
  Rôles: USER

ADMIN:
  Email: admin@test.be
  Username: admin
  Password: Test1234=
  Rôles: ADMIN
```

### Scénario de test complet

#### 1. Enregistrement

```bash
curl -X POST http://localhost:8080/IntroJaxRs/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "username": "newuser",
    "password": "SecurePass123="
  }'
```

#### 2. Connexion

```bash
curl -X POST http://localhost:8080/IntroJaxRs/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "user@test.be",
    "password": "Test1234="
  }'
```

Copier le `token` de la réponse.

#### 3. Lister les produits

```bash
curl http://localhost:8080/IntroJaxRs/api/product \
  -H "Accept: application/json"
```

#### 4. Créer une commande (nécessite token)

```bash
curl -X POST http://localhost:8080/IntroJaxRs/api/order \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "orderLines": [
      {
        "productId": "550e8400-e29b-41d4-a716-446655440000",
        "quantity": 2
      }
    ]
  }'
```

---

## 📚 Ressources pour approfondir

### Concepts JAX-RS

- **@ApplicationPath** : Définit le chemin racine de toutes les ressources
- **Response Builder Pattern** : Construit les réponses HTTP (`Response.ok().entity(...).build()`)
- **Content Negotiation** : Négociation de format (JSON/XML) basée sur les headers Accept

### Technologies utilisées

```
JAX-RS (Jakarta REST 4.0.0)
├── Jersey (implémentation)
├── Hibernat (ORM)
├── Jakarta Persistence (JPA)
├── JWT (JSON Web Token)
├── BCrypt (Hachage des mots de passe)
└── Validation (Jakarta Validation API)
```

### Architecture REST

```
Niveaux de maturité de Richardson:
  Niveau 0: RPC sur HTTP
  Niveau 1: Utilisation des ressources (URIs)
  Niveau 2: Utilisation des verbes HTTP (GET, POST, etc.)
  Niveau 3: Utilisation des hypermédias (HATEOAS)
  
Ce projet: Niveau 2-3
```

---

## 🎓 Points clés à retenir

✅ **REST = Ressources + URLs + Verbes HTTP + Représentation**

✅ **JAX-RS transforme les classes Java en ressources web**

✅ **Les annotations déclarent comment les URLs mappent aux méthodes**

✅ **L'authentification par JWT est stateless et scalable**

✅ **Les filtres interceptent toutes les requêtes pour la sécurité**

✅ **Les services métier contiennent la logique, les DAOs gèrent les données**

✅ **Hibernate traduit les objets Java en requêtes SQL via JPA**

---

## 📞 Structure des fichiers

```
src/main/java/be/bstorm/introjaxrs/
├── HelloApplication.java          # Configuration JAX-RS
├── annotations/security/          # Annotations personnalisées
├── config/                        # Configuration (Validators, Security)
├── daos/                          # Data Access Objects
├── enums/                         # OrderStatus, MovementType
├── filters/                       # Filtres de sécurité
├── models/                        # DTOs (Data Transfer Objects)
├── pojos/                         # Entités JPA
├── resources/                     # Ressources JAX-RS (endpoints)
├── services/                      # Logique métier
└── utils/                         # Utilitaires (JWT, DataInitializer)
```

---

**Créé le**: 29 Avril 2026  
**Pour**: Apprentissage de JAX-RS et du développement d'APIs REST  
**Niveau**: Débutant à Intermédiaire

