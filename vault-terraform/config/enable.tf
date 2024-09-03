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

resource "vault_mount" "root" {
    type = "pki"
    path = "pki_root_ca"
    default_lease_ttl_seconds = 31556952 # 1 years
    max_lease_ttl_seconds = 157680000 # 5 years
    description = "Root Certificate Authority"
}

resource "vault_mount" "pki_int" {
    type = "pki"
    path = "pki_int_ca"
    default_lease_ttl_seconds = 63072000 # 2 years
    max_lease_ttl_seconds = 63072000 # 2 years
    description = "Intermediate Authority for Vault"
}