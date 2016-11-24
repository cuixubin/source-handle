<%-- 
    Document   : index
    Created on : 2016-10-26, 9:42:49
    Author     : xcyg
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script> 
        <title>Index Page</title>
        <script>
            var goEasy = new GoEasy({ 
                appkey: '5775a420-6472-4410-90fa-d7ced73bc6b0' 
            }); 
            goEasy.subscribe({ 
                channel: 'your_channel', 
                onMessage: function(message){ 
                    alert('Meessage received:'+message.content); 
                } 
            }); 
        </script>
    </head>
    <body>
        <h1>Welcome to Hello World!</h1>
    </body>
</html>
