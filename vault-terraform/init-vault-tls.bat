REM after unsealing VAULT

set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=../certs/root.crt
set VAULT_CLIENT_CERT=../certs/vault-client.crt
set VAULT_CLIENT_KEY=../certs/vault-client.key
set VAULT_TOKEN=hvs.CmlIRPJRtMhkjnmN5uKid3pK

pushd config
..\terraform init
..\terraform apply
popd

cmd /k