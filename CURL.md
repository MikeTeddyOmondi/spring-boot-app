## Testing with cURL

### Create a Todo
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Complete Spring Boot tutorial and build a todo app",
    "status": "PENDING",
    "dueDate": "2024-12-31T23:59:59"
  }'
```

### Get All Todos
```bash
curl -X GET http://localhost:8080/api/todos
```

### Get Todo by ID
```bash
curl -X GET http://localhost:8080/api/todos/1
```

### Update Todo
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot - Updated",
    "description": "Complete Spring Boot tutorial and build a todo app with advanced features",
    "status": "IN_PROGRESS"
  }'
```

### Delete Todo
```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

### Search Todos
```bash
curl -X GET "http://localhost:8080/api/todos/search?title=spring"
```

### Get Overdue Todos
```bash
curl -X GET http://localhost:8080/api/todos/overdue
```
