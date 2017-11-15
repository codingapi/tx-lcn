/**
 * Created by yuliang on 2015/5/12.
 */
function hint(msg, duration) {
    duration = isNaN(duration) ? 3000 : duration;
    var m = document.createElement('div');
    m.innerHTML = msg;
    m.style.cssText = "width:60%; min-width:150px; background:black; opacity:0.5; height:40px; color:#fff; line-height:40px; text-align:center; border-radius:5px; position:fixed; top:90%; left:20%; z-index:99999999; font-weight:bold;";
    document.body.appendChild(m);
    setTimeout(function () {
        var d = 0.5;
        m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
        m.style.opacity = '0';
        setTimeout(function () {
            document.body.removeChild(m)
        }, d * 1000);
    }, duration);
}

Date.prototype.format = function(format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

var _st = window.setTimeout;
//fRef 是test函数,mDelay是时间
window.setTimeout = function (fRef, mDelay) {
    if (typeof fRef == 'function') {
        var argu = Array.prototype.slice.call(arguments, 2);
        var f = (
            function () {
                fRef.apply(null, argu);
            });
        return _st(f, mDelay);
    }
    return _st(fRef, mDelay);
}


function getRequestParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}