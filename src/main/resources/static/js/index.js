function showAndHide() {
    $('#chatArea').toggleClass('show');
    $('#startArea').toggleClass('hide')
}

function webSocket() {
    showAndHide()
    $("#content").html("");

    let ws = new WebSocket("ws://localhost:8080/chat");
    let isOpen = true;

    ws.onmessage = function (ev) {
        let receiveStr = "<span id='mes_left'>" + ev.data + "</span></br>";
        $("#content").append(receiveStr);
    };

    ws.onclose = function (ev) {
        isOpen = false;
    };

    $("#submit").click(function () {
        if (isOpen) {
            ws.send($("#input_text").val());
            let sendStr = "<span id='mes_right'>" + $("#input_text").val() + "</span></br>";
            $("#content").append(sendStr);
            $("#input_text").val("");
        }
    })

    $("#exit").click(function () {
        ws.close();
        history.go(0)
    })
}

$("#start").click(function () {
    $.ajax({
        url: "/sendSecret",
        data: {"secret": $('#input_Secret').val()},
        type: "POST",
        dataType: "text",
        cache: false,
        async: false,
        success: function () {
            $('#input_Secret').val("")
        }
    });
    webSocket();
});
