set VAULT_ADDR=http://localhost:8200

curl --request POST --data "{\"key\": \"hRJmFYnCx9hcwtxNUCtuL528i22AeNlqo9rV9elAiTH/\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"xo/zWi9a3iwY/igh5ByuPxo3/ydx06oeYs3ko/ugvSVq\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"MQXL8G4SaAPxN4XqB5QdpgQ4u6ptv/clO2PAOngM8BZf\"}" http://127.0.0.1:8200/v1/sys/unseal
rem cmd /k