# Spring Microservice Authentication Server

A server for developing spring microservices.

#### Local
- ##### Config in application.yml
  - auth.type : LOCAL
  - auth.url : ${SERVER.IP}:${SERVER.PORT}
- ##### Required run oauth_schema.sql in your database
- ##### KeyTool command to use public key
  - Generate : keytool -genkeypair -alias oauth2 -keyalg RSA -keypass ${PASSWORD} -keystore oauth2.jks -storepass ${PASSWORD}
  - Information : keytool -list -rfc --keystore oauth2.jks | openssl x509 -inform pem -pubkey

#### Keycloak
- ##### Config in application.yml
  - auth.type : KEYCLOAK
  - auth.url : ${KEYCLOAK.IP}:${KEYCLOAK.PORT}/auth/realms/${REALM}

