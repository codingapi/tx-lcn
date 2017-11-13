var init = function () {


    http.get('/admin/onlines', '加载数据...', function (res) {

        var list = $("#list");
        list.empty();
        for (var p in res) {
            var obj = res[p];
            var tr =
                '<tr>' +
                '<td>' + obj.model + '</td>' +
                '<td>' + obj.uniqueKey + '</td>' +
                '<td>' + obj.ipAddress + '</td>' +
                '<td>' + obj.channelName + '</td>' +
                '</tr>';
            list.append(tr);
        }

    });
};


init();

