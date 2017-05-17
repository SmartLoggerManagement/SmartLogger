## I Démarche de Qualité
Une application de la taille de **SmartLogger** se doit d'être une application de qualité. Grâce à une gestion de la qualité soignée, nous avons pu assurer la robustesse, la maintenablilté, la conformité et la fiabilité de l'application, cela au moindre coût.
Pour faire cela, un ensemble de mesures ont été prises, celles-ci seront détaillées si dessous.
Il est important de noter, que la démarche du test-driven developpement a été abandonnée, car la nature des technologies, ainsi que notre faible expérience dans la gestion d'un projet de cette taille aurait rendu cette methode dangereuse et inefficace.

## II Stratégies de tests effectués

### Unitaire
Pour chaque classe de l'application, nous avons effectué des tests élémentaires, afin de vérifier le bon fonctionnement de l'application, sa capacité à réagir en cas de mauvaise entrée, ainsi qu'en cas d'erreurs. Les tests unitaires représentent le plus gros de la volumétrie de test, et au final, ceci ne testent qu'une fonctionnalité chacun, pour un module donné, à un temps donné. Ce qui ne les empêche pas d'être cruciaux et vitaux au bon fonctionnement de l'application.
Pour effectuer les tests unitaires, nous avons majoritairement utilisé *ScalaTest*, ce dernier étant le framework qui nous a semblé le plus adapté, étant donné notre environnement de developpement ainsi que le language sur lequel est basé l'application, en grande partie en Scala.
Nous avons aussi utilisé *Mockito-Sugar*, afin de tester certaines classes qui étaient non testable seules, étant trop dépendantes d'autres classes, soit non dévelopées, soit trop lourdes à instancier. Ainsi, ces dernières sont **mockées** afin de simuler leur comportement, sans avoir à les instancier.
Enfin, il est important de noter que la condition de validité d'une classe, est le passage de 90% de ces tests, ainsi que le passage de 100% des tests critiques, tests étant défini comme déclencheur de défaillance lors d'une execution de l'application.
### Intégration
Après avoir effectué les tests unitaires de différents modules, des tests d'intégration ont été effectués. L'objectif de ces test est de controller les communications entre plusieurs modules, testant ainsi leur inter-opérabilité. Leur but final est de tester l'architecture globale de l'application pour une itération donnée.
### Montée en charge
Certain modules ont été testés en situation de stress, c'est a dire en observant leurs comportements en cas de solicitation élevée du système. Particulièrement les modules d'entrée et de sortie, ainsi que le module analytique : les modules consernés principalement par ce problème.
Dans cet optique, nous avons eu recours à *Gatling*, qui est un framework dédiée au test de performance et de reaction. Cela nous a permis de simuler l'envoi d'une quantité massive de log, par requête HTTP, et de suivre la capacité du système à les recevoir.
### Performances
Afin d'évaluer la performance du module analytique, nous avons effectué du parangonage, par lequel nous avons pu comparer la précision, l'efficacité et le coûts des différents algorithmes de classication. Tout cela conformément aux exigences client.
Grace à ces test, nous avons dégagé deux algorithmes principaux, qui sont : `NaiveBayes` et `DecisionTreeClassifier`.
### Comportementaux
L'IHM a été, quand à elle, testée visuellement afin de vérifier que son rendu soit conforme à celui spécifié dans les documents de réference.
L'IHM étant un module de moindre importance, elle n'a été testée que sommairement. Mais différent frameworks ont été recherchés pour l'occasion, et si l'on devait étendre l'IHM, nous utiliserions *Cuncumber*.

## III Conformité exigences client
Enfin, un ensemble de réunions avec le client ont été mise en place, afin qu'il puisse suivre l'avancement de l'application et valider ses avancés, ainsi que suggérer de nouveaux ou différents axes pour les modules.
Pour cela, des simulations de fonctionnement de l'application, des démonstrations ont été réalisées, et soumises au client.

## IV Synthese des tests
| Module      | Couverture | Taux de passage | Acceptation |
|-------------|------------|-----------------|-------------|
| Analytique  |    100%    |       100%      |     Oui     |
| Entrée      |    100%    |       100%      |     Oui     |
| Sortie      |    100%    |       100%      |     Oui     |
| Utilitaire  |    100%    |       100%      |     Oui     |
| Persistance |     90%    |       90%       |     Oui     |
| Interface   |     90%    |       100%      |     Oui     |
