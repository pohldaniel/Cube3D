REM after unsealing VAULT

set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=../certs/root.crt
set VAULT_CLIENT_CERT=../certs/cube-client.crt
set VAULT_CLIENT_KEY=../certs/cube-client.key
set VAULT_TOKEN=hvs.VaZVJa9L6TyR88QTVITZBZL6

pushd config
..\terraform init
..\terraform apply
popd

cmd /k