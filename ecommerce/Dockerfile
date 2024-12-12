#baixando a imagem do jdk 17 no container
FROM openjdk:17
#copia a pasta atual para a pasta /ecommerce dentro do container
COPY . /ecommerce
#entrando na pasta ecommerce e compilando o projeto
WORKDIR /ecommerce
RUN ./mvnw package
RUN mv target/*.jar ecommerce.jar 
#comando que irá rodar quando subir o container
CMD [ "java","-jar","ecommerce.jar"]