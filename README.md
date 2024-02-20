# spring-jdbc-demo
Illustrate spring-boot issue 32295

Experience:
org.springframework:spring-jdbc greater than version 6.1.1 cause procedure call to fail when schema is named with underscore '_'
It seems like this only effects procedure calls

Branch
- main: Test is running fine spring boot 3.2.0 and schema with name 'schema_demo'
- spring_3_2_1: Test is not running with schema name 'schema_demo'
- spring_3_2_1_nameWithoutUnderscore: Test is running with schema name 'schemademo'
- spring_3_2_1_jdbc: Test is running with schema name 'schema_demo', when spring-jdbc is set to 6.1.1
