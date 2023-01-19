<html>
    <head>
        <title>Horário Servidor</title>
    </head>
    <body>
        <h1>
			<%  
				java.util.Date agora = new java.util.Date(System.currentTimeMillis());  
		   		out.println(agora.toString());  
		%>	
        </h1>
    </body>
</html>  