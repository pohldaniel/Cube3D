provider "vault" {
  address = "http://127.0.0.1:8200"
  token = "hvs.EYKmnkVqDUs5l8HSHDgmjlSQ"
}

resource "vault_policy" "token-policy" {
  name   = "token-policy"
  policy = file("policy.hcl")
}

/*resource "vault_auth_backend" "approle" {
  type = "approle"
}

resource "vault_approle_auth_backend_role" "approle" {
  role_name      = "app"
  token_policies = ["token-policy"]
}*/

resource "vault_token_auth_backend_role" "tokenrole" {
  role_name      = "tokenrole"
  allowed_policies = ["token-policy"]
  orphan         = true
# token_ttl		 = 768
# token_max_ttl  = 36000000
  renewable      = true
  token_period = "2764800"
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

/*resource "vault_identity_entity" "userpass" {
    name = "daniel"
    policies = ["token-policy", "default"]
}

resource "vault_generic_endpoint" "userpass" {
  path = "auth/userpass/users/daniel2"
  ignore_absent_fields = true

  data_json = <<EOT
  {
    "password": "salt",
    "policies": "default, token-policy"
  }
  EOT
}*/

resource "vault_generic_secret" "aws-access" {
  path = "secret/aws-access"

  data_json = <<EOT
  {
    "username": "aws-username",
    "password": "aws-password"
  }
  EOT
}

resource "vault_identity_oidc_client" "cube" {
  name          = "cube"
  redirect_uris = [
    "http://localhost:8080/oidc/callback"
  ]
  assignments = [
    "allow_all"
  ]
  #id_token_ttl     = 2400
  #access_token_ttl = 7200
}