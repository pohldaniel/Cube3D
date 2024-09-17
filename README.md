# Cube3D
Important use lfs clone

git lfs clone https://github.com/pohldaniel/Cube3D.git C:\Cube

#Certificate Setup
- install Cyggwin with "openssl" and "jq" and "dos2unix"
- open Cygwin and change directory to vault-terraform subfolder "cd /cygdrive/c/Cube/vault-terraform"
- run the command "dos2unix create-initial-certs"
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

#Useful Curls and Links


curl -k -X POST "https://auth-server:8443/oauth2/introspect" -H "Authorization: Basic Y3ViZTpzZWNyZXQ" -H "Content-type: application/x-www-form-urlencoded" -H "Accept: application/json" -d "token=eyJraWQiOiIzZGMxZmUyMy03ODQyLTQwZDEtOWMzNS1kMjc1ZDFmYTM4OTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImN1YmUiLCJuYmYiOjE3MjY1NjAyNTksInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwczovL2F1dGgtc2VydmVyOjgyMDAiLCJleHAiOjE3MjY1NjA1NTksImlhdCI6MTcyNjU2MDI1OSwianRpIjoiZWRmODJlNzEtYjhmNi00ZDAzLTljOGEtMjYzNDc2NzU0ODY5In0.hF1ZDBjrnw289gHJzjr_OQPyB5-TuWFdr53KytiL4G5xLUmm66p_TOMhC_jCElkBZUkkjdJVn_shvMuaoa51zBQDXWz-_PpmnGKct-vDmEhKZsNr_KzuA_ADxSxcQ_7eB7r-Zgr7RcrTBb1suhztf9xeuJaaAtKhvWE8vIigUweaUP03v4KbD-D6b46ByOP66OKuWLAQ197JN1NLqjXLjLTxbsGITaLxMSpvOEuPT-BoPp82BMxOlAdWIQrrAmFzxB-OI7EvF-eJnuIOkjY4e5u87_eW1HsGLA6Befqsy6kH4FF-jhfSuveg9ww8iYoss8UP3jdYimw7wMpJMsqJ4A"
curl -k -X POST "https://auth-server:8443/oauth2/token" -H "Content-Type: application/x-www-form-urlencoded" --data-urlencode "scope=openid" --data-urlencode "code=5JNMVmRHVmK_G09DgSUrN84sz5cZJ8bgyFHRG871jKLBW01g1Z8wtyRXHjKPv66xFQ7BnjomG-jSEHECiJTF4qiQGgkmQeM4jQxleCfIDEDdv-zK81_JetlZA-Voikp2" --data-urlencode "grant_type=authorization_code" --data-urlencode "client_id=cube" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=http://localhost:8080/spring/oidc/callback"  

https://auth-server:8200/v1/identity/oidc/provider/user-provider/.well-known/openid-configuration
https://auth-server:8443/.well-known/openid-configuration

https://localhost:8200/ui/vault/identity/oidc/provider/user-provider/authorize?with=github&client_id=cube&redirect_uri=https%3A%2F%2Flocalhost%3A8080%2Fvault%2Foidc%2Fcallback&response_type=code&scope=openid%20user%20groups"
https://auth-server:8443/oauth2/authorize?client_id=cube&redirect_uri=https%3A%2F%2Flocalhost%3A8080%2Fspring%2Foidc%2Fcallback&response_type=code&scope=openid&nonce=www

https://auth-server:8443/download/own
https://auth-server:8443/download/root