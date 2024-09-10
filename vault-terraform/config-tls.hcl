api_addr = "https://127.0.0.1:8200"
ui = true
disable_mlock = true

listener "tcp" {
  address = "127.0.0.1:8200"
  tls_disable = "false"
  tls_cert_file = "./certs/cube-server.crt"
  tls_key_file = "./certs/cube-server.key"
  #tls_ca_file = "./certs/root.crt"
  #tls_disable_client_certs = "true"  
}

storage "file" {
  path = "./data"
}