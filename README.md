![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=plugin-anonymizationphoto-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-anonymizationphoto)

# Plugin anonymizationphoto

## Presentation

This plugin anonymizes photos. It calls a service that detects faces on photos and anonymises them. It is possible to perform a preprocessing on the photo before its anonymization and a postprocessing on an annymized photo.The plugin contains a back office screen for viewing photo anonymization statistics.

## How to use it

 **Simple mode** 

To anonymized a photo call method "callServiceAnonymization" from service AnonymizationPhotoCallService. The photo to anonymized must be pass in the PhotoToAnonymized object. The anonymized photo will be return in ResponseAnonymizationPhoto object.

 `public ResponseAnonymizationPhoto callServiceAnonymization( PhotoToAnonymized photoToAnonymized )` 

 **Daemon mode** 

The plugin contains a Lutece daemon which anonynmize a list of photos. To use the daemon mode is required to implements the method loadPhotos from interface IAnonymizationPhotoDAO.This method will provide the list of photo to anonymized. `public List<PhotoToAnonymized>loadPhotos( );` 


 Optionnal : you can implements one of these methods from the interface IAnonymizationPhotoService :

*  `default void preTreatment( List<PhotoToAnonymized>listPhoto )` to make a pre operation on photos before anonymized them
*  `default void postTreatment( ResponseAnonymizationPhoto responseAnonymizationPhoto )` to make a post operation on anonymized photos

## File anonymizationphoto.properties description


 
*  **anonymizationphoto.daemon.max.photo :** maximum number of photos anonymized in daemon mode.
*  **anonymizationphoto.anonymization.service.url :** URL of the anonymization service.
*  **anonymizationphoto.application.code :** application code to identify which application use anonymization service



[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-anonymizationphoto/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*