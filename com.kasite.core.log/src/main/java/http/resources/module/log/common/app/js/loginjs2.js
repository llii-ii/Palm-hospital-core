! function (t) {
    function n(a) {
        if (e[a]) return e[a].exports;
        var o = e[a] = {
            exports: {},
            id: a,
            loaded: !1
        };
        return t[a].call(o.exports, o, o.exports, n), o.loaded = !0, o.exports
    }
    var e = {};
    return n.m = t, n.c = e, n.p = "/build/", n(0)
}([

    function (t, n, e) {
        "use strict";

        function a() {
            var t = {
                initPosition: function () {}, start: function () {}, pause: function () {}
            };
            indexBanner_isIE9 || (t = o({
                container: "[data-group][data-group-open]",
                moveX: -25,
                moveY: -20,
                throttleTime: 60
            }));
            var n = $("[data-hover-container]"),
                e = n.find("[data-group]"),
                a = $("#J_topbar_2016"),
                i = r({
                    start: 0,
                    container: n,
                    group: "[data-group]",
                    parent: n.parent(),
                    debug: !1,
                    duration: 1.25,
                    autoPlayTime: AUTO_PLAY_TIME,
                    effect: "ease-out",
                    center: indexBanner_isIE9 ? "center-ie" : "center",
                    bottom: indexBanner_isIE9 ? "bottom-ie" : "bottom",
                    darkColor: DARK_BG,
                    lightColor: LIGHT_BG,
                    callback: function (n, o) {
                        t.initPosition(e.eq(n).find("[data-base-layer]")), "light" == o ? a.trigger("change", "white") : "dark" == o ? a.trigger("change", "dark") : console.error("color\u65e0\u6548", o)
                    }
                });
            ! function () {
                i.auto.start(), n.hover(function () {
                    i.auto.pause()
                }, function () {
                    i.auto.start()
                })
            }(),
            function () {
                var n;
                $(window).on("scroll", function () {
                    t.pause(), n && clearTimeout(n), n = setTimeout(function () {
                        t.start()
                    }, 200)
                })
            }()
        }
        e(1), e(2);
        var o = e(3),
            r = e(5);
        new a, t.exports = a
    },
    function (t, n) {},
    function (t, n) {
        Array.prototype.forEach || (Array.prototype.forEach = function (t, n) {
            var e, a;
            if (null == this) throw new TypeError("this is null or not defined");
            var o = Object(this),
                r = o.length >>> 0;
            if ("function" != typeof t) throw new TypeError(t + " is not a function");
            for (arguments.length > 1 && (e = n), a = 0; r > a;) {
                var i;
                a in o && (i = o[a], t.call(e, i, a, o)), a++
            }
        })
    },
    function (t, n, e) {
        var a = e(4);
        t.exports = function (t) {
            var n = {
                container: void 0,
                layer: [],
                transition: {},
                w: $(window).width(),
                h: $(window).height(),
                throttleTime: 100,
                moveTime: 1250,
                autoTime: 5e3,
                pause: !1
            };
            $.extend(n, t);
            var e = function (t) {
                    var e = (t.pageX / n.w - .5) * n.moveX || 10,
                        a = -(t.pageY / n.h - .5) * n.moveY || 10;
                    return {
                        moveX: e,
                        moveY: a
                    }
                },
                o = function (t) {
                    var e = "translateZ(" + t.zIndex + "px)",
                        a = {
                            "-webkit-transform": e,
                            "-moz-transform": e,
                            "-ms-transform": e,
                            "-o-transform": e,
                            transform: e
                        };
                    $.extend(a, n.transition), t.selector.css(a).addClass("no-transition")
                },
                r = function (t) {
                    var e = "rotateX(" + t.moveY + "deg) rotateY(" + t.moveX + "deg) ",
                        a = {
                            "-webkit-transform": e,
                            "-moz-transform": e,
                            "-ms-transform": e,
                            "-o-transform": e,
                            transform: e
                        };
                    return $.extend(a, n.transition), a
                },
                i = function () {
                    var t = a(function (t, n) {
                        var a = e(t);
                        $(n).css(r(a))
                    }, n.throttleTime);
                    n.container.each(function (e, a) {
                        $(a).on("mousemove", function (e) {
                            n.pause || t(e, a)
                        }), $(a).siblings().on("mousemove", function (e) {
                            n.pause || t(e, a)
                        }), $(a).parents("[data-group]").find(".left-header").on("mousemove", function (e) {
                            n.pause || t(e, a)
                        })
                    })
                },
                s = function () {
                    {
                        var t = n.container.find(".layer");
                        t.outerWidth()
                    }
                    t.each(function (t, e) {
                        var a = $(e);
                        void 0 == a.data("ignore") ? n.layer.push({
                            selector: a,
                            zIndex: a.data("zindex") || 0,
                            offset: a.data("offset") || 0,
                            offsetX: a.data("offsetx") || 0,
                            offsetY: a.data("offsetx") || 0
                        }) : a.css("z-index", 100)
                    })
                },
                u = function (t) {
                    $(t).css(r({
                        moveX: 0,
                        moveY: 0
                    }))
                },
                c = function () {
                    n.container = $(n.container).find("[data-base-layer]"), s(), n.layer.forEach(function (t) {
                        o(t)
                    }), i()
                };
            return c(), {
                initPosition: u,
                start: function () {
                    n.pause = !1
                }, pause: function () {
                    n.pause = !0
                }
            }
        }
    },
    function (t, n) {
        t.exports = function (t, n, e) {
            var a, o, r, i = null,
                s = 0;
            e || (e = {});
            var u = function () {
                s = e.leading === !1 ? 0 : Date.now(), i = null, r = t.apply(a, o), i || (a = o = null)
            };
            return function () {
                var c = Date.now();
                s || e.leading !== !1 || (s = c);
                var l = n - (c - s);
                return a = this, o = arguments, 0 >= l || l > n ? (i && (clearTimeout(i), i = null), s = c, r = t.apply(a, o), i || (a = o = null)) : i || e.trailing === !1 || (i = setTimeout(u, l)), r
            }
        }
    },
    function (t, n) {
        t.exports = function (t) {
            var n = {
                container: t.container,
                debug: !1,
                nowIndex: t.start,
                moveTime: 100 * (t.duration || 1.25),
                previous: 0,
                center: "center",
                bottom: "bottom",
                autoPlayTime: 5e3,
                auto: {},
                darkColor: "#24282C",
                lightColor: "#fff",
                callback: function () {}
            };
            $.extend(n, t), n.group = n.container.find(n.group), n.step = n.group.outerHeight(), n.num = n.group.length, n.group.each(function (t, e) {
                $(e).addClass(n.bottom)
            });
            var e = function (t) {
                    for (var e = $('<ul class="banner-tab"></ul>'), a = function (t) {
                        var e = n.start == t ? "active" : "";
                        return '<li class="banner-tab-li ' + e + '" data-index="' + t + '"></li>'
                    }, o = [], r = 0; t > r; r++) o.push(a(r));
                    e.append(o.join("")), e.css({
                        visibility: "hidden"
                    }), n.parent.append(e), e.css({
                        "margin-left": -e.width() / 2,
                        visibility: "visible"
                    }), n.ul = e
                },
                a = function (t) {
                    void 0 != t && n.nowIndex != t && (i(t), n.nowIndex = t, n.ul.find("li").eq(t).addClass("active").siblings().removeClass("active"))
                },
                o = function () {
                    n.ul.hover(function () {
                        n.auto.pause()
                    }), n.ul.on("click", function (t) {
                        var e = $(t.target),
                            a = e.data("index");
                        void 0 != a && n.nowIndex != a && (i(a), n.nowIndex = a, e.addClass("active").siblings().removeClass("active"))
                    })
                },
                r = function (t) {
                    t || (t = "dark"), n.parent.removeClass("dark light").addClass(t).css({
                        background: "dark" == t ? n.darkColor : n.lightColor
                    })
                },
                i = function (t) {
                    var e = "up";
                    t < n.nowIndex && (e = "down");
                    var a = n.group.eq(n.nowIndex),
                        o = n.group.eq(t);
                    n.group.removeClass("animating-enter-up animating-enter-down"), a.addClass(n.bottom).removeClass(n.center), "up" == e ? o.addClass(n.center + " animating-enter-up").removeClass(n.bottom) : o.addClass(n.center + " animating-enter-down").removeClass(n.bottom);
                    var i = o.data("active");
                    r(i), n.callback(t, i), n.ul.removeClass("dark light").addClass(o.data("active"))
                },
                s = function (t) {
                    return "[data-groupindex=" + t + "]"
                },
                u = function () {
                    r();
                    var t = n.container.find(s(n.start)).eq(0);
                    t.attr("class", n.center + " animating-enter-up");
                    var a = t.data("active");
                    n.callback(n.start, a), n.num > 1 && (e(n.num), o(), n.ul.addClass(a))
                },
                c = function (t) {
                    return (t + 1) % n.num
                };
            return n.auto = {
                timer: 0,
                start: function () {
                    var t = this;
                    clearInterval(t.timer), t.timer = setInterval(function () {
                        var t = c(n.nowIndex);
                        a(t)
                    }, n.autoPlayTime)
                }, pause: function () {
                    clearInterval(this.timer)
                }
            }, u(), {
                auto: n.auto
            }
        }
    }
]);
! function (n) {
    function a(t) {
        if (i[t]) return i[t].exports;
        var e = i[t] = {
            exports: {},
            id: t,
            loaded: !1
        };
        return n[t].call(e.exports, e, e.exports, a), e.loaded = !0, e.exports
    }
    var i = {};
    return a.m = n, a.c = i, a.p = "/build/", a(0)
}([

    function (n, a, i) {
        "use strict";

        function t() {
            e({
                container: ".banner-bottom-container",
                distance: 175,
                isIE8: indexBanner_isIE8
            })
        }
        i(1);
        var e = i(2);
        new t, n.exports = t
    },
    function (n, a) {},
    function (n, a) {
        function i(n) {
            function a(n) {
                var a = $(n).find("[data-image]"),
                    i = a.data("ie8img");
                a.css({
                    "background-image": 'url("' + i + '")'
                })
            }

            function i(a) {
                function i() {
                    r.css({
                        "background-position": "0 -" + m * u + "px"
                    })
                }

                function t() {
                    u++, s > u ? (i(), o = requestAnimationFrame(t)) : cancelAnimationFrame(o)
                }

                function e() {
                    u--, u >= 0 ? (i(), o = requestAnimationFrame(e)) : cancelAnimationFrame(o)
                }
                var o, r = $(a).find("[data-image]"),
                    c = r.data("url"),
                    u = 0,
                    s = r.data("frame"),
                    m = n.distance;
                r.css({
                    "background-image": 'url("' + c + '")'
                }), $(a).hover(function () {
                    cancelAnimationFrame(o), o = requestAnimationFrame(t)
                }, function () {
                    cancelAnimationFrame(o), o = requestAnimationFrame(e)
                })
            }
            $(n.container + " [data-icon-animate]").each(function () {
                n.isIE8 ? a(this) : i(this)
            })
        }
        n.exports = i
    }
]);
! function (e) {
    function u(t) {
        if (o[t]) return o[t].exports;
        var i = o[t] = {
            exports: {},
            id: t,
            loaded: !1
        };
        return e[t].call(i.exports, i, i.exports, u), i.loaded = !0, i.exports
    }
    var o = {};
    return u.m = e, u.c = o, u.p = "/build/", u(0)
}([

    function (e, u, o) {
        "use strict";

        function t() {
            $(".card-area .card-item").on("mouseenter", function () {
                i.push($(this))
            })
        }
        o(1);
        var i = o(2);
        new t, e.exports = t
    },
    function (e, u) {},
    function (e, u) {
        e.exports = {
            _queue: [],
            gap: !1,
            timer: void 0,
            _lock: !1,
            _timeout: void 0,
            duration: 310,
            run: function () {
                var e = this;
                if (e._queue.length) {
                    var u = e._queue.pop();
                    u.fn(u.param), e._queue = []
                }
            }, lock: function () {
                var e = this;
                e._lock = !0, setTimeout(function () {
                    e.unlock(), e.logic()
                }, e.duration)
            }, unlock: function () {
                this._lock = !1
            }, logic: function () {
                var e = this;
                if (e._lock);
                else if (e._queue.length) {
                    var u = e._queue.pop();
                    u.addClass("active").siblings().removeClass("active"), e._queue = [], e.lock()
                }
            }, push: function (e) {
                var u = this;
                u._queue.push(e), u.logic()
            }
        }
    }
]);
! function (n) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var i = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return n[o].call(i.exports, i, i.exports, t), i.loaded = !0, i.exports
    }
    var e = {};
    return t.m = n, t.c = e, t.p = "/build/", t(0)
}([

    function (n, t, e) {
        "use strict";

        function o() {
            i({
                container: "ali-main-productor-other",
                selector: "fade",
                between: -60
            })
        }
        e(1), e(2);
        var i = e(3);
        new o, n.exports = o
    },
    function (n, t) {},
    function (n, t) {
        ! function () {
            for (var n = 0, t = ["ms", "moz", "webkit", "o"], e = 0; e < t.length && !window.requestAnimationFrame; ++e) window.requestAnimationFrame = window[t[e] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[t[e] + "CancelAnimationFrame"] || window[t[e] + "CancelRequestAnimationFrame"];
            window.requestAnimationFrame || (window.requestAnimationFrame = function (t, e) {
                var o = (new Date).getTime(),
                    i = Math.max(0, 16 - (o - n)),
                    r = window.setTimeout(function () {
                        t(o + i)
                    }, i);
                return n = o + i, r
            }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                clearTimeout(n)
            }), Function.prototype.bind || (Function.prototype.bind = function (n) {
                if ("function" != typeof this) throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                var t = Array.prototype.slice.call(arguments, 1),
                    e = this,
                    o = function () {},
                    i = function () {
                        return e.apply(this instanceof o && n ? this : n, t.concat(Array.prototype.slice.call(arguments)))
                    };
                return o.prototype = this.prototype, i.prototype = new o, i
            })
        }()
    },
    function (n, t) {
        function e(n) {
            function t() {
                document.getElementById(n.container).style.visibility = "visible", r.css("visibility", "visible"), a.each(function () {
                    $(this).addClass($(this).data(n.selector))
                })
            }

            function e() {
                l > u ? (u++, o.scrollTop() + i >= w && (t(), o.off("scroll", e))) : u = 0
            }
            if (!$) return console.error("need jQuery");
            var o = $(window),
                i = o.height(),
                r = $("#" + n.container),
                a = r.find("[data-" + n.selector + "]"),
                c = r.offset().top,
                s = n.between || 150,
                u = 0,
                l = 30,
                w = c + s;
            o.scrollTop() + i < w ? o.on("scroll", e) : t()
        }
        n.exports = e
    }
]);
! function (t) {
    function i(n) {
        if (e[n]) return e[n].exports;
        var s = e[n] = {
            exports: {},
            id: n,
            loaded: !1
        };
        return t[n].call(s.exports, s, s.exports, i), s.loaded = !0, s.exports
    }
    var e = {};
    return i.m = t, i.c = e, i.p = "/build/", i(0)
}([

    function (t, i, e) {
        "use strict";

        function n() {
            var t = new s({
                showSize: 5,
                moveSize: 5
            });
            t.init()
        }
        e(1);
        var s = e(2);
        new n, t.exports = n
    },
    function (t, i) {},
    function (t, i) {
        "use strict";

        function e(t) {
            this.showSize = t.showSize || 5, this.moveSize = t.moveSize || 5, this.currentPos = t.currentPos || 0, this.containerId = t.containerId || "slide-container", this.bodyId = t.bodyId || "slide-body", this.contentId = t.contentId || "slide-content", this.leftBtnId = t.leftBtnId || "left-btn", this.rightBtnId = t.rightBtnId || "right-btn", this.itemClazz = t.itemClazz || "slide-item", this.btnPreClazz = t.btnPreClazz || "btn-bg", this.isRunning = !1
        }
        e.prototype = {
            init: function () {
                this.initDom(), this.initState(), this.bindEvent()
            }, initDom: function () {
                this.container = $("#" + this.containerId), this.content = $("#" + this.contentId), this.size = this.content.find(".slide-item").size();
                var t = this.content.html();
                this.content.append(t).prepend(t), this.body = $("#" + this.bodyId), this.leftBtn = $("#" + this.leftBtnId), this.rightBtn = $("#" + this.rightBtnId)
            }, initState: function () {
                this.itemW = this.container.width() / this.showSize, this.container.height(420 * this.itemW / 288), this.body.height(420 * this.itemW / 288), this.content.width(this.itemW * this.size * 3), this.content.css({
                    left: -(this.itemW * this.size)
                }), this.content.find(".item-img-panel").height(this.content.find(".item-img-panel").width()), this.content.find("." + this.itemClazz).width(this.itemW).height(420 * this.itemW / 288), this.initBtnsState()
            }, initBtnsState: function () {
                this.size <= this.showSize && (this.leftBtn.hide(), this.leftBtn.prev("." + this.btnPreClazz).hide(), this.rightBtn.hide(), this.rightBtn.prev("." + this.btnPreClazz).hide())
            }, bindEvent: function () {
                var t = this;
                $(window).bind("resize.slider", function () {
                    t.initState()
                }), this.leftBtn.bind("click.slider", function () {
                    t.moveToRight()
                }), this.rightBtn.bind("click.slider", function () {
                    t.moveToLeft()
                }), this.container.on("resetevent", function (i, e) {
                    e.reverse(), $.each(e, function (i, e) {
                        var n = t.content.find('.slide-item[data-code="' + e + '"]');
                        n.each(function (i) {
                            $(this).insertBefore(t.content.find(".slide-item").eq(i * t.size))
                        })
                    }), t.content.find(".slide-item").find(".bg").each(function (i) {
                        i % t.size % 2 == 0 ? $(this).hasClass("other-bg") && $(this).removeClass("other-bg") : $(this).hasClass("other-bg") || $(this).addClass("other-bg")
                    })
                })
            }, moveToLeft: function () {
                var t = this;
                if (this.direction = "left", !t.isRunning) {
                    t.isRunning = !0;
                    var i = this.getNextPosition(),
                        e = this.getMoveWidth(i);
                    t.content.stop(!0, !0).animate({
                        left: "-=" + e + "px"
                    }, 350, function () {
                        t.currentPos = i, t.isRunning = !1, t.content.position().left <= -(t.itemW * t.size * 2) && (setTimeout(function () {
                            t.content.css({
                                left: -(t.itemW * t.size)
                            })
                        }, 0), t.currentPos = 0)
                    })
                }
            }, moveToRight: function () {
                var t = this;
                if (this.direction = "right", !t.isRunning) {
                    t.isRunning = !0;
                    var i = this.getNextPosition(),
                        e = this.getMoveWidth(i);
                    t.content.stop(!0, !0).animate({
                        left: "+=" + e + "px"
                    }, 350, function () {
                        t.currentPos = i, t.isRunning = !1, t.content.position().left >= 0 && (setTimeout(function () {
                            t.content.css({
                                left: -(t.itemW * t.size)
                            })
                        }, 0), t.currentPos = 0)
                    })
                }
            }, getMoveWidth: function (t) {
                return "right" === this.direction ? this.currentPos < t ? this.moveSize * this.itemW : (this.currentPos - t) * this.itemW : this.currentPos > t ? this.moveSize * this.itemW : (t - this.currentPos) * this.itemW
            }, getNextPosition: function () {
                return "left" === this.direction ? this.currentPos + this.moveSize + this.showSize < this.size ? this.currentPos + this.moveSize : this.currentPos + this.showSize == this.size ? 0 : this.size - this.showSize : 0 == this.currentPos ? this.size - this.showSize : this.currentPos - this.moveSize > 0 ? this.currentPos - this.moveSize : 0
            }, canScroll: function () {
                var t = this;
                return t.isRunning ? !1 : void 0
            }
        }, t.exports = e
    }
]);
! function (n) {
    function t(e) {
        if (i[e]) return i[e].exports;
        var o = i[e] = {
            exports: {},
            id: e,
            loaded: !1
        };
        return n[e].call(o.exports, o, o.exports, t), o.loaded = !0, o.exports
    }
    var i = {};
    return t.m = n, t.c = i, t.p = "/build/", t(0)
}([

    function (n, t, i) {
        "use strict";

        function e() {
            o({
                container: "#ali-main-market",
                distance: 75,
                isIE8: indexMarket_isIE8
            }), a({
                container: "ali-main-market",
                selector: "fade",
                between: -60
            })
        }
        i(1), i(2);
        var o = i(3),
            a = i(4);
        new e, n.exports = e
    },
    function (n, t) {},
    function (n, t) {
        ! function () {
            for (var n = 0, t = ["ms", "moz", "webkit", "o"], i = 0; i < t.length && !window.requestAnimationFrame; ++i) window.requestAnimationFrame = window[t[i] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[t[i] + "CancelAnimationFrame"] || window[t[i] + "CancelRequestAnimationFrame"];
            window.requestAnimationFrame || (window.requestAnimationFrame = function (t, i) {
                var e = (new Date).getTime(),
                    o = Math.max(0, 16 - (e - n)),
                    a = window.setTimeout(function () {
                        t(e + o)
                    }, o);
                return n = e + o, a
            }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                clearTimeout(n)
            }), Function.prototype.bind || (Function.prototype.bind = function (n) {
                if ("function" != typeof this) throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                var t = Array.prototype.slice.call(arguments, 1),
                    i = this,
                    e = function () {},
                    o = function () {
                        return i.apply(this instanceof e && n ? this : n, t.concat(Array.prototype.slice.call(arguments)))
                    };
                return e.prototype = this.prototype, o.prototype = new e, o
            })
        }()
    },
    function (n, t) {
        function i(n) {
            function t(n) {
                var t = $(n).find("[data-image]"),
                    i = t.data("ie8img");
                t.css({
                    "background-image": 'url("' + i + '")'
                })
            }

            function i(t) {
                function i() {
                    r.css({
                        "background-position": "0 -" + m * s + "px"
                    })
                }

                function e() {
                    s++, u > s ? (i(), a = requestAnimationFrame(e)) : cancelAnimationFrame(a)
                }

                function o() {
                    s--, s >= 0 ? (i(), a = requestAnimationFrame(o)) : cancelAnimationFrame(a)
                }
                var a, r = $(t).find("[data-image]"),
                    c = r.data("url"),
                    s = 0,
                    u = r.data("frame"),
                    m = n.distance;
                r.css({
                    "background-image": 'url("' + c + '")'
                }), $(t).hover(function () {
                    cancelAnimationFrame(a), a = requestAnimationFrame(e)
                }, function () {
                    cancelAnimationFrame(a), a = requestAnimationFrame(o)
                })
            }
            $(n.container + " [data-icon-animate]").each(function () {
                n.isIE8 ? t(this) : i(this)
            })
        }
        n.exports = i
    },
    function (n, t) {
        function i(n) {
            function t() {
                document.getElementById(n.container).style.visibility = "visible", a.css("visibility", "visible"), r.each(function () {
                    $(this).addClass($(this).data(n.selector))
                })
            }

            function i() {
                m > u ? (u++, e.scrollTop() + o >= l && (t(), e.off("scroll", i))) : u = 0
            }
            if (!$) return console.error("need jQuery");
            var e = $(window),
                o = e.height(),
                a = $("#" + n.container),
                r = a.find("[data-" + n.selector + "]"),
                c = a.offset().top,
                s = n.between || 150,
                u = 0,
                m = 30,
                l = c + s;
            e.scrollTop() + o < l ? e.on("scroll", i) : t()
        }
        n.exports = i
    }
]);
! function (r) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var n = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return r[o].call(n.exports, n, n.exports, t), n.loaded = !0, n.exports
    }
    var e = {};
    return t.m = r, t.c = e, t.p = "/build/", t(0)
}([

    function (r, t) {}
]);
! function (n) {
    function t(a) {
        if (e[a]) return e[a].exports;
        var r = e[a] = {
            exports: {},
            id: a,
            loaded: !1
        };
        return n[a].call(r.exports, r, r.exports, t), r.loaded = !0, r.exports
    }
    var e = {};
    return t.m = n, t.c = e, t.p = "/build/", t(0)
}([

    function (n, t, e) {
        "use strict";

        function a() {
            function n() {
                var n = !1,
                    t = window.navigator.msPointerEnabled,
                    e = $("#ali-main-register");
                e.css(n || t ? {
                    "background-image": 'url("' + e.data("ie8img") + '")'
                } : {
                    "background-image": 'url("' + e.data("svg") + '")'
                })
            }

            function t(n) {
                r({
                    speed: 2.5,
                    container: "ali-main-defence",
                    callback: e
                })
            }

            function e() {
                var n = function (n, t, e, a) {
                        return e * ((n = n / a - 1) * n * n * n * n + 1) + t
                    },
                    t = {
                        useEasing: !0,
                        useGrouping: !0,
                        easingFn: n[EASE_FN],
                        separator: ",",
                        decimal: ".",
                        prefix: "",
                        suffix: ""
                    },
                    e = {
                        init: !0,
                        count: {},
                        container: $("#defence-number"),
                        changeWidth: function (n) {
                            var t = n.toString(),
                                e = t.length;
                            this.container.css("width", 19 * e)
                        }, time: 1e3 * NUMBER_DURATION_TIME,
                        initFn: function (n) {
                            for (var a = parseInt(n), r = parseInt(NUMBER_CHANGE_TIME), o = a.toString().length, u = 1, s = 0; o - 1 > s; s++) u *= 10;
                            e.count = new i("defence-number", u, a - NUMBER_LARGE, 0, r, t), e.changeWidth(a), e.count.start(function () {
                                e.init && e.count.update(a)
                            })
                        }, get: function () {
                            var n = this;
                            $.ajax({
                                url: a,
                                dataType: "jsonp",
                                jsonp: "callback",
                                lastData: 0,
                                success: function (t) {
                                    return n.cb(t)
                                }, error: function () {
                                    e.cb({
                                        code: 500
                                    })
                                }
                            })
                        }, cb: function (n) {
                            var t, a = n.code;
                            return 200 != a ? e.lastData ? t = e.lastData : $(".ali-main-defence-wrapper").css({
                                display: "none"
                            }) : t = n.data, e.changeWidth(t), e.init ? (e.init = !1, e.initFn(t)) : e.count.update(t), e.lastData = t, setTimeout(function () {
                                return e.get()
                            }, e.time)
                        }
                    };
                e.get()
            }
            n();
            var a = "https://yundun.console.aliyun.com/common/event/count.json";
            $.ajax({
                url: a,
                dataType: "jsonp",
                jsonp: "callback",
                lastData: 0,
                success: function (n) {
                    200 == n.code && ($(".ali-main-defence-wrapper").css("display", "block"), t(n.data))
                }, error: function () {}
            })
        }
        e(1); {
            var r = e(2),
                i = e(3);
            e(4)
        }
        new a, n.exports = a
    },
    function (n, t) {},
    function (n, t) {
        n.exports = function (n) {
            function t(t) {
                document.getElementById(a.container).style.backgroundPosition = "0 -" + t / n.speed + "px"
            }

            function e() {
                d > f ? (f++, r = i.scrollTop() + o - m, r >= 0 && c > r && (p && (n.callback && n.callback(), p = !1), t(c - r))) : f = 0
            }
            var a = {
                speed: 5
            };
            $.extend(a, n);
            var r, i = $(window),
                o = i.height(),
                u = $("#" + a.container),
                s = u.outerHeight(),
                c = o + s,
                l = u.offset().top,
                f = 0,
                d = 10,
                m = l,
                p = !0;
            e(), i.on("scroll", e)
        }
    },
    function (n, t, e) {
        var a, r;
        ! function (i, o) {
            a = o, r = "function" == typeof a ? a.call(t, e, t, n) : a, !(void 0 !== r && (n.exports = r))
        }(this, function (n, t, e) {
            var a = function (n, t, e, a, r, i) {
                for (var o = 0, u = ["webkit", "moz", "ms", "o"], s = 0; s < u.length && !window.requestAnimationFrame; ++s) window.requestAnimationFrame = window[u[s] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[u[s] + "CancelAnimationFrame"] || window[u[s] + "CancelRequestAnimationFrame"];
                window.requestAnimationFrame || (window.requestAnimationFrame = function (n, t) {
                    var e = (new Date).getTime(),
                        a = Math.max(0, 16 - (e - o)),
                        r = window.setTimeout(function () {
                            n(e + a)
                        }, a);
                    return o = e + a, r
                }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                    clearTimeout(n)
                });
                var c = this;
                c.options = {
                    useEasing: !0,
                    useGrouping: !0,
                    separator: ",",
                    decimal: ".",
                    easingFn: null,
                    formattingFn: null
                };
                for (var l in i) i.hasOwnProperty(l) && (c.options[l] = i[l]);
                "" === c.options.separator && (c.options.useGrouping = !1), c.options.prefix || (c.options.prefix = ""), c.options.suffix || (c.options.suffix = ""), c.d = "string" == typeof n ? document.getElementById(n) : n, c.startVal = Number(t), c.endVal = Number(e), c.countDown = c.startVal > c.endVal, c.frameVal = c.startVal, c.decimals = Math.max(0, a || 0), c.dec = Math.pow(10, c.decimals), c.duration = 1e3 * Number(r) || 2e3, c.formatNumber = function (n) {
                    n = n.toFixed(c.decimals), n += "";
                    var t, e, a, r;
                    if (t = n.split("."), e = t[0], a = t.length > 1 ? c.options.decimal + t[1] : "", r = /(\d+)(\d{3})/, c.options.useGrouping)
                        for (; r.test(e);) e = e.replace(r, "$1" + c.options.separator + "$2");
                    return c.options.prefix + e + a + c.options.suffix
                }, c.easeOutExpo = function (n, t, e, a) {
                    return e * (-Math.pow(2, -10 * n / a) + 1) * 1024 / 1023 + t
                }, c.easingFn = c.options.easingFn ? c.options.easingFn : c.easeOutExpo, c.formattingFn = c.options.formattingFn ? c.options.formattingFn : c.formatNumber, c.version = function () {
                    return "1.7.1"
                }, c.printValue = function (n) {
                    var t = c.formattingFn(n);
                    "INPUT" === c.d.tagName ? this.d.value = t : "text" === c.d.tagName || "tspan" === c.d.tagName ? this.d.textContent = t : this.d.innerHTML = t
                }, c.count = function (n) {
                    c.startTime || (c.startTime = n), c.timestamp = n;
                    var t = n - c.startTime;
                    c.remaining = c.duration - t, c.frameVal = c.options.useEasing ? c.countDown ? c.startVal - c.easingFn(t, 0, c.startVal - c.endVal, c.duration) : c.easingFn(t, c.startVal, c.endVal - c.startVal, c.duration) : c.countDown ? c.startVal - (c.startVal - c.endVal) * (t / c.duration) : c.startVal + (c.endVal - c.startVal) * (t / c.duration), c.frameVal = c.countDown ? c.frameVal < c.endVal ? c.endVal : c.frameVal : c.frameVal > c.endVal ? c.endVal : c.frameVal, c.frameVal = Math.round(c.frameVal * c.dec) / c.dec, c.printValue(c.frameVal), t < c.duration ? c.rAF = requestAnimationFrame(c.count) : c.callback && c.callback()
                }, c.start = function (n) {
                    return c.callback = n, c.rAF = requestAnimationFrame(c.count), !1
                }, c.pauseResume = function () {
                    c.paused ? (c.paused = !1, delete c.startTime, c.duration = c.remaining, c.startVal = c.frameVal, requestAnimationFrame(c.count)) : (c.paused = !0, cancelAnimationFrame(c.rAF))
                }, c.reset = function () {
                    c.paused = !1, delete c.startTime, c.startVal = t, cancelAnimationFrame(c.rAF), c.printValue(c.startVal)
                }, c.update = function (n) {
                    cancelAnimationFrame(c.rAF), c.paused = !1, delete c.startTime, c.startVal = c.frameVal, c.endVal = Number(n), c.countDown = c.startVal > c.endVal, c.rAF = requestAnimationFrame(c.count)
                }, c.printValue(c.startVal)
            };
            return a
        })
    },
    function (n, t) {
        n.exports = {
            easeInQuad: function (n, t, e, a) {
                return e * (n /= a) * n + t
            }, easeOutQuad: function (n, t, e, a) {
                return -e * (n /= a) * (n - 2) + t
            }, easeInOutQuad: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n + t : -e / 2 * (--n * (n - 2) - 1) + t
            }, easeInCubic: function (n, t, e, a) {
                return e * (n /= a) * n * n + t
            }, easeOutCubic: function (n, t, e, a) {
                return e * ((n = n / a - 1) * n * n + 1) + t
            }, easeInOutCubic: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n + t : e / 2 * ((n -= 2) * n * n + 2) + t
            }, easeInQuart: function (n, t, e, a) {
                return e * (n /= a) * n * n * n + t
            }, easeOutQuart: function (n, t, e, a) {
                return -e * ((n = n / a - 1) * n * n * n - 1) + t
            }, easeInOutQuart: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n * n + t : -e / 2 * ((n -= 2) * n * n * n - 2) + t
            }, easeInQuint: function (n, t, e, a) {
                return e * (n /= a) * n * n * n * n + t
            }, easeOutQuint: function (n, t, e, a) {
                return e * ((n = n / a - 1) * n * n * n * n + 1) + t
            }, easeInOutQuint: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n * n * n + t : e / 2 * ((n -= 2) * n * n * n * n + 2) + t
            }, easeInSine: function (n, t, e, a) {
                return -e * Math.cos(n / a * (Math.PI / 2)) + e + t
            }, easeOutSine: function (n, t, e, a) {
                return e * Math.sin(n / a * (Math.PI / 2)) + t
            }, easeInOutSine: function (n, t, e, a) {
                return -e / 2 * (Math.cos(Math.PI * n / a) - 1) + t
            }, easeInExpo: function (n, t, e, a) {
                return 0 == n ? t : e * Math.pow(2, 10 * (n / a - 1)) + t
            }, easeOutExpo: function (n, t, e, a) {
                return n == a ? t + e : e * (-Math.pow(2, -10 * n / a) + 1) + t
            }, easeInOutExpo: function (n, t, e, a) {
                return 0 == n ? t : n == a ? t + e : (n /= a / 2) < 1 ? e / 2 * Math.pow(2, 10 * (n - 1)) + t : e / 2 * (-Math.pow(2, -10 * --n) + 2) + t
            }, easeInCirc: function (n, t, e, a) {
                return -e * (Math.sqrt(1 - (n /= a) * n) - 1) + t
            }, easeOutCirc: function (n, t, e, a) {
                return e * Math.sqrt(1 - (n = n / a - 1) * n) + t
            }, easeInOutCirc: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? -e / 2 * (Math.sqrt(1 - n * n) - 1) + t : e / 2 * (Math.sqrt(1 - (n -= 2) * n) + 1) + t
            }
        }
    }
]);
! function (i) {
    function t(n) {
        if (e[n]) return e[n].exports;
        var o = e[n] = {
            exports: {},
            id: n,
            loaded: !1
        };
        return i[n].call(o.exports, o, o.exports, t), o.loaded = !0, o.exports
    }
    var e = {};
    return t.m = i, t.c = e, t.p = "/build/", t(0)
}([

    function (i, t, e) {
        "use strict";

        function n() {
            if (indexServe_isIE8) {
                var i = $("#serve-img-area").find("img");
                i.attr("src", i.data("ie8img"))
            } else
                for (var t = o({
                    container: "#serve-img-area",
                    delayTime: 2250
                }), e = JSON.parse(serveDots), n = e.length, s = 0; n > s; s++) t.append(e[s])
        }
        e(1);
        var o = e(2);
        new n, i.exports = n
    },
    function (i, t) {},
    function (i, t) {
        i.exports = function (i) {
            var t = $(i.container);
            t.css("position", "relative");
            var e = '<div class="point-area"></div>',
                n = function (i) {
                    return '<p class="point-name">' + i + "</p>"
                },
                o = '<div class="point point-white"></div>',
                s = '<div class="point point-dot"></div><div class="point point-10"></div><div class="point point-40"></div><div class="point point-shadow point-80"></div>',
                p = '<div class="point point-dot"></div><div class="point point-10"></div><div class="point point-70"></div>',
                a = !1;
            return {
                append: function (r) {
                    var d, l = r.width ? r.width : 150,
                        v = $(e),
                        c = $(n(r.name));
                    if (d = "left" == r.position ? {
                        position: "absolute",
                        top: l / 2 - 10,
                        left: l / 2 - 40
                    } : {
                        position: "absolute",
                        top: l / 2 - 10,
                        left: l / 2 + 10
                    }, c.css(d), v.append(c), "white" == r.type) v.append(o);
                    else {
                        if ("blue" != r.type) return console.error("type\u53ea\u53ef\u4ee5\u4e3a[white|blue]");
                        v.append(r.width < 100 ? p : s)
                    }
                    v.css({
                        top: r.top - l / 2,
                        left: r.left - l / 2,
                        position: "absolute",
                        width: l,
                        height: l,
                        visibility: "hidden",
                        opacity: 0
                    });
                    var u = a ? 0 : i.delayTime;
                    a = !a, setTimeout(function () {
                        t.append(v)
                    }, u), setTimeout(function () {
                        v.css({
                            visibility: "visible",
                            opacity: 1
                        })
                    }, i.delayTime + 100)
                }
            }
        }
    }
]);
! function (t) {
    function r(e) {
        if (n[e]) return n[e].exports;
        var o = n[e] = {
            exports: {},
            id: e,
            loaded: !1
        };
        return t[e].call(o.exports, o, o.exports, r), o.loaded = !0, o.exports
    }
    var n = {};
    return r.m = t, r.c = n, r.p = "/build/", r(0)
}([

    function (t, r, n) {
        "use strict";

        function e() {}
        n(1), new e, t.exports = e
    },
    function (t, r) {}
]);
! function (n) {
    function e(o) {
        if (t[o]) return t[o].exports;
        var r = t[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return n[o].call(r.exports, r, r.exports, e), r.loaded = !0, r.exports
    }
    var t = {};
    return e.m = n, e.c = t, e.p = "/build/", e(0)
}([

    function (n, e, t) {
        "use strict";

        function o() {
            function n() {
                var n = !1,
                    e = window.navigator.msPointerEnabled,
                    t = $("#ali-main-defence");
                t.css(n || e ? {
                    "background-image": 'url("' + t.data("ie8img") + '")'
                } : {
                    "background-image": 'url("' + t.data("svg") + '")'
                })
            }
            n(), r({
                speed: 2.5,
                container: "ali-main-register"
            })
        }
        t(1);
        var r = t(2);
        new o, n.exports = o
    },
    function (n, e) {},
    function (n, e) {
        n.exports = function (n) {
            function e(e) {
                document.getElementById(o.container).style.backgroundPosition = "0 -" + e / n.speed + "px"
            }

            function t() {
                f > l ? (l++, r = i.scrollTop() + a - p, r >= 0 && u > r && (g && (n.callback && n.callback(), g = !1), e(u - r))) : l = 0
            }
            var o = {
                speed: 5
            };
            $.extend(o, n);
            var r, i = $(window),
                a = i.height(),
                c = $("#" + o.container),
                s = c.outerHeight(),
                u = a + s,
                d = c.offset().top,
                l = 0,
                f = 10,
                p = d,
                g = !0;
            i.on("scroll", t)
        }
    }
]);
! function (r) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var n = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return r[o].call(n.exports, n, n.exports, t), n.loaded = !0, n.exports
    }
    var e = {};
    return t.m = r, t.c = e, t.p = "/build/", t(0)
}([

    function (r, t) {}
]);
! function (t) {
    function e(o) {
        if (i[o]) return i[o].exports;
        var n = i[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return t[o].call(n.exports, n, n.exports, e), n.loaded = !0, n.exports
    }
    var i = {};
    return e.m = t, e.c = i, e.p = "/build/", e(0)
}([

    function (t, e, i) {
        "use strict";

        function o() {
            var t = i(2),
                e = function (e) {
                    var i = [];
                    e.buttons = [], e.button1 && e.button1.length > 0 && (e.buttons[0] = {}, e.buttons[0].button = e.button1, e.buttons[0].link = e.link1, e.buttons[0].or = e.or1, e.buttons[0].btn_type = e.btn_type1), e.button2 && e.button2.length > 0 && (e.buttons[1] = {}, e.buttons[1].button = e.button2, e.buttons[1].link = e.link2, e.buttons[1].or = e.or2, e.buttons[1].btn_type = e.btn_type2), e.button3 && e.button3.length > 0 && (e.buttons[2] = {}, e.buttons[2].button = e.button3, e.buttons[2].link = e.link3, e.buttons[2].or = e.or3, e.buttons[2].btn_type = e.btn_type3), e.buttons && $.each(e.buttons, function (t, e) {
                        e.button.length > 0 && i.push({
                            value: e.button,
                            callback: function () {
                                window.location.href = e.link
                            }, autofocus: "blue" == e.btn_type
                        })
                    }), t({
                        width: 476,
                        title: e.title,
                        content: e.content,
                        button: i
                    }).showModal()
                },
                o = function (t, i, n) {
                    $.ajax({
                        url: "//promotion.aliyun.com/promotion/coderlottery/lottery.htm",
                        dataType: "jsonp",
                        jsonp: "cback",
                        data: {
                            fc: i[n].fc,
                            umidToken: umidToken,
                            collina: getUA()
                        }
                    }).done(function (s) {
                        if (29 == s.code) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else if (25 == s.code) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else if (i.length - 1 == n) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else o(t, i, n + 1)
                    })
                };
            $(".aliyun-common-double-lottery").each(function () {
                var t = JSON.parse($(this).find("textarea[name=moduleinfo]").val());
                $.each(t, function (t, e) {
                    var i = $(e.id);
                    i.attr({
                        _href: i.attr("href"),
                        "data-spm-click": "gostr=/aliyun;locaid=" + e.spm
                    }).data("config", e.li).css("cursor", "pointer"), i.attr("href", "javascript:;").removeAttr("target"), i.click(function () {
                        o(this, $(this).data("config"), 0)
                    })
                })
            })
        }
        i(1), new o, t.exports = o
    },
    function (t, e) {},
    function (t, e, i) {
        + function (e) {
            i(3);
            var o = i(4),
                n = i(5),
                s = 0,
                a = new Date - 0,
                r = !("minWidth" in e("html")[0].style),
                c = "createTouch" in document && !("onmousemove" in document) || /(iPhone|iPad|iPod)/i.test(navigator.userAgent),
                l = !r && !c,
                u = function (t, i, o) {
                    t.width = t.width || 480;
                    var n = t = t || {};
                    ("string" == typeof t || 1 === t.nodeType) && (t = {
                        content: t,
                        fixed: !c
                    }), t = e.extend(!0, {}, u.defaults, t), t.original = n;
                    var r = t.id = t.id || a + s,
                        d = u.get(r);
                    return d ? d.focus() : (l || (t.fixed = !1), t.quickClose && (t.modal = !0, t.backdropOpacity = 0), e.isArray(t.button) || (t.button = []), void 0 !== i && (t.ok = i), t.ok && t.button.push({
                        id: "ok",
                        value: t.okValue,
                        callback: t.ok,
                        autofocus: !0
                    }), void 0 !== o && (t.cancel = o), t.cancel && t.button.push({
                        id: "cancel",
                        value: t.cancelValue,
                        callback: t.cancel,
                        display: t.cancelDisplay
                    }), u.list[r] = new u.create(t))
                },
                d = function () {};
            d.prototype = o.prototype;
            var h = u.prototype = new d;
            u.create = function (t) {
                var i = this;
                e.extend(this, new o);
                var n = (t.original, e(this.node).html(t.innerHTML)),
                    a = e(this.backdrop);
                return this.options = t, this._popup = n, e.each(t, function (t, e) {
                    "function" == typeof i[t] ? i[t](e) : i[t] = e
                }), t.zIndex && (o.zIndex = t.zIndex), n.attr({
                    "aria-labelledby": this._$("title").attr("id", "title:" + this.id).attr("id"),
                    "aria-describedby": this._$("content").attr("id", "content:" + this.id).attr("id")
                }), this._$("close").css("display", this.cancel === !1 ? "none" : "").attr("title", this.cancelValue).on("click", function (t) {
                    i._trigger("cancel"), t.preventDefault()
                }), this._$("dialog").addClass(this.skin), this._$("body").css("padding", this.padding), t.quickClose && a.on("onmousedown" in document ? "mousedown" : "click", function () {
                    return i._trigger("cancel"), !1
                }), this.addEventListener("show", function () {
                    a.css({
                        opacity: 0,
                        background: t.backdropBackground
                    }).animate({
                        opacity: t.backdropOpacity
                    }, 150)
                }), this._esc = function (t) {
                    var e = t.target,
                        n = e.nodeName,
                        s = /^input|textarea$/i,
                        a = o.current === i,
                        r = t.keyCode;
                    !a || s.test(n) && "button" !== e.type || 27 === r && i._trigger("cancel")
                }, e(document).on("keydown", this._esc), this.addEventListener("remove", function () {
                    e(document).off("keydown", this._esc), delete u.list[this.id]
                }), s++, u.oncreate(this), this
            }, u.create.prototype = h, e.extend(h, {
                content: function (t) {
                    var i = this._$("content");
                    return "object" == typeof t ? (t = e(t), i.empty("").append(t.show()), this.addEventListener("beforeremove", function () {
                        e("body").append(t.hide())
                    })) : i.html(t), this.reset()
                }, type: function (t) {
                    var e = this._$("type"),
                        i = t.text || "\u63d0\u793a\u6587\u5b57",
                        o = '<div class="y-clear">',
                        n = "";
                    switch (t.icon) {
                    case "warning":
                        n = "icon-bang";
                        break;
                    case "error":
                        n = "icon-no3";
                        break;
                    case "success":
                        n = "icon-yes3"
                    }
                    return o += '<span style="' + (t.leftWidth ? t.leftWidth : "") + '" class="type-icon ' + n + '"></span>', o += '<span style="' + (t.rightWidth ? t.rightWidth : "") + '">' + i + "</span></div>", e.html(o).show(), this
                }, title: function (t) {
                    return this._$("title").text(t), this._$("header")[t ? "show" : "hide"](), this
                }, width: function (t) {
                    return this._$("content").css("width", t), this.reset()
                }, height: function (t) {
                    return this._$("content").css("height", t), this.reset()
                }, button: function (t) {
                    t = t || [];
                    var i = this,
                        o = "",
                        n = 0;
                    return this.callbacks = {}, "string" == typeof t ? (o = t, n++) : e.each(t, function (t, s) {
                        var a = s.id = s.id || s.value,
                            r = "";
                        i.callbacks[a] = s.callback, s.display === !1 ? r = ' style="display:none"' : n++, o += '<button type="button" i-id="' + a + '"' + r + (s.disabled ? " disabled" : "") + (s.autofocus ? ' autofocus class="ui-dialog-autofocus"' : "") + ">" + s.value + "</button>", i._$("button").on("click", "[i-id=" + a + "]", function (t) {
                            var o = e(this);
                            o.attr("disabled") || i._trigger(a), t.preventDefault()
                        })
                    }), this._$("button").html(o), this._$("footer")[n ? "show" : "hide"](), this
                }, statusbar: function (t) {
                    return this._$("statusbar").html(t)[t ? "show" : "hide"](), this
                }, _$: function (t) {
                    return this._popup.find("[i=" + t + "]")
                }, _trigger: function (t) {
                    var e = this.callbacks[t];
                    return "function" != typeof e || e.call(this) !== !1 ? this.close().remove() : this
                }
            }), u.oncreate = e.noop, u.getCurrent = function () {
                return o.current
            }, u.get = function (t) {
                return void 0 === t ? u.list : u.list[t]
            }, u.list = {}, u.defaults = n, t.exports = u
        }(jQuery)
    },
    function (t, e) {},
    function (t, e) {
        ! function (e) {
            function i() {
                this.destroyed = !1, this.__popup = e("<div />").css({
                    display: "none",
                    position: "absolute",
                    outline: 0
                }).attr("tabindex", "-1").html(this.innerHTML).appendTo("body"), this.__backdrop = this.__mask = e("<div />").css({
                    opacity: .7,
                    background: "#000"
                }), this.node = this.__popup[0], this.backdrop = this.__backdrop[0], o++
            }
            var o = 0,
                n = !("minWidth" in e("html")[0].style),
                s = !n;
            e.extend(i.prototype, {
                node: null,
                backdrop: null,
                fixed: !1,
                destroyed: !0,
                open: !1,
                returnValue: "",
                autofocus: !0,
                align: "bottom left",
                innerHTML: "",
                className: "dbl-popup",
                show: function (t) {
                    if (this.destroyed) return this;
                    var o = this.__popup,
                        a = this.__backdrop;
                    if (this.__activeElement = this.__getActive(), this.open = !0, this.follow = t || this.follow, !this.__ready) {
                        if (o.addClass(this.className).attr("role", this.modal ? "alertdialog" : "dialog").css("position", this.fixed ? "fixed" : "absolute"), n || e(window).on("resize", e.proxy(this.reset, this)), this.modal) {
                            var r = {
                                position: "fixed",
                                left: 0,
                                top: 0,
                                width: "100%",
                                height: "100%",
                                overflow: "hidden",
                                userSelect: "none",
                                zIndex: this.zIndex || i.zIndex
                            };
                            o.addClass(this.className + "-modal"), s || e.extend(r, {
                                position: "absolute",
                                width: e(window).width() + "px",
                                height: e(document).height() + "px"
                            }), a.css(r).attr({
                                tabindex: "0"
                            }).on("focus", e.proxy(this.focus, this)), this.__mask = a.clone(!0).attr("style", "").insertAfter(o), a.addClass(this.className + "-backdrop").insertBefore(o), this.__ready = !0
                        }
                        o.html() || o.html(this.innerHTML)
                    }
                    return o.addClass(this.className + "-show").show(), a.show(), this.reset().focus(), this.__dispatchEvent("show"), this
                }, showModal: function () {
                    return this.modal = !0, this.show.apply(this, arguments)
                }, close: function (t) {
                    return !this.destroyed && this.open && (void 0 !== t && (this.returnValue = t), this.__popup.hide().removeClass(this.className + "-show"), this.__backdrop.hide(), this.open = !1, this.blur(), this.__dispatchEvent("close")), this
                }, remove: function () {
                    if (this.destroyed) return this;
                    this.__dispatchEvent("beforeremove"), i.current === this && (i.current = null), this.__popup.remove(), this.__backdrop.remove(), this.__mask.remove(), n || e(window).off("resize", this.reset), this.__dispatchEvent("remove");
                    for (var t in this) delete this[t];
                    return this
                }, reset: function () {
                    var t = this.follow;
                    return t ? this.__follow(t) : this.__center(), this.__dispatchEvent("reset"), this
                }, focus: function () {
                    var t = this.node,
                        o = this.__popup,
                        n = i.current,
                        s = this.zIndex = i.zIndex++;
                    if (n && n !== this && n.blur(!1), !e.contains(t, this.__getActive())) {
                        var a = o.find("[autofocus]")[0];
                        !this._autofocus && a ? this._autofocus = !0 : a = t, this.__focus(a)
                    }
                    return o.css("zIndex", s), i.current = this, o.addClass(this.className + "-focus"), this.__dispatchEvent("focus"), this
                }, blur: function () {
                    var t = this.__activeElement,
                        e = arguments[0];
                    return e !== !1 && this.__focus(t), this._autofocus = !1, this.__popup.removeClass(this.className + "-focus"), this.__dispatchEvent("blur"), this
                }, addEventListener: function (t, e) {
                    return this.__getEventListener(t).push(e), this
                }, removeEventListener: function (t, e) {
                    for (var i = this.__getEventListener(t), o = 0; o < i.length; o++) e === i[o] && i.splice(o--, 1);
                    return this
                }, __getEventListener: function (t) {
                    var e = this.__listener;
                    return e || (e = this.__listener = {}), e[t] || (e[t] = []), e[t]
                }, __dispatchEvent: function (t) {
                    var e = this.__getEventListener(t);
                    this["on" + t] && this["on" + t]();
                    for (var i = 0; i < e.length; i++) e[i].call(this)
                }, __focus: function (t) {
                    try {
                        this.autofocus && !/^iframe$/i.test(t.nodeName) && t.focus()
                    } catch (e) {}
                }, __getActive: function () {
                    try {
                        var t = document.activeElement,
                            e = t.contentDocument,
                            i = e && e.activeElement || t;
                        return i
                    } catch (o) {}
                }, __center: function () {
                    var t = this.__popup,
                        i = e(window),
                        o = e(document),
                        n = this.fixed,
                        s = n ? 0 : o.scrollLeft(),
                        a = n ? 0 : o.scrollTop(),
                        r = i.width(),
                        c = i.height(),
                        l = t.width(),
                        u = t.height(),
                        d = (r - l) / 2 + s,
                        h = 382 * (c - u) / 1e3 + a,
                        p = t[0].style;
                    p.left = Math.max(parseInt(d), s) + "px", p.top = Math.max(parseInt(h), a) + "px"
                }, __follow: function (t) {
                    var i = t.parentNode && e(t),
                        o = this.__popup;
                    if (this.__followSkin && o.removeClass(this.__followSkin), i) {
                        var n = i.offset();
                        if (n.left * n.top < 0) return this.__center()
                    }
                    var s = this,
                        a = this.fixed,
                        r = e(window),
                        c = e(document),
                        l = r.width(),
                        u = r.height(),
                        d = c.scrollLeft(),
                        h = c.scrollTop(),
                        p = o.width(),
                        f = o.height(),
                        _ = i ? i.outerWidth() : 0,
                        b = i ? i.outerHeight() : 0,
                        v = this.__offset(t),
                        g = v.left,
                        m = v.top,
                        y = a ? g - d : g,
                        w = a ? m - h : m,
                        k = a ? 0 : d,
                        x = a ? 0 : h,
                        $ = k + l - p,
                        T = x + u - f,
                        E = {},
                        L = this.align.split(" "),
                        C = this.className + "-",
                        N = {
                            top: "bottom",
                            bottom: "top",
                            left: "right",
                            right: "left"
                        },
                        I = {
                            top: "top",
                            bottom: "top",
                            left: "left",
                            right: "left"
                        },
                        j = [{
                            top: w - f,
                            bottom: w + b,
                            left: y - p,
                            right: y + _
                        }, {
                            top: w,
                            bottom: w - f + b,
                            left: y,
                            right: y - p + _
                        }],
                        z = {
                            left: y + _ / 2 - p / 2,
                            top: w + b / 2 - f / 2
                        },
                        M = {
                            left: [k, $],
                            top: [x, T]
                        };
                    e.each(L, function (t, e) {
                        j[t][e] > M[I[e]][1] && (e = L[t] = N[e]), j[t][e] < M[I[e]][0] && (L[t] = N[e])
                    }), L[1] || (I[L[1]] = "left" === I[L[0]] ? "top" : "left", j[1][L[1]] = z[I[L[1]]]), C += L.join("-") + " " + this.className + "-follow", s.__followSkin = C, i && o.addClass(C), E[I[L[0]]] = parseInt(j[0][L[0]]), E[I[L[1]]] = parseInt(j[1][L[1]]), o.css(E)
                }, __offset: function (t) {
                    var i = t.parentNode,
                        o = i ? e(t).offset() : {
                            left: t.pageX,
                            top: t.pageY
                        };
                    t = i ? t : t.target;
                    var n = t.ownerDocument,
                        s = n.defaultView || n.parentWindow;
                    if (s == window) return o;
                    var a = s.frameElement,
                        r = e(n),
                        c = r.scrollLeft(),
                        l = r.scrollTop(),
                        u = e(a).offset(),
                        d = u.left,
                        h = u.top;
                    return {
                        left: o.left + d - c,
                        top: o.top + h - l
                    }
                }
            }), i.zIndex = 1024, i.current = null, t.exports = i
        }(jQuery)
    },
    function (t, e, i) {
        t.exports = {
            backdropBackground: "#000",
            backdropOpacity: .3,
            content: '<span class="ui-dialog-loading">Loading..</span>',
            title: "",
            statusbar: "",
            button: null,
            ok: null,
            cancel: null,
            okValue: "ok",
            cancelValue: "cancel",
            cancelDisplay: !0,
            width: "",
            height: "",
            padding: "",
            skin: "",
            quickClose: !1,
            cssUri: "../css/ui-dialog.css",
            innerHTML: i(6)()
        }
    },
    function (module, exports) {
        module.exports = function (obj) {
            obj || (obj = {});
            var __p = "";
            with(obj) __p += '\n<div i="dialog" class="ui-dialog dbl-components-dialog">\n    <div class="ui-dialog-arrow-a"></div>\n    <div class="ui-dialog-arrow-b"></div>\n    <table class="ui-dialog-grid">\n        <tr>\n            <td i="header" class="ui-dialog-header">\n                <button i="close" class="ui-dialog-close">&#215;</button>\n                <div i="title" class="ui-dialog-title"></div>\n            </td>\n        </tr>\n        <tr>\n            <td i="body" class="ui-dialog-body">\n                <div i="type" class="ui-dialog-type"></div>\n                <div i="content" class="ui-dialog-content"></div>\n            </td>\n        </tr>\n        <tr>\n            <td i="footer" class="ui-dialog-footer">\n                <div i="statusbar" class="ui-dialog-statusbar"></div>\n                <div i="button" class="ui-dialog-button"></div>\n            </td>\n        </tr>\n    </table>\n</div>\n';
            return __p
        }
    }
]);
! function (t) {
    function n(a) {
        if (e[a]) return e[a].exports;
        var o = e[a] = {
            exports: {},
            id: a,
            loaded: !1
        };
        return t[a].call(o.exports, o, o.exports, n), o.loaded = !0, o.exports
    }
    var e = {};
    return n.m = t, n.c = e, n.p = "/build/", n(0)
}([

    function (t, n, e) {
        "use strict";

        function a() {
            var t = {
                initPosition: function () {}, start: function () {}, pause: function () {}
            };
            indexBanner_isIE9 || (t = o({
                container: "[data-group][data-group-open]",
                moveX: -25,
                moveY: -20,
                throttleTime: 60
            }));
            var n = $("[data-hover-container]"),
                e = n.find("[data-group]"),
                a = $("#J_topbar_2016"),
                i = r({
                    start: 0,
                    container: n,
                    group: "[data-group]",
                    parent: n.parent(),
                    debug: !1,
                    duration: 1.25,
                    autoPlayTime: AUTO_PLAY_TIME,
                    effect: "ease-out",
                    center: indexBanner_isIE9 ? "center-ie" : "center",
                    bottom: indexBanner_isIE9 ? "bottom-ie" : "bottom",
                    darkColor: DARK_BG,
                    lightColor: LIGHT_BG,
                    callback: function (n, o) {
                        t.initPosition(e.eq(n).find("[data-base-layer]")), "light" == o ? a.trigger("change", "white") : "dark" == o ? a.trigger("change", "dark") : console.error("color\u65e0\u6548", o)
                    }
                });
            ! function () {
                i.auto.start(), n.hover(function () {
                    i.auto.pause()
                }, function () {
                    i.auto.start()
                })
            }(),
            function () {
                var n;
                $(window).on("scroll", function () {
                    t.pause(), n && clearTimeout(n), n = setTimeout(function () {
                        t.start()
                    }, 200)
                })
            }()
        }
        e(1), e(2);
        var o = e(3),
            r = e(5);
        new a, t.exports = a
    },
    function (t, n) {},
    function (t, n) {
        Array.prototype.forEach || (Array.prototype.forEach = function (t, n) {
            var e, a;
            if (null == this) throw new TypeError("this is null or not defined");
            var o = Object(this),
                r = o.length >>> 0;
            if ("function" != typeof t) throw new TypeError(t + " is not a function");
            for (arguments.length > 1 && (e = n), a = 0; r > a;) {
                var i;
                a in o && (i = o[a], t.call(e, i, a, o)), a++
            }
        })
    },
    function (t, n, e) {
        var a = e(4);
        t.exports = function (t) {
            var n = {
                container: void 0,
                layer: [],
                transition: {},
                w: $(window).width(),
                h: $(window).height(),
                throttleTime: 100,
                moveTime: 1250,
                autoTime: 5e3,
                pause: !1
            };
            $.extend(n, t);
            var e = function (t) {
                    var e = (t.pageX / n.w - .5) * n.moveX || 10,
                        a = -(t.pageY / n.h - .5) * n.moveY || 10;
                    return {
                        moveX: e,
                        moveY: a
                    }
                },
                o = function (t) {
                    var e = "translateZ(" + t.zIndex + "px)",
                        a = {
                            "-webkit-transform": e,
                            "-moz-transform": e,
                            "-ms-transform": e,
                            "-o-transform": e,
                            transform: e
                        };
                    $.extend(a, n.transition), t.selector.css(a).addClass("no-transition")
                },
                r = function (t) {
                    var e = "rotateX(" + t.moveY + "deg) rotateY(" + t.moveX + "deg) ",
                        a = {
                            "-webkit-transform": e,
                            "-moz-transform": e,
                            "-ms-transform": e,
                            "-o-transform": e,
                            transform: e
                        };
                    return $.extend(a, n.transition), a
                },
                i = function () {
                    var t = a(function (t, n) {
                        var a = e(t);
                        $(n).css(r(a))
                    }, n.throttleTime);
                    n.container.each(function (e, a) {
                        $(a).on("mousemove", function (e) {
                            n.pause || t(e, a)
                        }), $(a).siblings().on("mousemove", function (e) {
                            n.pause || t(e, a)
                        }), $(a).parents("[data-group]").find(".left-header").on("mousemove", function (e) {
                            n.pause || t(e, a)
                        })
                    })
                },
                s = function () {
                    {
                        var t = n.container.find(".layer");
                        t.outerWidth()
                    }
                    t.each(function (t, e) {
                        var a = $(e);
                        void 0 == a.data("ignore") ? n.layer.push({
                            selector: a,
                            zIndex: a.data("zindex") || 0,
                            offset: a.data("offset") || 0,
                            offsetX: a.data("offsetx") || 0,
                            offsetY: a.data("offsetx") || 0
                        }) : a.css("z-index", 100)
                    })
                },
                u = function (t) {
                    $(t).css(r({
                        moveX: 0,
                        moveY: 0
                    }))
                },
                c = function () {
                    n.container = $(n.container).find("[data-base-layer]"), s(), n.layer.forEach(function (t) {
                        o(t)
                    }), i()
                };
            return c(), {
                initPosition: u,
                start: function () {
                    n.pause = !1
                }, pause: function () {
                    n.pause = !0
                }
            }
        }
    },
    function (t, n) {
        t.exports = function (t, n, e) {
            var a, o, r, i = null,
                s = 0;
            e || (e = {});
            var u = function () {
                s = e.leading === !1 ? 0 : Date.now(), i = null, r = t.apply(a, o), i || (a = o = null)
            };
            return function () {
                var c = Date.now();
                s || e.leading !== !1 || (s = c);
                var l = n - (c - s);
                return a = this, o = arguments, 0 >= l || l > n ? (i && (clearTimeout(i), i = null), s = c, r = t.apply(a, o), i || (a = o = null)) : i || e.trailing === !1 || (i = setTimeout(u, l)), r
            }
        }
    },
    function (t, n) {
        t.exports = function (t) {
            var n = {
                container: t.container,
                debug: !1,
                nowIndex: t.start,
                moveTime: 100 * (t.duration || 1.25),
                previous: 0,
                center: "center",
                bottom: "bottom",
                autoPlayTime: 5e3,
                auto: {},
                darkColor: "#24282C",
                lightColor: "#fff",
                callback: function () {}
            };
            $.extend(n, t), n.group = n.container.find(n.group), n.step = n.group.outerHeight(), n.num = n.group.length, n.group.each(function (t, e) {
                $(e).addClass(n.bottom)
            });
            var e = function (t) {
                    for (var e = $('<ul class="banner-tab"></ul>'), a = function (t) {
                        var e = n.start == t ? "active" : "";
                        return '<li class="banner-tab-li ' + e + '" data-index="' + t + '"></li>'
                    }, o = [], r = 0; t > r; r++) o.push(a(r));
                    e.append(o.join("")), e.css({
                        visibility: "hidden"
                    }), n.parent.append(e), e.css({
                        "margin-left": -e.width() / 2,
                        visibility: "visible"
                    }), n.ul = e
                },
                a = function (t) {
                    void 0 != t && n.nowIndex != t && (i(t), n.nowIndex = t, n.ul.find("li").eq(t).addClass("active").siblings().removeClass("active"))
                },
                o = function () {
                    n.ul.hover(function () {
                        n.auto.pause()
                    }), n.ul.on("click", function (t) {
                        var e = $(t.target),
                            a = e.data("index");
                        void 0 != a && n.nowIndex != a && (i(a), n.nowIndex = a, e.addClass("active").siblings().removeClass("active"))
                    })
                },
                r = function (t) {
                    t || (t = "dark"), n.parent.removeClass("dark light").addClass(t).css({
                        background: "dark" == t ? n.darkColor : n.lightColor
                    })
                },
                i = function (t) {
                    var e = "up";
                    t < n.nowIndex && (e = "down");
                    var a = n.group.eq(n.nowIndex),
                        o = n.group.eq(t);
                    n.group.removeClass("animating-enter-up animating-enter-down"), a.addClass(n.bottom).removeClass(n.center), "up" == e ? o.addClass(n.center + " animating-enter-up").removeClass(n.bottom) : o.addClass(n.center + " animating-enter-down").removeClass(n.bottom);
                    var i = o.data("active");
                    r(i), n.callback(t, i), n.ul.removeClass("dark light").addClass(o.data("active"))
                },
                s = function (t) {
                    return "[data-groupindex=" + t + "]"
                },
                u = function () {
                    r();
                    var t = n.container.find(s(n.start)).eq(0);
                    t.attr("class", n.center + " animating-enter-up");
                    var a = t.data("active");
                    n.callback(n.start, a), n.num > 1 && (e(n.num), o(), n.ul.addClass(a))
                },
                c = function (t) {
                    return (t + 1) % n.num
                };
            return n.auto = {
                timer: 0,
                start: function () {
                    var t = this;
                    clearInterval(t.timer), t.timer = setInterval(function () {
                        var t = c(n.nowIndex);
                        a(t)
                    }, n.autoPlayTime)
                }, pause: function () {
                    clearInterval(this.timer)
                }
            }, u(), {
                auto: n.auto
            }
        }
    }
]);
! function (n) {
    function a(t) {
        if (i[t]) return i[t].exports;
        var e = i[t] = {
            exports: {},
            id: t,
            loaded: !1
        };
        return n[t].call(e.exports, e, e.exports, a), e.loaded = !0, e.exports
    }
    var i = {};
    return a.m = n, a.c = i, a.p = "/build/", a(0)
}([

    function (n, a, i) {
        "use strict";

        function t() {
            e({
                container: ".banner-bottom-container",
                distance: 175,
                isIE8: indexBanner_isIE8
            })
        }
        i(1);
        var e = i(2);
        new t, n.exports = t
    },
    function (n, a) {},
    function (n, a) {
        function i(n) {
            function a(n) {
                var a = $(n).find("[data-image]"),
                    i = a.data("ie8img");
                a.css({
                    "background-image": 'url("' + i + '")'
                })
            }

            function i(a) {
                function i() {
                    r.css({
                        "background-position": "0 -" + m * u + "px"
                    })
                }

                function t() {
                    u++, s > u ? (i(), o = requestAnimationFrame(t)) : cancelAnimationFrame(o)
                }

                function e() {
                    u--, u >= 0 ? (i(), o = requestAnimationFrame(e)) : cancelAnimationFrame(o)
                }
                var o, r = $(a).find("[data-image]"),
                    c = r.data("url"),
                    u = 0,
                    s = r.data("frame"),
                    m = n.distance;
                r.css({
                    "background-image": 'url("' + c + '")'
                }), $(a).hover(function () {
                    cancelAnimationFrame(o), o = requestAnimationFrame(t)
                }, function () {
                    cancelAnimationFrame(o), o = requestAnimationFrame(e)
                })
            }
            $(n.container + " [data-icon-animate]").each(function () {
                n.isIE8 ? a(this) : i(this)
            })
        }
        n.exports = i
    }
]);
! function (e) {
    function u(t) {
        if (o[t]) return o[t].exports;
        var i = o[t] = {
            exports: {},
            id: t,
            loaded: !1
        };
        return e[t].call(i.exports, i, i.exports, u), i.loaded = !0, i.exports
    }
    var o = {};
    return u.m = e, u.c = o, u.p = "/build/", u(0)
}([

    function (e, u, o) {
        "use strict";

        function t() {
            $(".card-area .card-item").on("mouseenter", function () {
                i.push($(this))
            })
        }
        o(1);
        var i = o(2);
        new t, e.exports = t
    },
    function (e, u) {},
    function (e, u) {
        e.exports = {
            _queue: [],
            gap: !1,
            timer: void 0,
            _lock: !1,
            _timeout: void 0,
            duration: 310,
            run: function () {
                var e = this;
                if (e._queue.length) {
                    var u = e._queue.pop();
                    u.fn(u.param), e._queue = []
                }
            }, lock: function () {
                var e = this;
                e._lock = !0, setTimeout(function () {
                    e.unlock(), e.logic()
                }, e.duration)
            }, unlock: function () {
                this._lock = !1
            }, logic: function () {
                var e = this;
                if (e._lock);
                else if (e._queue.length) {
                    var u = e._queue.pop();
                    u.addClass("active").siblings().removeClass("active"), e._queue = [], e.lock()
                }
            }, push: function (e) {
                var u = this;
                u._queue.push(e), u.logic()
            }
        }
    }
]);
! function (n) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var i = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return n[o].call(i.exports, i, i.exports, t), i.loaded = !0, i.exports
    }
    var e = {};
    return t.m = n, t.c = e, t.p = "/build/", t(0)
}([

    function (n, t, e) {
        "use strict";

        function o() {
            i({
                container: "ali-main-productor-other",
                selector: "fade",
                between: -60
            })
        }
        e(1), e(2);
        var i = e(3);
        new o, n.exports = o
    },
    function (n, t) {},
    function (n, t) {
        ! function () {
            for (var n = 0, t = ["ms", "moz", "webkit", "o"], e = 0; e < t.length && !window.requestAnimationFrame; ++e) window.requestAnimationFrame = window[t[e] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[t[e] + "CancelAnimationFrame"] || window[t[e] + "CancelRequestAnimationFrame"];
            window.requestAnimationFrame || (window.requestAnimationFrame = function (t, e) {
                var o = (new Date).getTime(),
                    i = Math.max(0, 16 - (o - n)),
                    r = window.setTimeout(function () {
                        t(o + i)
                    }, i);
                return n = o + i, r
            }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                clearTimeout(n)
            }), Function.prototype.bind || (Function.prototype.bind = function (n) {
                if ("function" != typeof this) throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                var t = Array.prototype.slice.call(arguments, 1),
                    e = this,
                    o = function () {},
                    i = function () {
                        return e.apply(this instanceof o && n ? this : n, t.concat(Array.prototype.slice.call(arguments)))
                    };
                return o.prototype = this.prototype, i.prototype = new o, i
            })
        }()
    },
    function (n, t) {
        function e(n) {
            function t() {
                document.getElementById(n.container).style.visibility = "visible", r.css("visibility", "visible"), a.each(function () {
                    $(this).addClass($(this).data(n.selector))
                })
            }

            function e() {
                l > u ? (u++, o.scrollTop() + i >= w && (t(), o.off("scroll", e))) : u = 0
            }
            if (!$) return console.error("need jQuery");
            var o = $(window),
                i = o.height(),
                r = $("#" + n.container),
                a = r.find("[data-" + n.selector + "]"),
                c = r.offset().top,
                s = n.between || 150,
                u = 0,
                l = 30,
                w = c + s;
            o.scrollTop() + i < w ? o.on("scroll", e) : t()
        }
        n.exports = e
    }
]);
! function (t) {
    function i(n) {
        if (e[n]) return e[n].exports;
        var s = e[n] = {
            exports: {},
            id: n,
            loaded: !1
        };
        return t[n].call(s.exports, s, s.exports, i), s.loaded = !0, s.exports
    }
    var e = {};
    return i.m = t, i.c = e, i.p = "/build/", i(0)
}([

    function (t, i, e) {
        "use strict";

        function n() {
            var t = new s({
                showSize: 5,
                moveSize: 5
            });
            t.init()
        }
        e(1);
        var s = e(2);
        new n, t.exports = n
    },
    function (t, i) {},
    function (t, i) {
        "use strict";

        function e(t) {
            this.showSize = t.showSize || 5, this.moveSize = t.moveSize || 5, this.currentPos = t.currentPos || 0, this.containerId = t.containerId || "slide-container", this.bodyId = t.bodyId || "slide-body", this.contentId = t.contentId || "slide-content", this.leftBtnId = t.leftBtnId || "left-btn", this.rightBtnId = t.rightBtnId || "right-btn", this.itemClazz = t.itemClazz || "slide-item", this.btnPreClazz = t.btnPreClazz || "btn-bg", this.isRunning = !1
        }
        e.prototype = {
            init: function () {
                this.initDom(), this.initState(), this.bindEvent()
            }, initDom: function () {
                this.container = $("#" + this.containerId), this.content = $("#" + this.contentId), this.size = this.content.find(".slide-item").size();
                var t = this.content.html();
                this.content.append(t).prepend(t), this.body = $("#" + this.bodyId), this.leftBtn = $("#" + this.leftBtnId), this.rightBtn = $("#" + this.rightBtnId)
            }, initState: function () {
                this.itemW = this.container.width() / this.showSize, this.container.height(420 * this.itemW / 288), this.body.height(420 * this.itemW / 288), this.content.width(this.itemW * this.size * 3), this.content.css({
                    left: -(this.itemW * this.size)
                }), this.content.find(".item-img-panel").height(this.content.find(".item-img-panel").width()), this.content.find("." + this.itemClazz).width(this.itemW).height(420 * this.itemW / 288), this.initBtnsState()
            }, initBtnsState: function () {
                this.size <= this.showSize && (this.leftBtn.hide(), this.leftBtn.prev("." + this.btnPreClazz).hide(), this.rightBtn.hide(), this.rightBtn.prev("." + this.btnPreClazz).hide())
            }, bindEvent: function () {
                var t = this;
                $(window).bind("resize.slider", function () {
                    t.initState()
                }), this.leftBtn.bind("click.slider", function () {
                    t.moveToRight()
                }), this.rightBtn.bind("click.slider", function () {
                    t.moveToLeft()
                }), this.container.on("resetevent", function (i, e) {
                    e.reverse(), $.each(e, function (i, e) {
                        var n = t.content.find('.slide-item[data-code="' + e + '"]');
                        n.each(function (i) {
                            $(this).insertBefore(t.content.find(".slide-item").eq(i * t.size))
                        })
                    }), t.content.find(".slide-item").find(".bg").each(function (i) {
                        i % t.size % 2 == 0 ? $(this).hasClass("other-bg") && $(this).removeClass("other-bg") : $(this).hasClass("other-bg") || $(this).addClass("other-bg")
                    })
                })
            }, moveToLeft: function () {
                var t = this;
                if (this.direction = "left", !t.isRunning) {
                    t.isRunning = !0;
                    var i = this.getNextPosition(),
                        e = this.getMoveWidth(i);
                    t.content.stop(!0, !0).animate({
                        left: "-=" + e + "px"
                    }, 350, function () {
                        t.currentPos = i, t.isRunning = !1, t.content.position().left <= -(t.itemW * t.size * 2) && (setTimeout(function () {
                            t.content.css({
                                left: -(t.itemW * t.size)
                            })
                        }, 0), t.currentPos = 0)
                    })
                }
            }, moveToRight: function () {
                var t = this;
                if (this.direction = "right", !t.isRunning) {
                    t.isRunning = !0;
                    var i = this.getNextPosition(),
                        e = this.getMoveWidth(i);
                    t.content.stop(!0, !0).animate({
                        left: "+=" + e + "px"
                    }, 350, function () {
                        t.currentPos = i, t.isRunning = !1, t.content.position().left >= 0 && (setTimeout(function () {
                            t.content.css({
                                left: -(t.itemW * t.size)
                            })
                        }, 0), t.currentPos = 0)
                    })
                }
            }, getMoveWidth: function (t) {
                return "right" === this.direction ? this.currentPos < t ? this.moveSize * this.itemW : (this.currentPos - t) * this.itemW : this.currentPos > t ? this.moveSize * this.itemW : (t - this.currentPos) * this.itemW
            }, getNextPosition: function () {
                return "left" === this.direction ? this.currentPos + this.moveSize + this.showSize < this.size ? this.currentPos + this.moveSize : this.currentPos + this.showSize == this.size ? 0 : this.size - this.showSize : 0 == this.currentPos ? this.size - this.showSize : this.currentPos - this.moveSize > 0 ? this.currentPos - this.moveSize : 0
            }, canScroll: function () {
                var t = this;
                return t.isRunning ? !1 : void 0
            }
        }, t.exports = e
    }
]);
! function (n) {
    function t(e) {
        if (i[e]) return i[e].exports;
        var o = i[e] = {
            exports: {},
            id: e,
            loaded: !1
        };
        return n[e].call(o.exports, o, o.exports, t), o.loaded = !0, o.exports
    }
    var i = {};
    return t.m = n, t.c = i, t.p = "/build/", t(0)
}([

    function (n, t, i) {
        "use strict";

        function e() {
            o({
                container: "#ali-main-market",
                distance: 75,
                isIE8: indexMarket_isIE8
            }), a({
                container: "ali-main-market",
                selector: "fade",
                between: -60
            })
        }
        i(1), i(2);
        var o = i(3),
            a = i(4);
        new e, n.exports = e
    },
    function (n, t) {},
    function (n, t) {
        ! function () {
            for (var n = 0, t = ["ms", "moz", "webkit", "o"], i = 0; i < t.length && !window.requestAnimationFrame; ++i) window.requestAnimationFrame = window[t[i] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[t[i] + "CancelAnimationFrame"] || window[t[i] + "CancelRequestAnimationFrame"];
            window.requestAnimationFrame || (window.requestAnimationFrame = function (t, i) {
                var e = (new Date).getTime(),
                    o = Math.max(0, 16 - (e - n)),
                    a = window.setTimeout(function () {
                        t(e + o)
                    }, o);
                return n = e + o, a
            }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                clearTimeout(n)
            }), Function.prototype.bind || (Function.prototype.bind = function (n) {
                if ("function" != typeof this) throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                var t = Array.prototype.slice.call(arguments, 1),
                    i = this,
                    e = function () {},
                    o = function () {
                        return i.apply(this instanceof e && n ? this : n, t.concat(Array.prototype.slice.call(arguments)))
                    };
                return e.prototype = this.prototype, o.prototype = new e, o
            })
        }()
    },
    function (n, t) {
        function i(n) {
            function t(n) {
                var t = $(n).find("[data-image]"),
                    i = t.data("ie8img");
                t.css({
                    "background-image": 'url("' + i + '")'
                })
            }

            function i(t) {
                function i() {
                    r.css({
                        "background-position": "0 -" + m * s + "px"
                    })
                }

                function e() {
                    s++, u > s ? (i(), a = requestAnimationFrame(e)) : cancelAnimationFrame(a)
                }

                function o() {
                    s--, s >= 0 ? (i(), a = requestAnimationFrame(o)) : cancelAnimationFrame(a)
                }
                var a, r = $(t).find("[data-image]"),
                    c = r.data("url"),
                    s = 0,
                    u = r.data("frame"),
                    m = n.distance;
                r.css({
                    "background-image": 'url("' + c + '")'
                }), $(t).hover(function () {
                    cancelAnimationFrame(a), a = requestAnimationFrame(e)
                }, function () {
                    cancelAnimationFrame(a), a = requestAnimationFrame(o)
                })
            }
            $(n.container + " [data-icon-animate]").each(function () {
                n.isIE8 ? t(this) : i(this)
            })
        }
        n.exports = i
    },
    function (n, t) {
        function i(n) {
            function t() {
                document.getElementById(n.container).style.visibility = "visible", a.css("visibility", "visible"), r.each(function () {
                    $(this).addClass($(this).data(n.selector))
                })
            }

            function i() {
                m > u ? (u++, e.scrollTop() + o >= l && (t(), e.off("scroll", i))) : u = 0
            }
            if (!$) return console.error("need jQuery");
            var e = $(window),
                o = e.height(),
                a = $("#" + n.container),
                r = a.find("[data-" + n.selector + "]"),
                c = a.offset().top,
                s = n.between || 150,
                u = 0,
                m = 30,
                l = c + s;
            e.scrollTop() + o < l ? e.on("scroll", i) : t()
        }
        n.exports = i
    }
]);
! function (r) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var n = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return r[o].call(n.exports, n, n.exports, t), n.loaded = !0, n.exports
    }
    var e = {};
    return t.m = r, t.c = e, t.p = "/build/", t(0)
}([

    function (r, t) {}
]);
! function (n) {
    function t(a) {
        if (e[a]) return e[a].exports;
        var r = e[a] = {
            exports: {},
            id: a,
            loaded: !1
        };
        return n[a].call(r.exports, r, r.exports, t), r.loaded = !0, r.exports
    }
    var e = {};
    return t.m = n, t.c = e, t.p = "/build/", t(0)
}([

    function (n, t, e) {
        "use strict";

        function a() {
            function n() {
                var n = !1,
                    t = window.navigator.msPointerEnabled,
                    e = $("#ali-main-register");
                e.css(n || t ? {
                    "background-image": 'url("' + e.data("ie8img") + '")'
                } : {
                    "background-image": 'url("' + e.data("svg") + '")'
                })
            }

            function t(n) {
                r({
                    speed: 2.5,
                    container: "ali-main-defence",
                    callback: e
                })
            }

            function e() {
                var n = function (n, t, e, a) {
                        return e * ((n = n / a - 1) * n * n * n * n + 1) + t
                    },
                    t = {
                        useEasing: !0,
                        useGrouping: !0,
                        easingFn: n[EASE_FN],
                        separator: ",",
                        decimal: ".",
                        prefix: "",
                        suffix: ""
                    },
                    e = {
                        init: !0,
                        count: {},
                        container: $("#defence-number"),
                        changeWidth: function (n) {
                            var t = n.toString(),
                                e = t.length;
                            this.container.css("width", 19 * e)
                        }, time: 1e3 * NUMBER_DURATION_TIME,
                        initFn: function (n) {
                            for (var a = parseInt(n), r = parseInt(NUMBER_CHANGE_TIME), o = a.toString().length, u = 1, s = 0; o - 1 > s; s++) u *= 10;
                            e.count = new i("defence-number", u, a - NUMBER_LARGE, 0, r, t), e.changeWidth(a), e.count.start(function () {
                                e.init && e.count.update(a)
                            })
                        }, get: function () {
                            var n = this;
                            $.ajax({
                                url: a,
                                dataType: "jsonp",
                                jsonp: "callback",
                                lastData: 0,
                                success: function (t) {
                                    return n.cb(t)
                                }, error: function () {
                                    e.cb({
                                        code: 500
                                    })
                                }
                            })
                        }, cb: function (n) {
                            var t, a = n.code;
                            return 200 != a ? e.lastData ? t = e.lastData : $(".ali-main-defence-wrapper").css({
                                display: "none"
                            }) : t = n.data, e.changeWidth(t), e.init ? (e.init = !1, e.initFn(t)) : e.count.update(t), e.lastData = t, setTimeout(function () {
                                return e.get()
                            }, e.time)
                        }
                    };
                e.get()
            }
            n();
            var a = "https://yundun.console.aliyun.com/common/event/count.json";
            $.ajax({
                url: a,
                dataType: "jsonp",
                jsonp: "callback",
                lastData: 0,
                success: function (n) {
                    200 == n.code && ($(".ali-main-defence-wrapper").css("display", "block"), t(n.data))
                }, error: function () {}
            })
        }
        e(1); {
            var r = e(2),
                i = e(3);
            e(4)
        }
        new a, n.exports = a
    },
    function (n, t) {},
    function (n, t) {
        n.exports = function (n) {
            function t(t) {
                document.getElementById(a.container).style.backgroundPosition = "0 -" + t / n.speed + "px"
            }

            function e() {
                d > f ? (f++, r = i.scrollTop() + o - m, r >= 0 && c > r && (p && (n.callback && n.callback(), p = !1), t(c - r))) : f = 0
            }
            var a = {
                speed: 5
            };
            $.extend(a, n);
            var r, i = $(window),
                o = i.height(),
                u = $("#" + a.container),
                s = u.outerHeight(),
                c = o + s,
                l = u.offset().top,
                f = 0,
                d = 10,
                m = l,
                p = !0;
            e(), i.on("scroll", e)
        }
    },
    function (n, t, e) {
        var a, r;
        ! function (i, o) {
            a = o, r = "function" == typeof a ? a.call(t, e, t, n) : a, !(void 0 !== r && (n.exports = r))
        }(this, function (n, t, e) {
            var a = function (n, t, e, a, r, i) {
                for (var o = 0, u = ["webkit", "moz", "ms", "o"], s = 0; s < u.length && !window.requestAnimationFrame; ++s) window.requestAnimationFrame = window[u[s] + "RequestAnimationFrame"], window.cancelAnimationFrame = window[u[s] + "CancelAnimationFrame"] || window[u[s] + "CancelRequestAnimationFrame"];
                window.requestAnimationFrame || (window.requestAnimationFrame = function (n, t) {
                    var e = (new Date).getTime(),
                        a = Math.max(0, 16 - (e - o)),
                        r = window.setTimeout(function () {
                            n(e + a)
                        }, a);
                    return o = e + a, r
                }), window.cancelAnimationFrame || (window.cancelAnimationFrame = function (n) {
                    clearTimeout(n)
                });
                var c = this;
                c.options = {
                    useEasing: !0,
                    useGrouping: !0,
                    separator: ",",
                    decimal: ".",
                    easingFn: null,
                    formattingFn: null
                };
                for (var l in i) i.hasOwnProperty(l) && (c.options[l] = i[l]);
                "" === c.options.separator && (c.options.useGrouping = !1), c.options.prefix || (c.options.prefix = ""), c.options.suffix || (c.options.suffix = ""), c.d = "string" == typeof n ? document.getElementById(n) : n, c.startVal = Number(t), c.endVal = Number(e), c.countDown = c.startVal > c.endVal, c.frameVal = c.startVal, c.decimals = Math.max(0, a || 0), c.dec = Math.pow(10, c.decimals), c.duration = 1e3 * Number(r) || 2e3, c.formatNumber = function (n) {
                    n = n.toFixed(c.decimals), n += "";
                    var t, e, a, r;
                    if (t = n.split("."), e = t[0], a = t.length > 1 ? c.options.decimal + t[1] : "", r = /(\d+)(\d{3})/, c.options.useGrouping)
                        for (; r.test(e);) e = e.replace(r, "$1" + c.options.separator + "$2");
                    return c.options.prefix + e + a + c.options.suffix
                }, c.easeOutExpo = function (n, t, e, a) {
                    return e * (-Math.pow(2, -10 * n / a) + 1) * 1024 / 1023 + t
                }, c.easingFn = c.options.easingFn ? c.options.easingFn : c.easeOutExpo, c.formattingFn = c.options.formattingFn ? c.options.formattingFn : c.formatNumber, c.version = function () {
                    return "1.7.1"
                }, c.printValue = function (n) {
                    var t = c.formattingFn(n);
                    "INPUT" === c.d.tagName ? this.d.value = t : "text" === c.d.tagName || "tspan" === c.d.tagName ? this.d.textContent = t : this.d.innerHTML = t
                }, c.count = function (n) {
                    c.startTime || (c.startTime = n), c.timestamp = n;
                    var t = n - c.startTime;
                    c.remaining = c.duration - t, c.frameVal = c.options.useEasing ? c.countDown ? c.startVal - c.easingFn(t, 0, c.startVal - c.endVal, c.duration) : c.easingFn(t, c.startVal, c.endVal - c.startVal, c.duration) : c.countDown ? c.startVal - (c.startVal - c.endVal) * (t / c.duration) : c.startVal + (c.endVal - c.startVal) * (t / c.duration), c.frameVal = c.countDown ? c.frameVal < c.endVal ? c.endVal : c.frameVal : c.frameVal > c.endVal ? c.endVal : c.frameVal, c.frameVal = Math.round(c.frameVal * c.dec) / c.dec, c.printValue(c.frameVal), t < c.duration ? c.rAF = requestAnimationFrame(c.count) : c.callback && c.callback()
                }, c.start = function (n) {
                    return c.callback = n, c.rAF = requestAnimationFrame(c.count), !1
                }, c.pauseResume = function () {
                    c.paused ? (c.paused = !1, delete c.startTime, c.duration = c.remaining, c.startVal = c.frameVal, requestAnimationFrame(c.count)) : (c.paused = !0, cancelAnimationFrame(c.rAF))
                }, c.reset = function () {
                    c.paused = !1, delete c.startTime, c.startVal = t, cancelAnimationFrame(c.rAF), c.printValue(c.startVal)
                }, c.update = function (n) {
                    cancelAnimationFrame(c.rAF), c.paused = !1, delete c.startTime, c.startVal = c.frameVal, c.endVal = Number(n), c.countDown = c.startVal > c.endVal, c.rAF = requestAnimationFrame(c.count)
                }, c.printValue(c.startVal)
            };
            return a
        })
    },
    function (n, t) {
        n.exports = {
            easeInQuad: function (n, t, e, a) {
                return e * (n /= a) * n + t
            }, easeOutQuad: function (n, t, e, a) {
                return -e * (n /= a) * (n - 2) + t
            }, easeInOutQuad: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n + t : -e / 2 * (--n * (n - 2) - 1) + t
            }, easeInCubic: function (n, t, e, a) {
                return e * (n /= a) * n * n + t
            }, easeOutCubic: function (n, t, e, a) {
                return e * ((n = n / a - 1) * n * n + 1) + t
            }, easeInOutCubic: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n + t : e / 2 * ((n -= 2) * n * n + 2) + t
            }, easeInQuart: function (n, t, e, a) {
                return e * (n /= a) * n * n * n + t
            }, easeOutQuart: function (n, t, e, a) {
                return -e * ((n = n / a - 1) * n * n * n - 1) + t
            }, easeInOutQuart: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n * n + t : -e / 2 * ((n -= 2) * n * n * n - 2) + t
            }, easeInQuint: function (n, t, e, a) {
                return e * (n /= a) * n * n * n * n + t
            }, easeOutQuint: function (n, t, e, a) {
                return e * ((n = n / a - 1) * n * n * n * n + 1) + t
            }, easeInOutQuint: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? e / 2 * n * n * n * n * n + t : e / 2 * ((n -= 2) * n * n * n * n + 2) + t
            }, easeInSine: function (n, t, e, a) {
                return -e * Math.cos(n / a * (Math.PI / 2)) + e + t
            }, easeOutSine: function (n, t, e, a) {
                return e * Math.sin(n / a * (Math.PI / 2)) + t
            }, easeInOutSine: function (n, t, e, a) {
                return -e / 2 * (Math.cos(Math.PI * n / a) - 1) + t
            }, easeInExpo: function (n, t, e, a) {
                return 0 == n ? t : e * Math.pow(2, 10 * (n / a - 1)) + t
            }, easeOutExpo: function (n, t, e, a) {
                return n == a ? t + e : e * (-Math.pow(2, -10 * n / a) + 1) + t
            }, easeInOutExpo: function (n, t, e, a) {
                return 0 == n ? t : n == a ? t + e : (n /= a / 2) < 1 ? e / 2 * Math.pow(2, 10 * (n - 1)) + t : e / 2 * (-Math.pow(2, -10 * --n) + 2) + t
            }, easeInCirc: function (n, t, e, a) {
                return -e * (Math.sqrt(1 - (n /= a) * n) - 1) + t
            }, easeOutCirc: function (n, t, e, a) {
                return e * Math.sqrt(1 - (n = n / a - 1) * n) + t
            }, easeInOutCirc: function (n, t, e, a) {
                return (n /= a / 2) < 1 ? -e / 2 * (Math.sqrt(1 - n * n) - 1) + t : e / 2 * (Math.sqrt(1 - (n -= 2) * n) + 1) + t
            }
        }
    }
]);
! function (i) {
    function t(n) {
        if (e[n]) return e[n].exports;
        var o = e[n] = {
            exports: {},
            id: n,
            loaded: !1
        };
        return i[n].call(o.exports, o, o.exports, t), o.loaded = !0, o.exports
    }
    var e = {};
    return t.m = i, t.c = e, t.p = "/build/", t(0)
}([

    function (i, t, e) {
        "use strict";

        function n() {
            if (indexServe_isIE8) {
                var i = $("#serve-img-area").find("img");
                i.attr("src", i.data("ie8img"))
            } else
                for (var t = o({
                    container: "#serve-img-area",
                    delayTime: 2250
                }), e = JSON.parse(serveDots), n = e.length, s = 0; n > s; s++) t.append(e[s])
        }
        e(1);
        var o = e(2);
        new n, i.exports = n
    },
    function (i, t) {},
    function (i, t) {
        i.exports = function (i) {
            var t = $(i.container);
            t.css("position", "relative");
            var e = '<div class="point-area"></div>',
                n = function (i) {
                    return '<p class="point-name">' + i + "</p>"
                },
                o = '<div class="point point-white"></div>',
                s = '<div class="point point-dot"></div><div class="point point-10"></div><div class="point point-40"></div><div class="point point-shadow point-80"></div>',
                p = '<div class="point point-dot"></div><div class="point point-10"></div><div class="point point-70"></div>',
                a = !1;
            return {
                append: function (r) {
                    var d, l = r.width ? r.width : 150,
                        v = $(e),
                        c = $(n(r.name));
                    if (d = "left" == r.position ? {
                        position: "absolute",
                        top: l / 2 - 10,
                        left: l / 2 - 40
                    } : {
                        position: "absolute",
                        top: l / 2 - 10,
                        left: l / 2 + 10
                    }, c.css(d), v.append(c), "white" == r.type) v.append(o);
                    else {
                        if ("blue" != r.type) return console.error("type\u53ea\u53ef\u4ee5\u4e3a[white|blue]");
                        v.append(r.width < 100 ? p : s)
                    }
                    v.css({
                        top: r.top - l / 2,
                        left: r.left - l / 2,
                        position: "absolute",
                        width: l,
                        height: l,
                        visibility: "hidden",
                        opacity: 0
                    });
                    var u = a ? 0 : i.delayTime;
                    a = !a, setTimeout(function () {
                        t.append(v)
                    }, u), setTimeout(function () {
                        v.css({
                            visibility: "visible",
                            opacity: 1
                        })
                    }, i.delayTime + 100)
                }
            }
        }
    }
]);
! function (t) {
    function r(e) {
        if (n[e]) return n[e].exports;
        var o = n[e] = {
            exports: {},
            id: e,
            loaded: !1
        };
        return t[e].call(o.exports, o, o.exports, r), o.loaded = !0, o.exports
    }
    var n = {};
    return r.m = t, r.c = n, r.p = "/build/", r(0)
}([

    function (t, r, n) {
        "use strict";

        function e() {}
        n(1), new e, t.exports = e
    },
    function (t, r) {}
]);
! function (n) {
    function e(o) {
        if (t[o]) return t[o].exports;
        var r = t[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return n[o].call(r.exports, r, r.exports, e), r.loaded = !0, r.exports
    }
    var t = {};
    return e.m = n, e.c = t, e.p = "/build/", e(0)
}([

    function (n, e, t) {
        "use strict";

        function o() {
            function n() {
                var n = !1,
                    e = window.navigator.msPointerEnabled,
                    t = $("#ali-main-defence");
                t.css(n || e ? {
                    "background-image": 'url("' + t.data("ie8img") + '")'
                } : {
                    "background-image": 'url("' + t.data("svg") + '")'
                })
            }
            n(), r({
                speed: 2.5,
                container: "ali-main-register"
            })
        }
        t(1);
        var r = t(2);
        new o, n.exports = o
    },
    function (n, e) {},
    function (n, e) {
        n.exports = function (n) {
            function e(e) {
                document.getElementById(o.container).style.backgroundPosition = "0 -" + e / n.speed + "px"
            }

            function t() {
                f > l ? (l++, r = i.scrollTop() + a - p, r >= 0 && u > r && (g && (n.callback && n.callback(), g = !1), e(u - r))) : l = 0
            }
            var o = {
                speed: 5
            };
            $.extend(o, n);
            var r, i = $(window),
                a = i.height(),
                c = $("#" + o.container),
                s = c.outerHeight(),
                u = a + s,
                d = c.offset().top,
                l = 0,
                f = 10,
                p = d,
                g = !0;
            i.on("scroll", t)
        }
    }
]);
! function (r) {
    function t(o) {
        if (e[o]) return e[o].exports;
        var n = e[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return r[o].call(n.exports, n, n.exports, t), n.loaded = !0, n.exports
    }
    var e = {};
    return t.m = r, t.c = e, t.p = "/build/", t(0)
}([

    function (r, t) {}
]);
! function (t) {
    function e(o) {
        if (i[o]) return i[o].exports;
        var n = i[o] = {
            exports: {},
            id: o,
            loaded: !1
        };
        return t[o].call(n.exports, n, n.exports, e), n.loaded = !0, n.exports
    }
    var i = {};
    return e.m = t, e.c = i, e.p = "/build/", e(0)
}([

    function (t, e, i) {
        "use strict";

        function o() {
            var t = i(2),
                e = function (e) {
                    var i = [];
                    e.buttons = [], e.button1 && e.button1.length > 0 && (e.buttons[0] = {}, e.buttons[0].button = e.button1, e.buttons[0].link = e.link1, e.buttons[0].or = e.or1, e.buttons[0].btn_type = e.btn_type1), e.button2 && e.button2.length > 0 && (e.buttons[1] = {}, e.buttons[1].button = e.button2, e.buttons[1].link = e.link2, e.buttons[1].or = e.or2, e.buttons[1].btn_type = e.btn_type2), e.button3 && e.button3.length > 0 && (e.buttons[2] = {}, e.buttons[2].button = e.button3, e.buttons[2].link = e.link3, e.buttons[2].or = e.or3, e.buttons[2].btn_type = e.btn_type3), e.buttons && $.each(e.buttons, function (t, e) {
                        e.button.length > 0 && i.push({
                            value: e.button,
                            callback: function () {
                                window.location.href = e.link
                            }, autofocus: "blue" == e.btn_type
                        })
                    }), t({
                        width: 476,
                        title: e.title,
                        content: e.content,
                        button: i
                    }).showModal()
                },
                o = function (t, i, n) {
                    $.ajax({
                        url: "//promotion.aliyun.com/promotion/coderlottery/lottery.htm",
                        dataType: "jsonp",
                        jsonp: "cback",
                        data: {
                            fc: i[n].fc,
                            umidToken: umidToken,
                            collina: getUA()
                        }
                    }).done(function (s) {
                        if (29 == s.code) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else if (25 == s.code) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else if (i.length - 1 == n) {
                            var a;
                            $.each(i[n].lotteryTip, function (t, e) {
                                e.code == s.code && (a = e)
                            }), a || (a = i[n].lotteryTip[i[n].lotteryTip.length - 1]), e(a)
                        } else o(t, i, n + 1)
                    })
                };
            $(".aliyun-common-double-lottery").each(function () {
                var t = JSON.parse($(this).find("textarea[name=moduleinfo]").val());
                $.each(t, function (t, e) {
                    var i = $(e.id);
                    i.attr({
                        _href: i.attr("href"),
                        "data-spm-click": "gostr=/aliyun;locaid=" + e.spm
                    }).data("config", e.li).css("cursor", "pointer"), i.attr("href", "javascript:;").removeAttr("target"), i.click(function () {
                        o(this, $(this).data("config"), 0)
                    })
                })
            })
        }
        i(1), new o, t.exports = o
    },
    function (t, e) {},
    function (t, e, i) {
        + function (e) {
            i(3);
            var o = i(4),
                n = i(5),
                s = 0,
                a = new Date - 0,
                r = !("minWidth" in e("html")[0].style),
                c = "createTouch" in document && !("onmousemove" in document) || /(iPhone|iPad|iPod)/i.test(navigator.userAgent),
                l = !r && !c,
                u = function (t, i, o) {
                    t.width = t.width || 480;
                    var n = t = t || {};
                    ("string" == typeof t || 1 === t.nodeType) && (t = {
                        content: t,
                        fixed: !c
                    }), t = e.extend(!0, {}, u.defaults, t), t.original = n;
                    var r = t.id = t.id || a + s,
                        d = u.get(r);
                    return d ? d.focus() : (l || (t.fixed = !1), t.quickClose && (t.modal = !0, t.backdropOpacity = 0), e.isArray(t.button) || (t.button = []), void 0 !== i && (t.ok = i), t.ok && t.button.push({
                        id: "ok",
                        value: t.okValue,
                        callback: t.ok,
                        autofocus: !0
                    }), void 0 !== o && (t.cancel = o), t.cancel && t.button.push({
                        id: "cancel",
                        value: t.cancelValue,
                        callback: t.cancel,
                        display: t.cancelDisplay
                    }), u.list[r] = new u.create(t))
                },
                d = function () {};
            d.prototype = o.prototype;
            var h = u.prototype = new d;
            u.create = function (t) {
                var i = this;
                e.extend(this, new o);
                var n = (t.original, e(this.node).html(t.innerHTML)),
                    a = e(this.backdrop);
                return this.options = t, this._popup = n, e.each(t, function (t, e) {
                    "function" == typeof i[t] ? i[t](e) : i[t] = e
                }), t.zIndex && (o.zIndex = t.zIndex), n.attr({
                    "aria-labelledby": this._$("title").attr("id", "title:" + this.id).attr("id"),
                    "aria-describedby": this._$("content").attr("id", "content:" + this.id).attr("id")
                }), this._$("close").css("display", this.cancel === !1 ? "none" : "").attr("title", this.cancelValue).on("click", function (t) {
                    i._trigger("cancel"), t.preventDefault()
                }), this._$("dialog").addClass(this.skin), this._$("body").css("padding", this.padding), t.quickClose && a.on("onmousedown" in document ? "mousedown" : "click", function () {
                    return i._trigger("cancel"), !1
                }), this.addEventListener("show", function () {
                    a.css({
                        opacity: 0,
                        background: t.backdropBackground
                    }).animate({
                        opacity: t.backdropOpacity
                    }, 150)
                }), this._esc = function (t) {
                    var e = t.target,
                        n = e.nodeName,
                        s = /^input|textarea$/i,
                        a = o.current === i,
                        r = t.keyCode;
                    !a || s.test(n) && "button" !== e.type || 27 === r && i._trigger("cancel")
                }, e(document).on("keydown", this._esc), this.addEventListener("remove", function () {
                    e(document).off("keydown", this._esc), delete u.list[this.id]
                }), s++, u.oncreate(this), this
            }, u.create.prototype = h, e.extend(h, {
                content: function (t) {
                    var i = this._$("content");
                    return "object" == typeof t ? (t = e(t), i.empty("").append(t.show()), this.addEventListener("beforeremove", function () {
                        e("body").append(t.hide())
                    })) : i.html(t), this.reset()
                }, type: function (t) {
                    var e = this._$("type"),
                        i = t.text || "\u63d0\u793a\u6587\u5b57",
                        o = '<div class="y-clear">',
                        n = "";
                    switch (t.icon) {
                    case "warning":
                        n = "icon-bang";
                        break;
                    case "error":
                        n = "icon-no3";
                        break;
                    case "success":
                        n = "icon-yes3"
                    }
                    return o += '<span style="' + (t.leftWidth ? t.leftWidth : "") + '" class="type-icon ' + n + '"></span>', o += '<span style="' + (t.rightWidth ? t.rightWidth : "") + '">' + i + "</span></div>", e.html(o).show(), this
                }, title: function (t) {
                    return this._$("title").text(t), this._$("header")[t ? "show" : "hide"](), this
                }, width: function (t) {
                    return this._$("content").css("width", t), this.reset()
                }, height: function (t) {
                    return this._$("content").css("height", t), this.reset()
                }, button: function (t) {
                    t = t || [];
                    var i = this,
                        o = "",
                        n = 0;
                    return this.callbacks = {}, "string" == typeof t ? (o = t, n++) : e.each(t, function (t, s) {
                        var a = s.id = s.id || s.value,
                            r = "";
                        i.callbacks[a] = s.callback, s.display === !1 ? r = ' style="display:none"' : n++, o += '<button type="button" i-id="' + a + '"' + r + (s.disabled ? " disabled" : "") + (s.autofocus ? ' autofocus class="ui-dialog-autofocus"' : "") + ">" + s.value + "</button>", i._$("button").on("click", "[i-id=" + a + "]", function (t) {
                            var o = e(this);
                            o.attr("disabled") || i._trigger(a), t.preventDefault()
                        })
                    }), this._$("button").html(o), this._$("footer")[n ? "show" : "hide"](), this
                }, statusbar: function (t) {
                    return this._$("statusbar").html(t)[t ? "show" : "hide"](), this
                }, _$: function (t) {
                    return this._popup.find("[i=" + t + "]")
                }, _trigger: function (t) {
                    var e = this.callbacks[t];
                    return "function" != typeof e || e.call(this) !== !1 ? this.close().remove() : this
                }
            }), u.oncreate = e.noop, u.getCurrent = function () {
                return o.current
            }, u.get = function (t) {
                return void 0 === t ? u.list : u.list[t]
            }, u.list = {}, u.defaults = n, t.exports = u
        }(jQuery)
    },
    function (t, e) {},
    function (t, e) {
        ! function (e) {
            function i() {
                this.destroyed = !1, this.__popup = e("<div />").css({
                    display: "none",
                    position: "absolute",
                    outline: 0
                }).attr("tabindex", "-1").html(this.innerHTML).appendTo("body"), this.__backdrop = this.__mask = e("<div />").css({
                    opacity: .7,
                    background: "#000"
                }), this.node = this.__popup[0], this.backdrop = this.__backdrop[0], o++
            }
            var o = 0,
                n = !("minWidth" in e("html")[0].style),
                s = !n;
            e.extend(i.prototype, {
                node: null,
                backdrop: null,
                fixed: !1,
                destroyed: !0,
                open: !1,
                returnValue: "",
                autofocus: !0,
                align: "bottom left",
                innerHTML: "",
                className: "dbl-popup",
                show: function (t) {
                    if (this.destroyed) return this;
                    var o = this.__popup,
                        a = this.__backdrop;
                    if (this.__activeElement = this.__getActive(), this.open = !0, this.follow = t || this.follow, !this.__ready) {
                        if (o.addClass(this.className).attr("role", this.modal ? "alertdialog" : "dialog").css("position", this.fixed ? "fixed" : "absolute"), n || e(window).on("resize", e.proxy(this.reset, this)), this.modal) {
                            var r = {
                                position: "fixed",
                                left: 0,
                                top: 0,
                                width: "100%",
                                height: "100%",
                                overflow: "hidden",
                                userSelect: "none",
                                zIndex: this.zIndex || i.zIndex
                            };
                            o.addClass(this.className + "-modal"), s || e.extend(r, {
                                position: "absolute",
                                width: e(window).width() + "px",
                                height: e(document).height() + "px"
                            }), a.css(r).attr({
                                tabindex: "0"
                            }).on("focus", e.proxy(this.focus, this)), this.__mask = a.clone(!0).attr("style", "").insertAfter(o), a.addClass(this.className + "-backdrop").insertBefore(o), this.__ready = !0
                        }
                        o.html() || o.html(this.innerHTML)
                    }
                    return o.addClass(this.className + "-show").show(), a.show(), this.reset().focus(), this.__dispatchEvent("show"), this
                }, showModal: function () {
                    return this.modal = !0, this.show.apply(this, arguments)
                }, close: function (t) {
                    return !this.destroyed && this.open && (void 0 !== t && (this.returnValue = t), this.__popup.hide().removeClass(this.className + "-show"), this.__backdrop.hide(), this.open = !1, this.blur(), this.__dispatchEvent("close")), this
                }, remove: function () {
                    if (this.destroyed) return this;
                    this.__dispatchEvent("beforeremove"), i.current === this && (i.current = null), this.__popup.remove(), this.__backdrop.remove(), this.__mask.remove(), n || e(window).off("resize", this.reset), this.__dispatchEvent("remove");
                    for (var t in this) delete this[t];
                    return this
                }, reset: function () {
                    var t = this.follow;
                    return t ? this.__follow(t) : this.__center(), this.__dispatchEvent("reset"), this
                }, focus: function () {
                    var t = this.node,
                        o = this.__popup,
                        n = i.current,
                        s = this.zIndex = i.zIndex++;
                    if (n && n !== this && n.blur(!1), !e.contains(t, this.__getActive())) {
                        var a = o.find("[autofocus]")[0];
                        !this._autofocus && a ? this._autofocus = !0 : a = t, this.__focus(a)
                    }
                    return o.css("zIndex", s), i.current = this, o.addClass(this.className + "-focus"), this.__dispatchEvent("focus"), this
                }, blur: function () {
                    var t = this.__activeElement,
                        e = arguments[0];
                    return e !== !1 && this.__focus(t), this._autofocus = !1, this.__popup.removeClass(this.className + "-focus"), this.__dispatchEvent("blur"), this
                }, addEventListener: function (t, e) {
                    return this.__getEventListener(t).push(e), this
                }, removeEventListener: function (t, e) {
                    for (var i = this.__getEventListener(t), o = 0; o < i.length; o++) e === i[o] && i.splice(o--, 1);
                    return this
                }, __getEventListener: function (t) {
                    var e = this.__listener;
                    return e || (e = this.__listener = {}), e[t] || (e[t] = []), e[t]
                }, __dispatchEvent: function (t) {
                    var e = this.__getEventListener(t);
                    this["on" + t] && this["on" + t]();
                    for (var i = 0; i < e.length; i++) e[i].call(this)
                }, __focus: function (t) {
                    try {
                        this.autofocus && !/^iframe$/i.test(t.nodeName) && t.focus()
                    } catch (e) {}
                }, __getActive: function () {
                    try {
                        var t = document.activeElement,
                            e = t.contentDocument,
                            i = e && e.activeElement || t;
                        return i
                    } catch (o) {}
                }, __center: function () {
                    var t = this.__popup,
                        i = e(window),
                        o = e(document),
                        n = this.fixed,
                        s = n ? 0 : o.scrollLeft(),
                        a = n ? 0 : o.scrollTop(),
                        r = i.width(),
                        c = i.height(),
                        l = t.width(),
                        u = t.height(),
                        d = (r - l) / 2 + s,
                        h = 382 * (c - u) / 1e3 + a,
                        p = t[0].style;
                    p.left = Math.max(parseInt(d), s) + "px", p.top = Math.max(parseInt(h), a) + "px"
                }, __follow: function (t) {
                    var i = t.parentNode && e(t),
                        o = this.__popup;
                    if (this.__followSkin && o.removeClass(this.__followSkin), i) {
                        var n = i.offset();
                        if (n.left * n.top < 0) return this.__center()
                    }
                    var s = this,
                        a = this.fixed,
                        r = e(window),
                        c = e(document),
                        l = r.width(),
                        u = r.height(),
                        d = c.scrollLeft(),
                        h = c.scrollTop(),
                        p = o.width(),
                        f = o.height(),
                        _ = i ? i.outerWidth() : 0,
                        b = i ? i.outerHeight() : 0,
                        v = this.__offset(t),
                        g = v.left,
                        m = v.top,
                        y = a ? g - d : g,
                        w = a ? m - h : m,
                        k = a ? 0 : d,
                        x = a ? 0 : h,
                        $ = k + l - p,
                        T = x + u - f,
                        E = {},
                        L = this.align.split(" "),
                        C = this.className + "-",
                        N = {
                            top: "bottom",
                            bottom: "top",
                            left: "right",
                            right: "left"
                        },
                        I = {
                            top: "top",
                            bottom: "top",
                            left: "left",
                            right: "left"
                        },
                        j = [{
                            top: w - f,
                            bottom: w + b,
                            left: y - p,
                            right: y + _
                        }, {
                            top: w,
                            bottom: w - f + b,
                            left: y,
                            right: y - p + _
                        }],
                        z = {
                            left: y + _ / 2 - p / 2,
                            top: w + b / 2 - f / 2
                        },
                        M = {
                            left: [k, $],
                            top: [x, T]
                        };
                    e.each(L, function (t, e) {
                        j[t][e] > M[I[e]][1] && (e = L[t] = N[e]), j[t][e] < M[I[e]][0] && (L[t] = N[e])
                    }), L[1] || (I[L[1]] = "left" === I[L[0]] ? "top" : "left", j[1][L[1]] = z[I[L[1]]]), C += L.join("-") + " " + this.className + "-follow", s.__followSkin = C, i && o.addClass(C), E[I[L[0]]] = parseInt(j[0][L[0]]), E[I[L[1]]] = parseInt(j[1][L[1]]), o.css(E)
                }, __offset: function (t) {
                    var i = t.parentNode,
                        o = i ? e(t).offset() : {
                            left: t.pageX,
                            top: t.pageY
                        };
                    t = i ? t : t.target;
                    var n = t.ownerDocument,
                        s = n.defaultView || n.parentWindow;
                    if (s == window) return o;
                    var a = s.frameElement,
                        r = e(n),
                        c = r.scrollLeft(),
                        l = r.scrollTop(),
                        u = e(a).offset(),
                        d = u.left,
                        h = u.top;
                    return {
                        left: o.left + d - c,
                        top: o.top + h - l
                    }
                }
            }), i.zIndex = 1024, i.current = null, t.exports = i
        }(jQuery)
    },
    function (t, e, i) {
        t.exports = {
            backdropBackground: "#000",
            backdropOpacity: .3,
            content: '<span class="ui-dialog-loading">Loading..</span>',
            title: "",
            statusbar: "",
            button: null,
            ok: null,
            cancel: null,
            okValue: "ok",
            cancelValue: "cancel",
            cancelDisplay: !0,
            width: "",
            height: "",
            padding: "",
            skin: "",
            quickClose: !1,
            cssUri: "../css/ui-dialog.css",
            innerHTML: i(6)()
        }
    },
    function (module, exports) {
        module.exports = function (obj) {
            obj || (obj = {});
            var __p = "";
            with(obj) __p += '\n<div i="dialog" class="ui-dialog dbl-components-dialog">\n    <div class="ui-dialog-arrow-a"></div>\n    <div class="ui-dialog-arrow-b"></div>\n    <table class="ui-dialog-grid">\n        <tr>\n            <td i="header" class="ui-dialog-header">\n                <button i="close" class="ui-dialog-close">&#215;</button>\n                <div i="title" class="ui-dialog-title"></div>\n            </td>\n        </tr>\n        <tr>\n            <td i="body" class="ui-dialog-body">\n                <div i="type" class="ui-dialog-type"></div>\n                <div i="content" class="ui-dialog-content"></div>\n            </td>\n        </tr>\n        <tr>\n            <td i="footer" class="ui-dialog-footer">\n                <div i="statusbar" class="ui-dialog-statusbar"></div>\n                <div i="button" class="ui-dialog-button"></div>\n            </td>\n        </tr>\n    </table>\n</div>\n';
            return __p
        }
    }
]);