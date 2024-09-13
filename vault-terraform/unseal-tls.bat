set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=certs/root.crt
set VAULT_CLIENT_CERT=certs/cube-client.crt
set VAULT_CLIENT_KEY=certs/cube-clien.key

curl -k --request POST --data "{\"key\": \"t15JH1YfbaQDKksCMbKsvpFL2KuV1wuRxQunqvXd9cd4\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"mgjIVVTrzWNDF6jQ2OeLp+cG4XgAG1jBoTFgc85YqifT\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"Nd6ySJOG6r66s47XndzC5qLOsjvrNEyabq+nye4dxmQG\"}" https://127.0.0.1:8200/v1/sys/unseal
rem cmd /k