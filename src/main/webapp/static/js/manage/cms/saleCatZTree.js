/**
 * by lijunfang 20160328
 **/
var SaleCmsManage = function () {
    function Init() {
        LoadDirectortList($("#SettingNodeList"));
        loadTable('0');
    }

    function LoadDirectortList(target, isFlag) {
        LoadTree(null, target, isFlag);
    }

    function LoadTree(znodes, container, isFlag) {
        var setting = {
            async: {
                enable: true,
                dataType:'json',
                url: "/cms/getCatJsonForZtree.sc",
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
                    enable: true,
                    showRemoveBtn: showRemoveBtn,
                    showRenameBtn: showRenameBtn
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
        
        function showRemoveBtn(treeId, treeNode) {
            return treeNode.level > 0;
        }

        function showRenameBtn(treeId, treeNode) {
            return treeNode.level > 0;
        }
        
        function beforeDrag(treeId, treeNodes) {
            return true;
        }

        function beforeDrop(event, treeNodes, targetNode, moveType) {
            if (moveType == 'inner') {
        		YouGou.UI.Dialog.autoCloseTip(data.message);
                return false;
            } else {
                if (treeNodes[0].parentTId != targetNode.parentTId) {
            		YouGou.UI.Dialog.autoCloseTip(data.message);
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
        	var flag = true;
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            zTree.selectNode(treeNode);
            if (treeNode.isParent) {
                YouGou.UI.Dialog.autoCloseTip("此资源节点存在下级资源，无法删除！");
                flag = false;
            }else{
            	if(window.confirm("确认要删除资源（" + treeNode.name + " ）吗？")){
            		$.ajax({
                        url: '/cms/updateSaleCat.sc',
                        type: 'post',
                        dataType: 'json',
                        async: false,
                        data: {
                            id: treeNode.id,
                            deleteFlag: 1
                        }
                    }).done(function (data, status, xhr) {
                    	if(data.state == 'error'){
                    		YouGou.UI.Dialog.autoCloseTip(data.msg);
                    		flag = false;
                    	}
                    });
            	}else{
            		flag = false;
            	}
            }
            return flag;
        }

        function beforeRename(treeId, treeNode, newName, isCancel) {
        	if(treeNode.level == 0){
        		return;
        	}
            if (newName.length == 0) {
        		YouGou.UI.Dialog.autoCloseTip("节点名称不能为空");
                return false;
            }
            var isValid = treeNode.getParentNode().children.filter(function (item, i) {
                return item.name === newName
            }).length > 0;
            if (!isCancel && treeNode.name != newName && isValid) {
        		YouGou.UI.Dialog.autoCloseTip('该层级的资源名称重复，请重新输入！');
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
            if (treeNode.editNameFlag || treeNode.level > 1) return;
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
                            ptype: rtype,
                            strLevel: treeNode.level
                        };
                    $.ajax({
                        url: '/cms/addSaleCat.sc',//新增
                        type: 'post',
                        dataType: 'json',
                        data: nodeData
                    }).done(function (data, status, xhr) {
                    	nodeData.id = data.id;
                        nodeData.rtype = data.rtype;
                        nodeData.iconSkin = iconskins[data.rtype];
                        nodeData.level = data.level;
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
        	
        }

        function onNodeRename(event, treeId, treeNode) {
        	if(treeNode.level == 0){
        		return;
        	}
            $.ajax({
                url: '/cms/updateSaleCat.sc',//重命名
                type: 'post',
                dataType: 'json',
                data: {
                    id: treeNode.id,
                    name: treeNode.name
                }
            }).done(function (data, status, xhr) {
            	if(treeNode.level > 0){
        			loadTable(treeNode.pId);
        		}
            });
        }

        function onClick(event, treeId, treeNode) {
            console.log('Name:' + treeNode.name + ' ID:' + treeNode.id);
            if (treeNode.level > 2)
                return;
            loadTable(treeNode.id);
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
