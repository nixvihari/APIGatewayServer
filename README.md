## API Gateway 

API gateway receives client requests, filters, resolves and routes to respective service through eureka server.

Implemented

- Logging Global Filter - logs incoming requests
- Basic Authorization - Authorize incoming user requests based on roles and user details (username and password)
- Fallback - when service is inactive, redirects to fallback page
