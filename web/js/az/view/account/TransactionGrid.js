Ext.define('alexzam.his.view.account.TransactionGrid', {
    extend:'Ext.grid.Panel',
    alias:'widget.his.account.TransactionGrid',

    requires:[
        'alexzam.his.model.account.store.Transaction',
        'alexzam.his.model.account.proxy.Transaction',
        'Ext.grid.column.Date',
        'Ext.grid.feature.Summary'
    ],

//        store:'stTrans',
//        id:'gridTrans',
//        multiSelect:true,
    columns:[
        {
            header:'Когда',
            dataIndex:'timestamp',
            xtype:'datecolumn',
            format:'d.m.Y'
        }
//            ,
//            {header:'Кто', dataIndex:'actor_name'},
//            {
//                header:'Сколько',
//                dataIndex:'amount',
//                xtype:'numbercolumn',
//                summaryType:'sum'
//            },
//            {
//                header:'Категория',
//                dataIndex:'category_name'
//            },
//            {
//                header:'Комментарий',
//                dataIndex:'comment',
//                flex:1
//            }
    ],

    selType:'rowmodel',

    features:[
        {ftype:'summary'}
    ],

    viewConfig:{
        getRowClass:function (record)
        {
            var t = record.get('type');
            if (t.length == 1) return "acc-transrow-" + t;
            else return '';
        }
    },

    initComponent:function ()
    {
        this.callParent();

        var store = Ext.create('alexzam.his.model.account.store.Transaction', {
            storeId:'stTrans',
            proxy:Ext.create('alexzam.his.model.account.proxy.Transaction', {
                rootUrl:this.rootUrl
            })
        });
    }
});