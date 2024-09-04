set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=certs/root.crt
set VAULT_CLIENT_CERT=certs/vault-client.crt
set VAULT_CLIENT_KEY=certs/vault-clien.key

curl -k --request POST --data "{\"key\": \"uBqzgDKhw8YfQSPQlw5f6PVSwLiJRVQ5JiiQzaBn5kol\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"K9Fgd4vOxBGjeh4HRZJxijyT+Kr+FP8Co5dg0kVYGzt8\"}" https://127.0.0.1:8200/v1/sys/unseal
curl -k --request POST --data "{\"key\": \"IexRcSaExPs2YfXBPKO6zG867sgKEG+Rs0a8m4qhHqSq\"}" https://127.0.0.1:8200/v1/sys/unseal
cmd /k