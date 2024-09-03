resource "vault_pki_secret_backend_config_urls" "config_urls" {
  depends_on = [ vault_mount.root ]  
  backend              = vault_mount.root.path
  issuing_certificates = ["http://vault1.${var.server_cert_domain}:8200/v1/pki/ca"]
  crl_distribution_points= ["http://vault1.${var.server_cert_domain}:8200/v1/pki/crl"]
}

data "local_file" "rootkey" {
  filename = "../certs/root.key"
}

data "local_file" "rootcrt" {
  filename = "../certs/root.crt"
}

data "local_file" "intermediatecrt" {
  filename = "../certs/vault-int.pem"
}

data "local_file" "intermediatekey" {
  filename = "../certs/vault-int.key"
}

resource "vault_pki_secret_backend_config_ca" "ca_config3" {
  depends_on = [vault_mount.root]  
  backend  = vault_mount.root.path
  pem_bundle = "${data.local_file.rootkey.content}\n${data.local_file.rootcrt.content}"
}

resource "vault_pki_secret_backend_role" "role-user-cert" {
  depends_on = [vault_mount.pki_int]  
  backend = vault_mount.pki_int.path
  name = "user-cert"
  allow_any_name= true
  # 2 years
  max_ttl = 63113904
  # 30 days
  ttl = 2592000
  enforce_hostnames = false
}

