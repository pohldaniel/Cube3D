REM after unsealing VAULT

set VAULT_ADDR=http://localhost:8200
set VAULT_TOKEN=hvs.VaZVJa9L6TyR88QTVITZBZL6

pushd config
..\terraform init
rem ..\terraform import vault_auth_backend.userpass userpass
..\terraform apply
popd

cmd /k