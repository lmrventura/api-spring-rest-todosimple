# api-spring-rest-todosimple

# service - para reutilizar o código nos controllers ou até mesmo outros services e modularizar o programa
# repositories - se conecta com o banco de dados diretamente
# src/resources/application.properties: 

# <dependency>
#    <groupId>mysql</groupId>
#    <artifactId>mysql-connector-java</artifactId>
#    <scope>runtime</scope>
# </dependency>

# mavenrepository.com - site para pegar dependências
# spring-boot-starter-validation - para validar os dados da classe em tempo real, não deixando o erro cair no banco de dados 

# MVCS
# A requisição do FRONT cai no Controller, que chama o Service que comunica ao Repository que vai usar o Model para persistir ou buscar dados no BD
# Camada de negócios que aumenta a modularidade/reusabilidade