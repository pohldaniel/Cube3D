resource "vault_mount" "secret" {
  path = "secret"
  type = "kv"
}

resource "vault_auth_backend" "userpass" {
  type = "userpass"
}

resource "vault_auth_backend" "cert" {
    path = "cert"
    type = "cert"
}