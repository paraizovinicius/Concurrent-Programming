# Quicksort - Tri Rapide en Java

Ce programme implémente l'algorithme de tri rapide (Quicksort) en Java en utilisant la méthode de tri parallèle à l'aide de ForkJoinPool.

## Description

Le tri rapide est un algorithme de tri très efficace qui utilise une stratégie de diviser pour régner pour trier un tableau donné. Cette implémentation spécifique utilise la méthode de tri parallèle pour exploiter le potentiel de traitement multi-thread.

## Fonctions

### `afficher(int[] t, int debut, int fin)`

Cette fonction affiche une partie du tableau spécifié, de l'index `debut` à l'index `fin`.

### `EchangerElements(int[] tableau, int i, int j)`

Cette fonction échange les éléments aux positions `i` et `j` dans le tableau.

### `partitionner(int[] t, int debut, int fin)`

Cette fonction effectue une partition du tableau en utilisant le dernier élément comme pivot, puis renvoie la position du pivot après partitionnement.

### `trierRapidement(int[] t, int debut, int fin)`

Cette fonction trie récursivement une partie du tableau en utilisant l'algorithme de tri rapide (Quicksort).

### `parallelQuicksort(int[] tableau, int low, int high)`

Cette fonction initialise et gère le tri parallèle en utilisant la classe `ForkJoinPool` pour diviser le travail entre différents threads.

### `PQuicksort(int[] tableau, int low, int high)`

Classe interne qui étend `RecursiveAction` et implémente la logique du tri rapide parallèle en utilisant des tâches récursives.

## Comment utiliser

1. Assurez-vous d'avoir Java installé sur votre système.
2. Compilez le fichier Java avec `javac Quicksort.java`.
3. Exécutez le programme avec `java Quicksort`.

## Fonctionnalités

- Tri rapide avec une méthode de tri parallèle pour améliorer les performances sur des tableaux de grande taille.
- Le seuil de commutation est ajusté dynamiquement pour optimiser le tri parallèle en fonction de la taille du tableau et du nombre de threads disponibles.

## Remarques

- Ce programme utilise la classe `ForkJoinPool` pour gérer les tâches parallèles et exploiter le parallélisme.
- Le seuil détermine la taille minimale du sous-tableau pour lequel le tri parallèle sera appliqué.