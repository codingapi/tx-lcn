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
                '<td><button  data-data="' + param.base64 + '" class="btn btn-info detail">详情</button>&nbsp;&nbsp;<button class="btn btn-success">补偿</button></td>' +
                '</tr>';
            list.append(tr);
        }

    });

    return false;
});

$(document).on("click", ".detail", function () {

    var base64 = $(this).attr("data-data");


    var unicode = BASE64.decoder(base64);

    var str = '';
    for (var i = 0, len = unicode.length; i < len; ++i) {
        str += String.fromCharCode(unicode[i]);
    }


    var obj = JSON.parse(str);


    $("#body").empty();

    $('#body').append('<tr>\n' +
        '                            <th>\n' +
        '                                模块名称\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-model"></span>\n' +
        '                            </td>\n' +
        '\n' +
        '                            <th>\n' +
        '                                模块地址\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-address"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                唯一标示\n' +
        '                            </th>\n' +
        '                            <td colspan="3">\n' +
        '                                <span id="data-uniqueKey"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                记录时间\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-currentTime"></span>\n' +
        '                            </td>\n' +
        '\n' +
        '                            <th>\n' +
        '                                执行时间\n' +
        '                            </th>\n' +
        '                            <td >\n' +
        '                                <span id="data-time"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                启动方法\n' +
        '                            </th>\n' +
        '                            <td colspan="3">\n' +
        '                                <span id="data-methodStr"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                事务组Id\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-groupId"></span>\n' +
        '                            </td>\n' +
        '                            <th>\n' +
        '                                完成状态\n' +
        '                            </th>\n' +
        '                            <td >\n' +
        '                                <span id="data-hasOver"></span>\n' +
        '                            </td>\n' +
        '                        </tr>');

    $("#data-address").text(obj["address"]);
    $("#data-currentTime").text(obj["currentTime"]);
    $("#data-model").text(obj["model"]);
    $("#data-model").text(obj["model"]);
    $("#data-time").text(obj["time"]);
    $("#data-methodStr").text(obj["methodStr"]);
    $("#data-uniqueKey").text(obj["uniqueKey"]);

    $("#data-hasOver").text(obj["txGroup"]["hasOver"]);
    $("#data-groupId").text(obj["txGroup"]["groupId"]);

    $("#data-startTime").text(obj["txGroup"]["startTime"]);
    $("#data-state").text(obj["txGroup"]["state"]);

    var list = obj["txGroup"]["list"];

    for (var index in list) {
        var p = list[index];
        var notify = p["notify"];
        var model = p["modelName"];
        var tr = '<tr><th>事务状态</th><td><span>' + notify + '</span><th>模块名称</th><td><span>' + model + '</span></td></tr>';
        $("#body").append(tr);
    }


    $('#show-modal').modal();

    return false;
});
