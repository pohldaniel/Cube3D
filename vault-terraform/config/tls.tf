resource "vault_pki_secret_backend_config_urls" "config_urls_int" {
  depends_on = [vault_mount.pki_int]  
  backend              = vault_mount.pki_int.path
  issuing_certificates = ["https://127.0.0.1:8200/v1/pki_int/ca"]
  crl_distribution_points= ["https://127.0.0.1:8200/v1/pki_int/crl"]
}

resource "vault_pki_secret_backend_config_urls" "config_urls_iss" {
  depends_on = [vault_mount.pki_iss]  
  backend              = vault_mount.pki_iss.path
  issuing_certificates = ["https://127.0.0.1:8200/v1/pki_iss/ca"]
  crl_distribution_points= ["https://127.0.0.1:8200/v1/pki_iss/crl"]
}

data "local_file" "rootkey" {
  filename = "../certs/root.key"
}

data "local_file" "rootcrt" {
  filename = "../certs/root.crt"
}

data "local_file" "intermediatecrt" {
  filename = "../certs/cube-int.crt"
}

data "local_file" "intermediatekey" {
  filename = "../certs/cube-int.key"
}

data "local_file" "issuingcrt" {
  filename = "../certs/cube-iss.crt"
}

data "local_file" "issuingkey" {
  filename = "../certs/cube-iss.key"
}

resource "vault_pki_secret_backend_config_ca" "ca_config_ca" {
  depends_on = [vault_mount.root]  
  backend  = vault_mount.root.path
  pem_bundle = "${data.local_file.rootkey.content}\n${data.local_file.rootcrt.content}"
}

resource "vault_pki_secret_backend_config_ca" "ca_config_int" {
  depends_on = [vault_mount.pki_int]  
  backend  = vault_mount.pki_int.path
  pem_bundle = "${data.local_file.intermediatekey.content}\n${data.local_file.intermediatecrt.content}\n${data.local_file.rootcrt.content}"
}

resource "vault_pki_secret_backend_config_ca" "ca_config_iss" {
  depends_on = [vault_mount.pki_iss]  
  backend  = vault_mount.pki_iss.path
  pem_bundle = "${data.local_file.issuingkey.content}\n${data.local_file.issuingcrt.content}\n${data.local_file.intermediatecrt.content}"
}

/*resource "vault_pki_secret_backend_intermediate_cert_request" "issuer" {
  depends_on = [vault_mount.pki_iss]
  backend = vault_mount.pki_iss.path
  type = "internal"
  # This appears to be overwritten when the CA signs this cert, I'm not sure 
  # the importance of common_name here.
  common_name = "Vault Issuing"
  format = "pem"
  private_key_format = "der"
  key_type = "rsa"
  key_bits = "4096"
}

resource "vault_pki_secret_backend_root_sign_intermediate" "issuer" {
  depends_on = [ vault_pki_secret_backend_intermediate_cert_request.issuer]
  backend = vault_mount.pki_int.path

  csr = vault_pki_secret_backend_intermediate_cert_request.issuer.csr
  common_name = "Vault Issuing"
  exclude_cn_from_sans = true
  ou = "Development"
  organization = "Main Gruppe"
  country = "DE"
  locality = "Leipzig"
  province = "Saxonia"

  ttl = 252288000 #8 years
 
}

resource local_file signed_intermediate {
    sensitive_content = vault_pki_secret_backend_root_sign_intermediate.issuer.certificate
    filename = "../certs/vault-iss.pem"
}

resource "vault_pki_secret_backend_intermediate_set_signed" "issuer" { 
 backend = vault_mount.pki_iss.path
 certificate = "${vault_pki_secret_backend_root_sign_intermediate.issuer.certificate}\n${data.local_file.intermediatecrt.content}"
}

resource local_file ca_file {
    sensitive_content = vault_pki_secret_backend_root_sign_intermediate.issuer.certificate
    filename = "../certs/issuer.crt"
}

resource local_file ca_pem_bundle {
    sensitive_content = vault_pki_secret_backend_intermediate_cert_request.issuer.private_key
    filename = "../certs/issuer.key"
    file_permission = "0400"
}

output "issuer_crt" {
    value = vault_pki_secret_backend_root_sign_intermediate.issuer.certificate
}

output "issuer_key" {
    value = vault_pki_secret_backend_intermediate_cert_request.issuer.private_key
	sensitive = true
}*/

resource "vault_pki_secret_backend_role" "role-user-cert-int" {
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

resource "vault_pki_secret_backend_role" "role-user-cert-iss" {
  depends_on = [vault_mount.pki_iss]  
  backend = vault_mount.pki_iss.path
  name = "user-cert"
  allow_any_name= true
  # 2 years
  max_ttl = 63113904
  # 30 days
  ttl = 2592000
  enforce_hostnames = false
}

