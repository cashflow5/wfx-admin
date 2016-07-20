/**
* by lijunfang 20160324
**/
var Upload = function(){
    function fileupload(imgId,btnId,imgWidth,imgHeight){
        var $ = jQuery,
        $list = $(imgId),
        // 优化retina, 在retina下这个值是2
        ratio = window.devicePixelRatio || 1,

        // 缩略图大小
        thumbnailWidth = (imgWidth ? imgWidth :50) * ratio,
        thumbnailHeight = (imgHeight ? imgHeight :50) * ratio,

        // Web Uploader实例
        uploader;

    // 初始化Web Uploader
    uploader = WebUploader.create({

        // 自动上传。
        auto: true,

        // swf文件路径
        swf: '/static/plugin/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: '/commodity/uploadBrandImg.sc?brandId='+$("#brandId").val()+"&imgType="+imgId.substr(-1,1),

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: btnId,

        // 只允许选择文件，可选。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png'
            //mimeTypes: 'image/*'
            //gif,jpg,jpeg,bmp,png,
        }
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
                '<div id="' + file.id + '" class="file-item">' +
                    '<img>' +
                    '<div class="info">' + file.name + '</div>' +
                '</div>'
                ),
            $img = $li.find('img');

        $list.html( $li );
        // 创建缩略图
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }

            $img.attr( 'src', src );
        }, thumbnailWidth, thumbnailHeight );
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
            var val=parseInt(percentage*100);
            console.log(percentage);
        $('.process-bar-run').css('width', val + '%');
        $('.process-bar-text').html(val + '%');
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file,response ) {
    	if(response.result != 'success'){
    		YouGou.UI.Dialog.alert({message:response.msg});
    		return;
    	}else{
    		YouGou.UI.Dialog.alert({message:"上传成功！"});
    		return;
    	}
        $( '#'+file.id ).addClass('upload-state-done');
    });

    // 文件上传失败，现实上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');
        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

    console.log(uploader);
    }
   
    return{
        fileUpload:fileupload
    };
}();
