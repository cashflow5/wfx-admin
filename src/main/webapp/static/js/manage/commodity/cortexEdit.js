/**
 * Created by guo.r on 2016/6/3.
 */
var CORTEXT_NEW = function () {
    var editor;
    var compments = {
        text: function () {
            return '<article class="editor-component cms-text-box" type="文本">点击这里编辑文本信息</article>';
        },
        image: function () {
            return '<div class="editor-component cms-img-box" type="图片"><img src="/static/images/details-img.png" onclick="void(0);" /></div>';
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
            editor = K.create('#cortexEditor', {
                themeType: 'bby',
                width: "640px",
                height: "600",
                resizeType: 0,
                uploadJson : '/commodity/uploadCortexEdit.sc',
                fileManagerJson : '',
                allowFileManager : false,
                //pasteType: 1,
                allowPreviewEmoticons: false,
                allowImageUpload: true,
                newlineTag: 'br',
                cssPath: ['/static/plugin/kindeditor/themes/bby/bby-editor.css', '/static/css/mui.css'],
                bodyClass: 'cms-content',
                htmlTags: {
                	a: ['class', 'href'],
                    span: ['class'],
                    del: ['class'],
                    img: ['class', 'src', 'alt', 'title', '/'],
                    p: ['class'],
                    div: ['class', 'align', 'style'],
                    article: ['class']
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
                area: ['1000px', '500px'],
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
            var $this = $(this),type = $this.data('type');
                insertTag(compments[type]() + '\u200B');
        }); 
        //通过此方法获取编辑器的内容
        $('.saveCortext').on('click', function(){
            var strHtml = getHtml();//.replace(/editor-component /g,'');
        	$('#description').val(strHtml);
        	saveCortex();
        	return false;
        })
    }

    $(function () {
        Init();
    });
    return {
    	Editor: function(){
    		return editor;
    	}
    };
}();