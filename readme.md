# Carte aux trésors
## Description du projet
Ce projet est une proposition d'implémentation de l'exercice pratique "La carte aux trésors" donnée par X.

## Technologies
- Spring Boot 3.0.4
- Java 17
- Maven 3
- Junit 5

## Lancement de l'application
- Cloner le repo.
- Packager le jar via maven et la commande `mvn clean package`.
- Se placer dans le dossier contenant le jar et lancer l'application via la commande 
`java -jar carte-aux-tresors-0.0.1-SNAPSHOT.jar`
- L'application est lancée.

## Lancement d'une chasse aux trésors
### Requête
- Effectuer une requête sur le endpoint suivant :
  - URL : `/start`
  - Méthode : `POST`
  - Body : Nom du fichier d'entrée (format .txt) à utiliser. Le nom du fichier doit comprendre l'extension 
  (par exemple `test.txt`).  
  Le fichier d'entrée doit être placé à l'emplacement du jar (`user working directory` a.k.a. `user.dir`).

### Réponse
- `200 OK` si la chasse aux trésors s'est bien terminée.  
Le fichier de sortie est placé à l'emplacement du jar (`user working directory` a.k.a. `user.dir`).  
Le fichier de sortie porte le même nom que le fichier d'entrée avec un préfixe "output-". Par exemple `output-test.txt`.
- `500 INTERNAL_SERVER_ERROR` si une erreur est survenue pendant la chasse aux trésors. Se référer au message retourné.
