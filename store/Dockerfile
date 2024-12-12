#baixando a imagem do jdk 17 no container
FROM openjdk:17
#copia a pasta atual para a pasta /store dentro do container
COPY . /store
#entrando na pasta store e compilando o projeto
WORKDIR /store
RUN ./mvnw package
RUN mv target/*.jar store.jar 
#comando que irá rodar quando subir o container
CMD [ "java","-jar","store.jar"]