provider "vault" {
  address = "http://127.0.0.1:8200"
  token = "hvs.svzrLIXsv5FiLwPdOxZ6cHjF"
}

resource "vault_mount" "secret" {
  path = "secret"
  type = "kv"
}

resource "vault_policy" "token-policy" {
  name   = "token-policy"
  policy = file("policy.hcl")
}

resource "vault_token_auth_backend_role" "tokenrole" {
  role_name      = "tokenrole"
  orphan         = true
  renewable      = true
  token_period = "2764800"
  allowed_policies = ["token-policy"]
}

resource "vault_auth_backend" "userpass" {
  type = "userpass"
}

resource "vault_generic_secret" "userpass" {
  path = "auth/userpass/users/daniel"
  data_json = <<EOT
  {
   "password": "salt",
   "policies": "default, token-policy"
  }
  EOT
}

resource "vault_generic_secret" "aws-access" {
  path = "secret/aws-access"

  data_json = <<EOT
  {
    "username": "aws-username",
    "password": "aws-password"
  }
  EOT
}