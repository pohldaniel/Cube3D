provider "vault" {
  address = "http://127.0.0.1:8200"
  token = "hvs.PcUOa2KS9qlHvV5N4cPFp591"
}

resource "vault_mount" "secret" {
  path = "secret"
  type = "kv"
}

resource "vault_policy" "secret-policy" {
  name   = "secret-policy"
  policy = file("policy.hcl")
}