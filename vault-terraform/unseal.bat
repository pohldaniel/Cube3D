set VAULT_ADDR=http://localhost:8200

curl --request POST --data "{\"key\": \"t15JH1YfbaQDKksCMbKsvpFL2KuV1wuRxQunqvXd9cd4\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"mgjIVVTrzWNDF6jQ2OeLp+cG4XgAG1jBoTFgc85YqifT\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"Nd6ySJOG6r66s47XndzC5qLOsjvrNEyabq+nye4dxmQG\"}" http://127.0.0.1:8200/v1/sys/unseal
rem cmd /k