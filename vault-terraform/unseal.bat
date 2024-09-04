set VAULT_ADDR=http://localhost:8200

curl --request POST --data "{\"key\": \"uBqzgDKhw8YfQSPQlw5f6PVSwLiJRVQ5JiiQzaBn5kol\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"K9Fgd4vOxBGjeh4HRZJxijyT+Kr+FP8Co5dg0kVYGzt8\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"IexRcSaExPs2YfXBPKO6zG867sgKEG+Rs0a8m4qhHqSq\"}" http://127.0.0.1:8200/v1/sys/unseal
rem cmd /k