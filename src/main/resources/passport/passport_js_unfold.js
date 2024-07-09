/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
(function (a) {
    if (typeof define === "function" && define.amd) {
        define(["jquery"], a)
    } else {
        if (typeof exports === "object") {
            a(require("jquery"))
        } else {
            a(jQuery)
        }
    }
}(function (f) {
    var a = /\+/g;

    function d(i) {
        return b.raw ? i : encodeURIComponent(i)
    }

    function g(i) {
        return b.raw ? i : decodeURIComponent(i)
    }

    function h(i) {
        return d(b.json ? JSON.stringify(i) : String(i))
    }

    function c(i) {
        if (i.indexOf('"') === 0) {
            i = i.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, "\\")
        }
        try {
            i = decodeURIComponent(i.replace(a, " "));
            return b.json ? JSON.parse(i) : i
        } catch (j) {
        }
    }

    function e(j, i) {
        var k = b.raw ? j : c(j);
        return f.isFunction(i) ? i(k) : k
    }

    var b = f.cookie = function (q, p, v) {
        if (p !== undefined && !f.isFunction(p)) {
            v = f.extend({}, b.defaults, v);
            if (typeof v.expires === "number") {
                var r = v.expires, u = v.expires = new Date();
                u.setTime(+u + r * 86400000)
            }
            return (document.cookie = [d(q), "=", h(p), v.expires ? "; expires=" + v.expires.toUTCString() : "", v.path ? "; path=" + v.path : "", v.domain ? "; domain=" + v.domain : "", v.secure ? "; secure" : ""].join(""))
        }
        var w = q ? undefined : {};
        var s = document.cookie ? document.cookie.split("; ") : [];
        for (var o = 0, m = s.length; o < m; o++) {
            var n = s[o].split("=");
            var j = g(n.shift());
            var k = n.join("=");
            if (q && q === j) {
                w = e(k, p);
                break
            }
            if (!q && (k = e(k)) !== undefined) {
                w[j] = k
            }
        }
        return w
    };
    b.defaults = {};
    f.removeCookie = function (j, i) {
        if (f.cookie(j) === undefined) {
            return false
        }
        f.cookie(j, "", f.extend({}, i, {expires: -1}));
        return !f.cookie(j)
    }
}));
var uampassport = {};
(function () {
    $(document).ready(function () {
        var a = $.ajax;
        var d = c()["redirect"] || passport_okPage;
        var b = c()["isFromExtened"];
        uampassport.checkLogin = function () {
            var e = $.cookie("tk");
            if (e == null || e == undefined || e == "") {
                a({
                    type: "POST",
                    url: passport_authuam,
                    data: {appid: passport_appId},
                    xhrFields: {withCredentials: true},
                    dataType: "json",
                    success: function (f) {
                        if (f.result_code == "0") {
                            var g = f.newapptk || f.apptk;
                            uampassport.uampassport(g)
                        } else {
                            if (b == 1) {
                                window.location.href = d;
                                return
                            }
                            window.location.href = ctx + passport_loginPage
                        }
                    },
                    error: function () {
                    }
                })
            } else {
                uampassport.uampassport(e)
            }
        };
        uampassport.uampassport = function (e) {
            a({
                type: "POST", url: ctx + passport_authclient, data: {tk: e}, datatype: "json", success: function (f) {
                    if (f.result_code == 0) {
                        window.location.href = d
                    } else {
                        if (b == 1) {
                            window.location.href = d;
                            return
                        }
                        window.location.href = ctx + passport_loginPage
                    }
                }, error: function () {
                }
            })
        };

        function c() {
            var h = [], g;
            var e = window.location.href.slice(window.location.href.indexOf("?") + 1).split("&");
            for (var f = 0; f < e.length; f++) {
                g = e[f].split("=");
                h.push(g[0]);
                h[g[0]] = g[1]
            }
            return h
        }
    })
})();
