# Project

# 1. hello-service-boot-starter
- Create interface helloService
- HelloServiceAutoConfig class to auto create helloServiceImpl when not have any bean helloService in context

# 2. demo-starter-template
- Use hello-service-boot-starter as a dependency
- Implement helloService to CustomService
- Test properties in dev, prod, default environment
- Run jar file with profile

```
java -jar myapp.jar --spring.profiles.active=dev
```

# 3. demo-maven-jar-plugin
- Build jar file with maven-jar-plugin dependency

# 4. demo-test
- Test with @SpringBootTest and  @TestConfiguration use Mock


