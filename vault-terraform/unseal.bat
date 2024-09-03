set VAULT_ADDR=http://localhost:8200

curl --request POST --data "{\"key\": \"KB3QNv5ea7z7DuVy8kAq/6QFEJHXiHzS5MqiYQZGksji\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"o/QCJOnAyG/IgE7eGICDsUtLcq9APiHBSvnkHYJpmraT\"}" http://127.0.0.1:8200/v1/sys/unseal
curl --request POST --data "{\"key\": \"jY8pRfLTwyt2i2+ClvVZNJegOVkLHC1IvkxaXoq380Ko\"}" http://127.0.0.1:8200/v1/sys/unseal
rem cmd /k