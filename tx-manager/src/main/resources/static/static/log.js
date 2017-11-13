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
            var data = res[p];
            var name = data.split("_")[1];
            name = name.split(".")[0];
            var tr = '<tr><td><a class="log-name" data-data="' + data + '" href="#">' + name + '</a></td></tr>';
            list.append(tr);
        }

    });

    return false;
});


$(document).on("click", ".log-name", function () {

    var file = $(this).attr("data-data");

    var path = $('#model').attr("data-path") + "/" + file;

    http.get('http://127.0.0.1:8899/admin/logs?path=' + path, '加载数据...', function (res) {

        var list = $("#compensate");
        list.empty();
        for (var p in res) {
            var param = res[p];
            var tr =
                '<tr>' +
                '<td><span>' + param.time + '</span></td>' +
                '<td><span>' + param.method + '</span></td>' +
                '<td><span>' + param.executeTime + '</span></td>' +
                '<td><button data-data="' + param.base64 + '" class="btn btn-info">详情</button>&nbsp;&nbsp;<button class="btn btn-success">补偿</button></td>' +
                '</tr>';
            list.append(tr);
        }

    });

    return false;
});