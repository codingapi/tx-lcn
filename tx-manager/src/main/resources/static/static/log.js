var init = function () {
    var name = getRequestParam('model');
    $("#model").text(name);

    http.get('http://127.0.0.1:8899/admin/childModel?model=' + name, '加载数据...', function (res) {

        var list = $("#list");
        list.empty();
        for (var p in res) {
            var tr = '<tr><td><a class="model-name" href="#">' + res[p] + '</a></td></tr>';
            list.append(tr);
        }

    });
};


init();


$(document).on("click", ".model-name", function () {
    var txt = $(this).text();

    var path = $('#model').text() + "/" + txt;

    $('#model').attr("data-path", path);

    http.get('http://127.0.0.1:8899/admin/logFile?path=' + path, '加载数据...', function (res) {

        var list = $("#logs");
        list.empty();
        for (var p in res) {
            var tr = '<tr><td><a class="log-name" href="#">' + res[p] + '</a></td></tr>';
            list.append(tr);
        }

    });

    return false;
});


$(document).on("click", ".log-name", function () {

    var file = $(this).text();

    var path = $('#model').attr("data-path") + "/" + file;

    http.get('http://127.0.0.1:8899/admin/logs?path=' + path, '加载数据...', function (res) {

        var list = $("#compensate");
        list.empty();
        for (var p in res) {
            var param = res[p];
            var tr = '<tr><td><a href="#">' + param.time + '</a></td></tr>';
            list.append(tr);
        }

    });

    return false;
});