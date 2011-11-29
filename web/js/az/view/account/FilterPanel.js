Ext.define('alexzam.his.view.account.FilterPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.FilterPanel',

    requires:[
        'Ext.layout.container.Border',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.model.account.proxy.Category',
        'alexzam.his.model.account.store.AccStat'
    ],

    layout:'border',
    items:[
        {
//                xtype:'form',
            id:'frmFilter',
            region:'center',
            title:'Фильтр',
            layout:'vbox',
            bodyPadding:5,
            autoScroll:true
//                items:[
//                    {
//                        xtype:'datefield',
//                        fieldLabel:'С',
//                        id:'tbFilterFrom',
//                        name:'from',
//                        format:'d.m.Y',
//                        validateOnChange:false,
//                        labelWidth:65,
//                        width:175,
//                        listeners:{
//                            change:account.onFilterChange
//                        }
//                    },
//                    {
//                        xtype:'datefield',
//                        fieldLabel:'По',
//                        id:'tbFilterTo',
//                        name:'to',
//                        format:'d.m.Y',
//                        validateOnChange:false,
//                        labelWidth:65,
//                        width:175,
//                        listeners:{
//                            change:account.onFilterChange
//                        }
//                    },
//                    {
//                        xtype:'combo',
//                        fieldLabel:'Категория',
//                        name:'cat',
//                        id:'cbFilterCategory',
//                        queryMode:'local',
//                        store:'stFilterCats',
//                        valueField:'id',
//                        displayField:'name',
//                        lastQuery:'',
//                        labelWidth:65,
//                        width:175,
//                        listeners:{
//                            change:account.onFilterChange
//                        }
//                    },
//                    {
//                        xtype:'button',
//                        text:'Удалить',
//                        handler:account.onBtDelete
//                    }
//                ]
        },
        {
            xtype:'grid',
            region:'south',
//                store:'stAccStats',
            columns:[
                {header:'Чего', dataIndex:'name', sortable:false, menuDisabled:true},
                {header:'Сколько', dataIndex:'val', flex:1, sortable:false, menuDisabled:true}
            ]
        }
    ],

    initComponent:function ()
    {
        this.callParent();

        var store = Ext.create('alexzam.his.model.account.store.Category', {
            storeId:'stFilterCats',
            proxy:Ext.create('alexzam.his.model.account.proxy.Category', {
                rootUrl:this.rootUrl
            })
        });

        var store2 = Ext.create('alexzam.his.model.account.store.AccStat', {
            storeId:'stAccStats'
        });
    }
});