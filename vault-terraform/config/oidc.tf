resource "vault_identity_oidc_scope" "groups" {
  name        = "groups"
  template    = "{\"roles\":{{identity.entity.groups.names}}}"
  description = "Vault OIDC Groups Scope"
}

resource "vault_identity_oidc_scope" "user" {
  name        = "user"
  template    = <<EOT
                    { 
                        "user": { 
                                  "name": {{identity.entity.aliases.${vault_auth_backend.userpass.accessor}.name}},
                                  "mail": {{identity.entity.metadata.mail}}
                                }
                    }
				 EOT
  description = "Vault OIDC User Scope"
}

resource "vault_identity_oidc_provider" "userprovider" {
  name = "user-provider"
  https_enabled = "false"
  issuer_host = "127.0.0.1:8200"
  allowed_client_ids = ["*"]
  scopes_supported = ["groups", "user"]
}

resource "vault_identity_oidc_assignment" "userassignment" {
  name       = "user-assignment"
  entity_ids = [vault_identity_entity.daniel.id]
  group_ids  = [vault_identity_group.personmanager.id]
}

resource "vault_identity_oidc_key" "key" {
  name      = "key"
  algorithm = "RS256"
  #allowed_client_ids = ["*"]
  allowed_client_ids = [vault_identity_oidc_client.cube.client_id]
}

resource "vault_identity_oidc_client" "cube" {
  name          = "cube"
  redirect_uris = [
    "http://localhost:8080/oidc/callback"
  ]
  key = "key"
  assignments = ["user-assignment"]
  #id_token_ttl     = 2400
  #access_token_ttl = 7200
}

