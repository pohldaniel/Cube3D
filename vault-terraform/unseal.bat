set VAULT_ADDR=http://localhost:8200
set VAULT_CACERT=../certs/root.crt
set VAULT_TOKEN=hvs.o2bMEY32LKp8DYjwqEjQ3WGL

curl --request POST --data "{\"key\": \"gM7KJ9SuvokRtO81rtZR5L0nx2Wkz0fatPeXwSJdo/T4\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"I/g0dycpLowFP87JQIgLZ2wLDMvZ2Ikw257ufQa4D3Sx\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"vXwH5Fe/eMiue+lRg06TMyJUFewBoxSkeGs2tR05R1HE\"}" http://127.0.0.1:8200/v1/sys/unseal
rem cmd /k