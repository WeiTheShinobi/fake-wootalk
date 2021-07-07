
function showAndHide() {
    $('#chatArea').toggleClass('show');
    $('#start').toggleClass('hide')
}


$("#start").click(function () {
    showAndHide()
    $("#content").html("");

    let ws = new WebSocket("ws://localhost:8080/chat");

    ws.onmessage = function (ev) {
        let receiveStr = "<span id='mes_left'>"+ ev.data +"</span></br>";
        $("#content").append(receiveStr);
    };

    ws.onclose = function (ev) {
        $("#test").text("left");
    };

    $("#submit").click(function () {
        ws.send($("#input_text").val());
        let sendStr = "<span id='mes_right'>"+ $("#input_text").val() +"</span></br>";
        $("#content").append(sendStr);
        $("#input_text").val("");
    })

    $("#exit").click(function () {
        ws.close();
        showAndHide();
        return
    })
});
