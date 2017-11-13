var init = function () {
    http.get('/admin/modelList', '加载数据...', function (res) {

        var list = $("#list");
        list.empty();
        for (var p in res) {
            var tr = '<tr><td><a href="log.html?model=' + res[p] + '">' + res[p] + '</a></td></tr>';
            list.append(tr);
        }

    });
};


init();