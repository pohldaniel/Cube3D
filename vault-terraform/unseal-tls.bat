set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=certs/root.crt
set VAULT_CLIENT_CERT=certs/cube-client.crt
set VAULT_CLIENT_KEY=certs/cube-clien.key

curl -k --request POST --data "{\"key\": \"hRJmFYnCx9hcwtxNUCtuL528i22AeNlqo9rV9elAiTH/\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"xo/zWi9a3iwY/igh5ByuPxo3/ydx06oeYs3ko/ugvSVq\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"MQXL8G4SaAPxN4XqB5QdpgQ4u6ptv/clO2PAOngM8BZf\"}" https://127.0.0.1:8200/v1/sys/unseal
cmd /k