#baixando a imagem do jdk 17 no container
FROM openjdk:17
#copia a pasta atual para a pasta /fidelity dentro do container
COPY . /fidelity
#entrando na pasta fidelity e compilando o projeto
WORKDIR /fidelity
RUN ./mvnw package
RUN mv target/*.jar fidelity.jar 
#comando que irá rodar quando subir o container
CMD [ "java","-jar","fidelity.jar"]