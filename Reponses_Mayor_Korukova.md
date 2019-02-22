# Réponses labo 3 SYM
*Samuel Mayor et Alexandra Korukova*  
## NFC
*Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8
dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément
copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.*  

*A partir de l’API Android concernant les tags NFC4, pouvez-vous imaginer une autre approche pour
rendre plus compliqué le clonage des tags NFC ? Est-ce possible sur toutes les plateformes (Android et
iOS), existe-il des limitations ? Voyez-vous d’autres possibilités ?*  

Pour rendre la tâche du clonage des tags NFC plus compliquée, on pourrait utiliser l'identificateur unique présent par défaut sur de nombreux tags NFC afin de valider dans l'application que ce tag est bien l'original grace à une liste d'identificateurs authorisés. Cette méthode n'est pas parfaite car il est possible d'attribuer un identificateur authorisé à n'importe quel tag si on le connait. Dans ce cas, la plateforme n'importe pas. Cependant le tag doit avoir un identificateur unique.  

On pourrait aussi utiliser un tag qui propose l'encryption des communications avec des clés partagées. MIFARE propose cela. Par contre, la clé du côté de l'application doit être stockée dans la mémoire du téléphone. Un attaquant pourrait donc y avoir accès et cloner le tag.

## Code-barre
*Comparer la technologie à codes-barres et la technologie NFC, du point de vue d'une utilisation dans
des applications pour smartphones, dans une optique :*  
*• Professionnelle (Authentification, droits d’accès, stockage d’une clé)*  
*• Grand public (Billetterie, contrôle d’accès, e-paiement)*  
*• Ludique (Preuves d'achat, publicité, etc.)*  
*• Financier (Coûts pour le déploiement de la technologie, possibilités de recyclage, etc.)*  

**Professionnel** :  Les tags NFC bénéficient du chiffrement cela donne une meilleure protection des données. De plus, l'utilisation de tags est plus pratique que celle des codes-barres ce qui la rend plus adaptée à une utilisation en milieu professionnel.  
**Grand public** :  La technologie NFC n'est pas accessible sur autant d'appareils que celle des codes-barres. On touchera donc plus de monde avec les codes-barres.  
**Ludique** :  Les codes-barres sont imprimables donc plus facile à mettre à disposition sur des prospectus ou autres publicité. De plus l'argument cité pour le grand public est valable ici aussi.  
**Financier** :  Le coût de déploiement de NFC est bien supérieur à celui des codes-barres. Le coût de recyclage également car il s'agit de remplacer les information dans tous les tags NFC utilisés.

## IBeacon
*Les iBeacons sont très souvent présentés comme une alternative à NFC. Pouvez-vous commenter cette
affirmation en vous basant sur 2-3 exemples de cas d’utilisations (use-cases) concrets (par exemple epaiement, second facteur d’identification, accéder aux horaires à un arrêt de bus, etc.).*  

Les iBeacons ont une portée bien supérieure à celle de NFC. Les cas d'utilisation sont donc différents.  

Il serait par exemple possible de remplacer un tag NFC destiné à informer les clients d'un restaurant du menu du jour par un iBeacon. Cela permettrait d'accèder aux informations sans avoir à s'approcher du tag.  

Dans le cas d'un e-paiement, un iBeacon ne convient pas car on souhaite que le paiement ne se réalise qu'au moment ou le capteur est proche de l'emetteur.  

Pour un second facteur d'identification, la portée du iBeacon diminue la sécurité de l'identification. Comme pour le e-paiement, on souhaite que le capteur et l'emetteur soient proche au moment de la validation.

## Capteurs
*Une fois la manipulation effectuée, vous constaterez que les animations de la flèche ne sont pas
fluides, il va y avoir un tremblement plus ou moins important même si le téléphone ne bouge pas.
Veuillez expliquer quelle est la cause la plus probable de ce tremblement et donner une manière (sans
forcément l’implémenter) d’y remédier.*  

Les vibrations perçues sont dûes au bruit que le capteur reçoit et traite. Pour corriger cela, on pourrait implémenter un filtre qui détecterait le bruit et diminuerait donc les vibrations.
