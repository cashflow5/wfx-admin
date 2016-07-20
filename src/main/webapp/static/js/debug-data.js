Mock.mock('http://g.cn', {
    'name': '@name',
    'age|1-100': 100,
    'color': '@color'
})

Mock.mock('/data/treedata', function (options) {
    var data = Mock.mock({
        'list|3-5': [
            {
                'id|+1': 10,
                'pId|+1': 1,
                'name|1': ['2013年', '2014年', '2015年', '宝贝图片'],
                't|1': '@name',
                'value|1': ['y2013', 'y2014', 'y2015', 'bbpic'],
                "isParent|1": true
            }
        ]
    });
    var list = [
        {
            "id": 1,
            "pId": 0,
            "name": "我的文件夹",
            "t": "",
            "open": "true",
            "click": false
        }
    ].concat(data.list);
    return list;
});

Mock.mock('/resource-manager/delete', 'post', {
    'result|1': 'sucess'
});

Mock.mock('/resource-manager/rename', 'post', {
    'result|1': 'sucess'
});

var nodeTypes = {folder: 'folder', menu: 'menu', point: 'point'};
Mock.mock('/resource-manager/addnode', 'post', function (options) {
    var params = options.data.split('&'),
        mkparam = {
            'result|1': 'sucess',
            'id|10000-90000': 10000
        };
    for (var i = 0; i < params.length; i++) {
        var param = params[i].split('='),
            key = param[0];
        if (key == 'ptype') {
            $.extend(mkparam, {rtype: nodeTypes[param[1]]});
        }
    }
    return Mock.mock(mkparam);
});

Mock.mock('/resource-manager/update', 'post', {
    'result|1': 'sucess'
});
