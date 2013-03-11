Cloud Stack Client API
===========

Description
---------------

Lib para acesso via linecommnd da api do CloudStack

Informações e Recomendações
---------------

Cloudstack-client é um projeto em desenvolvimento.
 
 * Precisando uma funcionalidade o codigo está aberto para implementar.
 
 * Ao criar um funcionalidade nova favor utilizar o TODO.

Requirements
-------------------

 * Java =< 6.0
 * Maven2 

Installation
--------------

    git@codebasehq.com:abril/aapg/cloudstack-client.git
    cd cloudstack-client

    # Use mvn para installar as dependencias
    mvn install

Running cloudstack-client
------------------------------

Start aplicação:

		mvn package
		cd target
		configurar o arquivo cloudstack-client.properties com suas permissões
 		java -jar cloudstack-client.jar ${1} ${2} 
		// onde:
		// 1  - Tipo serviço  
		// 2 ... n - Parametros do serviço 

    
Contributors
------------

 * Everton Amaral <everton.amaral@abril.com.br>

