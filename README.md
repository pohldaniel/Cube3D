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
	
  * Copy the spring-trust.p12 truststore to "C:\Cube\Security-service\src\main\resources\certs"

- import the Certificate "root.crt" as trusted certificate authority
- import the Certificate Stores "daniel.p12", "admin.p12" and "personmanager.p12" as "own certificates"

Additionally: For the Spring Authorization server is an IP alias necessary. For Windows go to "C:\Windows\System32\drivers\etc\hosts" and add the line "127.0.0.1 auth-server"
at the end of the File.

#Vault Security Server
- open cmd and change to the directory "C:\Cube\vault-terraform" run the command
  
  vault server -config=config-tls.hcl
  
- open a new cmd without closing the old one change to the directory "C:\Cube\vault-terraform" and execute the following commands

  set VAULT_ADDR=https://127.0.0.1:8200
  vault operator init
  
- replace "Initial Root Token" and the "Unseal Key's" inside the files "unseal.bat, unseal-tls.bat, init-vault.bat, init-vault-tls.bat, create-user-admin, create-user-joe, create-user-daniel"
- double click "unseal-tls.bat" and "init-vault-tls.bat"
- Visit https://localhost:8200 and login with the root token. Go to "Access-> OIDC Provider" open the Cube provider and exchange the "Client ID" and the "Client Secret"
  inside the "C:\Cube3D-service\src\main\resources\application.properties"
- open Cygwin and change directory to vault-terraform subfolder "cd /cygdrive/c/Cube/vault-terraform"
- run the commands "./create-user-daniel" and "./create-user-joe"

#Cube UI
- open the folder "C:\Cube3D-ui" with Visual Studio open a new Terminal and run

  npm install
  ng serve --ssl true --ssl-key angular-server.key --ssl-cert angular-server.crt --host localhost --port 4200
  
#Cube Service and Spring Security Service
- import "Cube3D-servcie" and "Security-service" in your prefered IDE
- run the applications check "https://auth-server:8443/.well-known/openid-configuration" to see if something is working

#Important: Close every Webbrowser to make the Certificate selection appearing

Login with OIDC
- Open the side "https://localhost:8080/vault/login", choose the certificate "admin", open "More Options " enter "cert" inside the Mountpath and press "Sign in"
- Open the side "https://localhost:8080/spring/login"
- Direct link https://localhost:4200/help Important it uses spring as default provider. Modifiable at "C:\Cube3D-ui\src\environments\environments.ts" and "C:\Cube3D-ui\src\environments\environments.prod.ts"

Login with Username Password
- Open the side https://localhost:4200/gateway and press "Sign in"