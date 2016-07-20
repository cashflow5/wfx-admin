/**
 * Created by guo.r on 2016/4/14.
 */

var list_aftersale_apply = function(){
    function Init(){
        ReutunTypeSelect();
        $('[name="returnType"]').eq(0).click();
    }

    function ReutunTypeSelect(){
        $('[name="returnType"]').on('change', function(e){
            var data = {
                id: '3355',
                returnType: this.value,
                returnAddress: '深圳大张伟加菲嘉园鄙'
            };
            laytpl($('#formtemplate').html()).render(data, function (html) {
                $('#returnForm').html(html);
            });
        });
    }

    $(function(){
        Init();
    });

    return {}
}();
