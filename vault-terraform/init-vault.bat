REM after unsealing VAULT

set VAULT_ADDR=http://localhost:8200
set VAULT_CACERT=../certs/root.crt
set VAULT_TOKEN=hvs.o2bMEY32LKp8DYjwqEjQ3WGL

REM pushd enable
REM ..\terraform init
REM ..\terraform apply
REM popd

pushd config
..\terraform init
rem ..\terraform import vault_auth_backend.userpass userpass
..\terraform apply
popd

cmd /k