api_addr = "http://127.0.0.1:8200"
ui = true
disable_mlock = true

listener "tcp" {
  address = "127.0.0.1:8200"
  tls_disable = "true"
  tls_cert_file = "./certs/vault.crt"
  tls_key_file = "./certs/vault.key"
  tls_ca_file = "./certs/root.crt"
  #tls_disable_client_certs = "true"  
}

storage "file" {
  path = "./data"
}