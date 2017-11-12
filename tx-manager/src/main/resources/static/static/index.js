var init = function () {

    http.get('/admin/setting', '加载中', function (res) {
        for (var key in res) {
            var value = res[key];
            $("#" + key).text(value);
        }
    })
}

init();
