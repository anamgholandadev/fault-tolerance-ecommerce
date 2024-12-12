#baixando a imagem do jdk 17 no container
FROM openjdk:17
#copia a pasta atual para a pasta /exchange dentro do container
COPY . /exchange
#entrando na pasta exchange e compilando o projeto
WORKDIR /exchange
RUN ./mvnw package
RUN mv target/*.jar exchange.jar 
#comando que irá rodar quando subir o container
CMD [ "java","-jar","exchange.jar"]