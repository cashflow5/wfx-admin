/**
 * Created by guo.r on 2016/6/3.
 */
var Article_New = function () {
    var editor;
    var compments = {
        text: function () {
            return '<article class="editor-component cms-text-box" type="文本">点击这里编辑文本信息</article>';
        },
        image: function () {
            return '<div class="editor-component cms-img-box" type="图片"><img src="static/images/details-img.png" onclick="void(0);" /></div>';
        },
        video: function () {

        },
        goods: function () {
            var html = [];
            html.push('<a class="editor-component cms-goods-box" href="{goodsUrl}" type="商品">');
            html.push('<div class="cms-item"><img src="{goodsImg}" alt="{goodsTitle}" /></div>');
            html.push('<div class="cms-item">');
            html.push('<p>{goodsTitle}</p>');
            html.push('<p>售价：<span class="Red">&yen;{price}</span> <del class="Gray">&yen;<span class="f12">{oldprice}</span></del>');
            html.push('</p>');
            html.push('</div>');
            html.push('</a>');
            return html.join('');
        },
        shop: function () {
            var html = [];
            html.push('<a class="editor-component cms-shop-box" href="{shopUrl}" type="店铺">');
            html.push('<div class="cms-item"><img src="{shopImg}" /></div>');
            html.push('<div class="cms-item">');
            html.push('<p>{shopNo}</p>');
            html.push('<p class="Gray">{shopDesc}</p>');
            html.push('</div>');
            html.push('</a>');
            return html.join('');
        }
    };

    function Init() {
        loadKindeditor();
        editorOperation();
    }

    /**
     * 加载发送短信编辑器
     */
    function loadKindeditor() {
        KindEditor.ready(function (K) {
            editor = K.create('#articleEditor', {
                //themeType: 'bby',
                width: "640px",
                height: "600",
                resizeType: 0,
                //pasteType: 1,
                allowPreviewEmoticons: false,
                allowImageUpload: true,
                items : [
                    'source', '|', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                    'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                    'insertunorderedlist', '|', 'image', 'link'],
                newlineTag: 'p',
                cssPath: ['static/plugin/kindeditor/themes/bby/bby-editor.css', 'http://10.0.20.199:8512/static/css/mui.css'],
                bodyClass: 'cms-content',
                htmlTags: {
                    a: ['class', 'href', 'style'],
                    article: ['class'],
                    div: ['class', 'align', 'style'],
                    del: ['class'],
                    em: ['style'],
                    img: ['class', 'src', 'alt', 'title', '/'],
                    p: ['class', 'style'],
                    span: ['class', 'style'],
                    strong: ['style'],
                    u: ['style']
                },
                afterChange: function () {
                    //if (compute() > 63) {
                    //    return;
                    //}
                },
                afterFocus: function () {
                    $('.ke-container-bby').addClass('hover');
                },
                afterBlur: function () {
                    $('.ke-container-bby').removeClass('hover');
                },
                afterCreate: function () {
                    $('.ke-edit-iframe').contents().find('html').addClass('yg-cms-detail');
                    console.log('create success!')
                }
            });
        });
    }

    /**
     * 获取编辑器内容
     * @returns {String} 编辑器内容
     */
    function getHtml() {
        return editor.html();
    }

    /**
     * 插入标签
     * @param {String} compoment 控件
     */
    function insertTag(compoment) {
        editor.insertHtml(compoment);
    }

    function getList(url, title, callback) {
        $.get(url, function (data) {
            layer.open({
                title: title,
                content: data,
                type: 1,
                area: ['900px', '500px'],
                offset: '10px',
                btn: ["添加", "取消"],
                yes: function (index) {
                    if (callback)
                        callback(index);
                },
                cancel: function () {

                }
            })
        });
    }

    function editorOperation() {
        $('.editor-group .btn').on('click', function () {
            var $this = $(this),
                type = $this.data('type');
            switch (type) {
                case 'goods':
                    getList.call(this, 'goods-list.shtml', '商品选择', function(index){
                        //获取中的行
                        var row = $('#mmgrid_table').mmGrid().selectedRows()[0];
                        if(!row){
                            layer.alert('最少选一行吧');
                            return;
                        }
                        insertTag(compments[type]().format({goodsUrl: 'http://m.yougou.net/yougoushop/item/{0}.sc'.format(row.no), goodsImg:row.defaultPic, goodsTitle: row.commodityName, price: row.salePrice, oldprice: row.publicPrice}) + '\u200B');
                        layer.close(index);
                    });
                    break;
                case 'shop':
                    getList.call(this, 'shop-list.shtml', '店铺选择', function(index){
                        //获取中的行
                        var row = $('#mmgrid_table').mmGrid().selectedRows()[0];
                        if(!row){
                            layer.alert('最少选一行吧');
                            return;
                        }
                        insertTag(compments[type]().format({shopUrl: 'http://m.yougou.net/{0}.sc'.format(row.sellerId), shopImg:'http://w1.ygimg.cn/{0}'.format(row.logoUrl), shopNo: row.loginName, shopDesc: row.notice || ''}) + '\u200B');
                        layer.close(index);
                    });
                    break;
                default:
                    insertTag(compments[type]() + '&nbsp;');
                    break;
            }
        });

        //通过此方法获取编辑器的内容
        $('.GetHtml').on('click', function(){
            var strHtml = getHtml().replace(/editor-component /g,'');
            console.log(strHtml);
        })
    }

    $(function () {
        Init();
    });
    return {
    };
}();