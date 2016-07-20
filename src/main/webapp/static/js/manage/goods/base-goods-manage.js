/**
 * by lijunfang 20160324
 **/
var BaseGoodsManage = function () {
    function Init() {
        LoadDirectortList($("#SettingNodeList"));
    }

    function LoadDirectortList(target, isFlag) {
        LoadTree(null, target, isFlag);
    }

    function LoadTree(znodes, container, isFlag) {
        var setting = {
            async: {
                enable: true,
                url: "/static/js/manage/goods/test-tree-data.json",
                autoParam: ["id", "name=n", "level=lv"],
                otherParam: {
                    "bbyparam": "bbytree"
                },
                dataFilter: filter
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeClick: beforeClick
            }
        };
        if (!isFlag) {
            $.extend(setting, {
                view: {
                    expandSpeed: "",
                    addHoverDom: addHoverDom,
                    removeHoverDom: removeHoverDom,
                    selectedMulti: false
                },
                edit: {
                    enable: true
                },
                callback: {
                    beforeDrag: beforeDrag,
                    beforeDrop: beforeDrop,
                    beforeRemove: beforeRemove,
                    beforeRename: beforeRename,
                    onDrop: onDrop,
                    onRemove: onNodeRemove,
                    onRename: onNodeRename,
                    onClick: onClick
                }
            })
        }

        function beforeDrag(treeId, treeNodes) {
            return true;
        }

        function beforeDrop(event, treeNodes, targetNode, moveType) {
            if (moveType == 'inner') {
                layer.msg('只能同级拖放!', {
                    offset: 0,
                    icon: 2,
                    time: 1000
                });
                return false;
            } else {
                if (treeNodes[0].parentTId != targetNode.parentTId) {
                    layer.msg('只能同级拖放!', {
                        offset: 0,
                        icon: 2,
                        time: 1000
                    });
                    return false;
                }
            }
            return true;
        }

        function onDrop(event, treeId, treeNodes, targetNode, moveType) {
            var childs = treeNodes[0].getParentNode().children,
                sorts = [];
            $(childs).each(function (i, node) {
                sorts.push({id: node.id, sort: i});
            });
            console.log(JSON.stringify(sorts));
        }

        function beforeClick(treeId, treeNode, clickFlag) {
        	
        }

        function filter(treeId, parentNode, childNodes) {
            if (!childNodes) return null;
            for (var i = 0, l = childNodes.length; i < l; i++) {
                childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            }
            return childNodes;
        }

        function beforeRemove(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            zTree.selectNode(treeNode);
            if (treeNode.isParent) {
                layer.msg('此资源节点存在下级资源，无法删除！', {
                    offset: 0,
                    icon: 2,
                    time: 1000
                });
                return false;
            }
            return confirm("确认要删除资源（" + treeNode.name + " ）吗？");
        }

        function beforeRename(treeId, treeNode, newName, isCancel) {
            if (newName.length == 0) {
                layer.msg('节点名称不能为空！', {
                    offset: 0,
                    icon: 2,
                    time: 1000
                });
                return false;
            }
            var isValid = treeNode.getParentNode().children.filter(function (item, i) {
                return item.name === newName
            }).length > 0;
            if (!isCancel && treeNode.name != newName && isValid) {
                layer.msg('该层级的资源名称重复，请重新输入！', {
                    offset: 0,
                    icon: 2,
                    time: 1000
                });
                return false;
            }
            return true;
        }

        var newCount = 1,
            rTypes = {'root': '根目录', 'folder': '目录', 'menu': '菜单', 'point': '特殊功能点'},
            iconskins = {'root': 'root', folder: 'folder', menu: 'menu', point: 'point'};

        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span"),
                rtype = treeNode.rtype;
            if (treeNode.editNameFlag || treeNode.level > 2) return;
            addBtn('menu');

            function addBtn(rtype) {
                var btnSelector = "#addBtn_" + rtype + "_" + treeNode.tId;
                if ($(btnSelector).length > 0) {
                    return;
                }
                var typeText = rTypes[rtype],
                    addStr = "<span class='button add' id='addBtn_" + rtype + "_" + treeNode.tId + "' title='添加" + typeText + "' onfocus='this.blur();'></span>";
                sObj.after(addStr);
                var btn = $(btnSelector);
                if (btn) btn.bind("click", function () {
                    var zTree = $.fn.zTree.getZTreeObj(treeId),
                        nodeData = {
                            pId: treeNode.id,
                            name: typeText + (newCount++),
                            ptype: rtype
                        };
                    $.ajax({
                        url: '/resource-manager/addnode',
                        type: 'post',
                        dataType: 'json',
                        data: nodeData
                    }).done(function (data, status, xhr) {
                        nodeData.id = data.id;
                        nodeData.rtype = data.rtype;
                        nodeData.iconSkin = iconskins[data.rtype];
                        nodeData.url = '';
                        zTree.addNodes(treeNode, nodeData);
                        console.log(nodeData);
                        console.log('添加成功')
                    });
                    return false;
                });
            }
        }

        function removeHoverDom(treeId, treeNode) {
            for (var i in rTypes) {
                $("#addBtn_" + i + "_" + treeNode.tId).unbind().remove();
            }
        }

        function onNodeRemove(event, treeId, treeNode) {
            $.ajax({
                url: '/resource-manager/delete',
                type: 'post',
                dataType: 'json',
                data: {
                    id: treeNode.id
                }
            }).done(function (data, status, xhr) {
                console.log(data);
                console.log('已删除')
            });
        }

        function onNodeRename(event, treeId, treeNode) {
            $.ajax({
                url: '/resource-manager/rename',
                type: 'post',
                dataType: 'json',
                data: {
                    id: treeNode.id,
                    text: treeNode.name
                }
            }).done(function (data, status, xhr) {
                $('#' + treeNode.tId + ' > a').click();
                console.log(data);
                console.log('已修改')
            });
        }

        function onClick(event, treeId, treeNode) {
            console.log('Name:' + treeNode.name + ' ID:' + treeNode.id);
            if (treeNode.level > 2)
                return;
            $.get('goods-category-list.shtml', function (data) {
                $('#goodsCategoryList').html(data).prepend('<div class="pb10">'+ treeNode.name +'</div>');

            });
        }

        $('.node-edit-box').on('click', '#btnSave', function (e) {
            $(this).parents('form').submit();
        });

        $('.node-edit-box').on('click', '#btnCancel', function (e) {
            $(this).parents('.node-edit-box').empty();
        });

        $.fn.zTree.init(container, setting);
    }

    $(function () {
        Init();
    });

    return {
    };
}();