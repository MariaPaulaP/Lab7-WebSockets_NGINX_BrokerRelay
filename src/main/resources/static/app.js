var app = (function () {


    var nombreJugador = "";

    var stompClient = null;
    var gameid = 0;

    var callback_palabraActualizada = function (message) {
        var palabra = message.body;
        $("#palabra h1").text(palabra);
    };
    var callback_setWinner = function (message) {
        var nombreGanador = message.body;
        $("#status  > div:nth-child(1)").text("Estado: Terminado");
        $("#status  > div:nth-child(2)").text("Ganador: " + nombreGanador + ".");
    };

    return {
        loadWord: function () {

            gameid = $("#gameid").val();

            $.get("/hangmangames/" + gameid + "/currentword",
                    function (data) {
                        $("#palabra").html("<h1>" + data + "</h1>");
                        app.wsconnect();
                    }
            ).fail(
                    function (data) {
                        alert(data["responseText"]);
                    }

            );


        }
        ,
        wsconnect: function () {
            
            var socket = new SockJS('/stompendpoint');
            stompClient = Stomp.over(socket);

            stompClient.connect("sstmfolm", "bpP4scrs1wzuOJcIeTetBdHh_SWs3A8B",
                    function (frame) {
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/topic/wupdate.' + gameid, function (eventbody) {
                            callback_palabraActualizada(eventbody);
                        });
                        stompClient.subscribe('/topic/winner.' + gameid, function (eventbody) {
                            callback_setWinner(eventbody);
                        });

                    }
            ,
                    function (error) {
                        console.info("error" + error);
                    }

            , "gmmufdcu");


            /**stompClient.connect({}, function (frame) {
             console.log('Connected: ' + frame);
             
             //lo suscribimos para el estado de la palabra
             stompClient.subscribe('/topic/wupdate.'+gameid, function (eventbody) {
             callback_palabraActualizada(eventbody);
             });
             
             //lo suscribimos para el ganador
             stompClient.subscribe('/topic/winner.'+gameid, function (eventbody) {
             callback_setWinner(eventbody);
             });
             });*/

        },
        sendLetter: function () {
            //no lo dejamos continuar si no tiene el nombre aún
            if (nombreJugador == "") {
                alert("ingresar usuario primero");
                return false;
            }
            if ($("#caracter").val() == "") {

                alert("ingresar letra primero");
                return false;
            }
            var id = gameid;

            var hangmanLetterAttempt = {letter: $("#caracter").val(), username: nombreJugador};

            console.info("Gameid:" + gameid + ",Sending v2:" + JSON.stringify(hangmanLetterAttempt));



            jQuery.ajax({
                url: "/hangmangames/" + id + "/letterattempts",
                type: "POST",
                data: JSON.stringify(hangmanLetterAttempt),
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    console.log(data);
                }
            });


        },
        sendWord: function () {

            var hangmanWordAttempt = {word: $("#adivina").val(), username: nombreJugador};


            var id = gameid;

            jQuery.ajax({
                url: "/hangmangames/" + id + "/wordattempts",
                type: "POST",
                data: JSON.stringify(hangmanWordAttempt),
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function () {
                    //
                }
            });

        },
        /**
         * encargado de pedir el id y enviarlo por REST para recoger datos del usuario (nombre, foto ...)
         * @returns {undefined}
         */

        setIdJugador: function () {
            var id_jugador = $("#playerid").val();

            jQuery.ajax({
                url: "/users/" + id_jugador,
                type: "GET",
                data: null,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    //mostramos el nombre
                    $("#datosjugador > div:nth-child(2)").text(data.name);
                    //guardamos nombre

                    nombreJugador = data.name;
                    //mostramos la img
                    $("#datosjugador img").attr("src", data.photoUrl);
                },
                error: function () {

                    alert("id de usuario incorrecto");
                }
            });
        }

    };

})();