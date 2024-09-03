set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=certs/root.crt
set VAULT_CLIENT_CERT=certs/vault-int.crt
set VAULT_CLIENT_KEY=certs/vault-int.key

curl -k --request POST --data "{\"key\": \"mBQZV0tbxvYoIgKqp/Hofr8o7faZYsRtJJtDegldyUCR\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"HVlVCYRbXIz5F1chUdQhBECPEAE5TP11nGyt94T0N3PR\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"S695RDAKex5QUgJRzrBlrB+LJsE0cOvaZyx6zCHRc9xU\"}" https://127.0.0.1:8200/v1/sys/unseal
rem cmd /k