# Cube3D
Important use lfs clone

git lfs clone https://github.com/pohldaniel/Cube3D.git C:\Cube

#Certificate Setup
- install Cyggwin with openssl and jq
- open Cygwin and change directory to vault-terraform subfolder "cd /cygdrive/c/Cube/vault-terraform"
- run the command "./create-initial-certs"
- unfortunately it seems there is no possibility to create the needed truststore with openssl so it has to be done manually 
  * Copy the Certificate cube-iss.crt to the used JDK e.g. C:\JDKs\jdk-22.0.2\bin and run the command
  
    keytool -import -trustcacerts -noprompt -file cube-iss.crt -alias "Cube Issuing" -deststoretype PKCS12 -keystore spring-trust.p12 -deststorepass password
	
  * Copy the spring-trust.p12 truststore to "C:\Cube\Security-service/src/main/resources/certs"

- import the Certificate "root.crt" as trusted certificate authority
- import the Certificate Stores "daniel.p12", "admin.p12" and "personmanager.p12" as "own certificates"

Additionally: For the Spring Authorization server is an IP alias necessary. For Windows go to "C:\Windows\System32\drivers\etc\hosts" and add the line "127.0.0.1 auth-server"
at the end of the File.

#Spring Security Server
- import "Cube3D-servcie" and "Security-service" in your prefered IDE
- open "Cube3D-ui" with Visual Studio Code and run
  * npm install
  * ng serve --ssl true --ssl-key angular-server.key --ssl-cert angular-server.crt --host localhost --port 4200
- open "https:\localhost:8080\login" inside your Webbrowser