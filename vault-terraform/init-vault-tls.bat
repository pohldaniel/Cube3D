REM after unsealing VAULT

set VAULT_ADDR=https://localhost:8200
set VAULT_CACERT=../certs/root.crt
set VAULT_CLIENT_CERT=../certs/vault-int.crt
set VAULT_CLIENT_KEY=../certs/vault-int.key
set VAULT_TOKEN=hvs.33HFYMAB50m488xffs4zbUWc

pushd config
..\terraform init
..\terraform apply
popd

cmd /k