var init = function () {


    http.get('/admin/modelList', '加载数据...', function (res) {

        var list = $("#list");

        list.empty();
        for (var p in res) {
            var v = res[p];
            var name = v['name'].split(':')[1];
            var tr = '<tr>' +
                '<td><a class="model-name" href="#">' + name + '</a></td>' +
                '<td><span>' + v['count'] + '</span></td>' +
                '</tr>';
            list.append(tr);
        }

    });
};


init();


$(document).on("click", ".model-name", function () {
    var model = $(this).text();

    $('#model').attr("data-model", model);

    http.get('/admin/modelTimes?model=' + model, '加载数据...', function (res) {

        var list = $("#logs");
        list.empty();
        for (var p in res) {
            var data = res[p];
            var name = data.split(":")[0];
            var tr = '<tr><td><a class="model-time" data-data="' + data + '" href="#">' + name + '</a></td></tr>';
            list.append(tr);
        }

    });

    return false;
});


$(document).on("click", ".model-time", function () {

    var file = $(this).attr("data-data");

    var path = $('#model').attr("data-model") + ":" + file;

    http.get('/admin/modelInfos?path=' + path, '加载数据...', function (res) {

        var list = $("#compensate");
        list.empty();
        for (var p in res) {
            var param = res[p];
            var tr =
                '<tr>' +
                '<td><span>' + param.time + '</span></td>' +
                '<td><span>' + param.method + '</span></td>' +
                '<td><span>' + param.executeTime + '</span></td>' +
                '<td><button  data-data="' + param.base64 + '" class="btn btn-info detail">详情</button>' +
                '&nbsp;&nbsp;' +
                '<button data-data="' + param.key + '" class="btn btn-success compensate">补偿</button>' +
                '&nbsp;&nbsp;' +
                '<button data-data="' + param.key + '" class="btn btn-danger delete">删除</button></td>' +
                '</tr>';
            list.append(tr);
        }

    });

    return false;
});


var confim = function(title,callback){
    var button = $('#show-alert').find("button[data-event=button]");
    button.unbind();


    $("#content").text(title);

    button.click(function(){
        if(callback!=null){
            callback();
            reloadPage();
        }
        return true;
    })
    $('#show-alert').modal();
}

$(document).on("click", ".delete", function () {

    var path = $(this).attr("data-data");

    var tag = $(this).parent().parent();


    confim("确认要删除吗？",function(){

        http.get('/admin/delCompensate?path=' + path, '加载数据...', function (res) {
            if (res) {
                hint('删除成功.');
                tag.remove();

            } else {
                hint('删除失败.');
            }
        });
    });

    return false;

});

var reloadPage = function(){
    var compensate =  $("#compensate");
    var lg = compensate.find("tr").length;
    if(lg==1){
        location.reload();
    }
}


$(document).on("click", ".compensate", function () {

    var path = $(this).attr("data-data");

    var tag = $(this).parent().parent();

    confim("确认要补偿吗？",function(){

        http.get('/admin/compensate?path=' + path, '加载数据...', function (res) {
            if (res) {
                hint('补偿成功.');
                tag.remove();

            } else {
                hint('补偿失败.');
            }

        });
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
        '                                发起方模块\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-model"></span>\n' +
        '                            </td>\n' +
        '\n' +
        '                            <th>\n' +
        '                                发起方地址\n' +
        '                            </th>\n' +
        '                            <td>\n' +
        '                                <span id="data-address"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                发起方标示\n' +
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
        '                                执行时间(毫秒)\n' +
        '                            </th>\n' +
        '                            <td >\n' +
        '                                <span id="data-time"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <th>\n' +
        '                                发起方执行方法\n' +
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
        '                                完成状态</br>(1:已结束 0:未结束)\n' +
        '                            </th>\n' +
        '                            <td >\n' +
        '                                <span id="data-hasOver"></span>\n' +
        '                            </td>\n' +
        '                        </tr>');

    $("#data-address").text(obj["address"]);
    var currentTime = obj["currentTime"];
    var date = new Date(currentTime);

    $("#data-currentTime").text(date.format('yyyy-MM-dd h:m:s'));

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
        var model = p["model"];
        var modelIpAddress = p["modelIpAddress"];
        var methodStr = p["methodStr"];

        var uniqueKey = p["uniqueKey"];

        var tr1 =
            '<tr>' +
            '<th>模块名称</th><td><span>' + model + '</span></td>' +
            '<th>模块地址</th><td><span>' + modelIpAddress + '</span></td>' +
            '</tr>';

        $("#body").append(tr1);

        var tr2 =
            '<tr>' +
            '<th>唯一标示</th><td><span>' + uniqueKey + '</span></td>' +
            '<th>是否提交</th><td><span>' + notify + '</span></td>' +
            '</tr>';

        $("#body").append(tr2);

        var tr3 =
            '<tr>' +
            '<th>执行方法</th><td colspan="3"><span>' + methodStr + '</span></td>' +
            '</tr>';

        $("#body").append(tr3);

    }


    $('#show-modal').modal();

    return false;
});
