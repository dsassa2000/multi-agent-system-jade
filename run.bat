mkdir bin\de\unihildesheim\iis\jadedemo && javac -cp lib\jade.jar src\de\unihildesheim\iis\jadedemo\*.java && COPY src\de\unihildesheim\iis\jadedemo\*.class bin\de\unihildesheim\iis\jadedemo\ && java -cp lib\jade.jar;bin; de/unihildesheim/iis/jadedemo/ContainerController