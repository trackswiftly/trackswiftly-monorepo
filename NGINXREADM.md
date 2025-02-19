# Advanced Nginx Topics

Nginx is a powerful, high-performance web server, reverse proxy, and load balancer. Mastering advanced topics can help you optimize performance, enhance security, and handle complex use cases. Below are some advanced topics in Nginx:

---

## 1. Performance Optimization

### Worker Processes and Connections
- Tune `worker_processes` (set to `auto` or the number of CPU cores).
- Adjust `worker_connections` to handle more concurrent connections.
- Example:
  ```nginx
  worker_processes auto;
  events {
      worker_connections 1024;
  } ```

### Caching

- Use proxy caching to reduce backend load.


```
proxy_cache_path /data/nginx/cache levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m use_temp_path=off;
server {
    location / {
        proxy_cache my_cache;
        proxy_pass http://backend;
    }
}
```



### Gzip Compression

- Enable gzip to reduce response sizes.

```
gzip on;
gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
```


### Keepalive Connections

- Reuse TCP connections to reduce overhead.

```
upstream backend {
    keepalive 32;
    server 127.0.0.1:8080;
}
```



## 2. Load Balancing

### Load Balancing Methods
- Round-robin (default), least connections, IP hash, etc.

- Example:
```
upstream backend {
    least_conn;
    server 192.168.1.101;
    server 192.168.1.102;
}
```

### Health Checks
    
- Use max_fails and fail_timeout to mark servers as unhealthy.

Example:

nginx
```server 192.168.1.101 max_fails=3 fail_timeout=30s;```


### Dynamic Load Balancing
- Integrate with service discovery tools like Consul or DNS-based discovery.


## 3. Security Enhancements


### SSL/TLS Configuration
- Use strong ciphers and protocols.

``` ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers HIGH:!aNULL:!MD5;
ssl_prefer_server_ciphers on;
```



### Rate Limiting
- Limit requests per IP to prevent abuse.

```
limit_req_zone $binary_remote_addr zone=one:10m rate=10r/s;
location / {
    limit_req zone=one burst=5;
}
```



### Web Application Firewall (WAF)
* Integrate with ModSecurity or similar tools.

### Prevent Common Attacks
* Mitigate DDoS, SQL injection, and XSS attacks using Nginx directives.



## 4. Advanced Routing and Rewrites


### Regex-Based Routing

- Use regex in location blocks for complex routing.

```
location ~* \.(jpg|png|gif)$ {
    expires 30d;
}
```



### URL Rewriting
- Use rewrite to modify URLs.

Example:

```rewrite ^/old-path/(.*)$ /new-path/$1 permanent;```


### Conditional Logic
- Use if statements for conditional routing (use sparingly).

Example:

```if ($http_user_agent ~* "MSIE") {
    return 403;
}
```

## 5. High Availability and Failover
### Active-Passive Setup
- Use multiple Nginx instances with a floating IP or DNS failover.

### Graceful Shutdown
- Use nginx -s quit to stop Nginx gracefully without dropping connections.




## 6. Logging and Monitoring
### Custom Log Formats
- Define custom log formats for better analysis.

Example:

```
log_format custom '$remote_addr - $remote_user [$time_local] "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for"';
access_log /var/log/nginx/access.log custom;
```

### Error Logging
- Set different log levels (debug, info, warn, error).

Example:

```error_log /var/log/nginx/error.log warn;```