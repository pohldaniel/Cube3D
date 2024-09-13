resource "vault_policy" "token-policy" {
  name   = "token-policy"
  policy = file("policy.hcl")
}

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
  depends_on = [vault_auth_backend.userpass]
  path = "auth/userpass/users/daniel"
  data_json = <<EOT
  {
   "password": "salt",
   "policies": "default, token-policy"
  }
  EOT
}

resource "vault_identity_entity" "daniel_userpass" {
    name = "daniel_userpass"
    policies = ["token-policy"]
    metadata = {
        mail = "test@test.de"
    }
}

resource "vault_identity_entity_alias" "daniel_userpass_alias" {
    name = "daniel_userpass"
    mount_accessor = vault_auth_backend.userpass.accessor
    canonical_id = vault_identity_entity.daniel_userpass.id
}

/*resource "vault_identity_entity" "daniel_cert" {
    name = "daniel_cert"
    policies = ["token-policy"]
    metadata = {
        mail = "test@test.de"
    }
}

resource "vault_identity_entity_alias" "daniel_cert_alias" {
    name = "Daniel Pohl"
    mount_accessor = vault_auth_backend.cert.accessor
    canonical_id = vault_identity_entity.daniel_cert.id
}*/

/*resource "vault_generic_endpoint" "userpass" {
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
  depends_on = [vault_mount.secret]
  path = "secret/aws-access"

  data_json = <<EOT
  {
    "username": "aws-username",
    "password": "aws-password"
  }
  EOT
}

resource "vault_identity_group" "personmanager" {
  name                        = "PERSON_MANAGER"
  type                        = "internal"
  external_member_entity_ids  = true
  policies = ["token-policy"]
  metadata = {
    version = "2"
  }
}

resource "vault_identity_group_member_entity_ids" "members_personmanager" {
  member_entity_ids = [vault_identity_entity.daniel_userpass.id]
  exclusive = false
  group_id = vault_identity_group.personmanager.id
}

resource "vault_identity_group" "admin" {
  name                        = "ADMIN"
  type                        = "internal"
  external_member_entity_ids  = true
  policies = ["token-policy"]
  metadata = {
    version = "2"
  }
}

resource "vault_identity_group_member_entity_ids" "members_admin" {
  member_entity_ids = [vault_identity_entity.daniel_userpass.id]
  exclusive = false
  group_id = vault_identity_group.admin.id
}

/*resource "vault_identity_group_alias" "group-alias" {
  name           = "PERSON_MANAGER"
  mount_accessor = vault_auth_backend.userpass.accessor
  canonical_id   = vault_identity_group.personmanager.id
}*/
