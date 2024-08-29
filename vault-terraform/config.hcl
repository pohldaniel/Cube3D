api_addr = "http://127.0.0.1:8200"
ui = true
disable_mlock = true

listener "tcp" {
  address = "127.0.0.1:8200"
  tls_disable = true
}

storage "file" {
  path = "./data"
}