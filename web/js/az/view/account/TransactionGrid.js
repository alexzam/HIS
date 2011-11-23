Ext.define('alexzam.his.view.account.TransactionGrid', {
    extend:'Ext.grid.Panel',
    alias:'widget.his.account.TransactionGrid',

    requires:[
//        'Ext.layout.container.Border'
    ],

    config:{
        region:'center'
//        ,
//        store:'stTrans',
//        id:'gridTrans',
//        multiSelect:true,
//        columns:[
//            {
//                header:'Когда',
//                dataIndex:'timestamp',
//                xtype:'datecolumn',
//                format:'d.m.Y'
//            },
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
//        ],
//        selType:'rowmodel',
//        features:[
//            {ftype:'summary'}
//        ],
//        viewConfig:{
//            getRowClass:function (record)
//            {
//                var t = record.get('type');
//                if (t.length == 1) return "acc-transrow-" + t;
//                else return '';
//            }
//        }
    }
});