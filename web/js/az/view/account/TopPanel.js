Ext.define('alexzam.his.view.account.TopPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.TopPanel',

    requires:[
        'Ext.layout.container.Border'
    ],

    config:{
        region:'north',
        height:125,
        layout:'border',

        items:[
            {
                xtype:'panel',
                region:'east',
                width:230,
                layout:'border',
                items:[
                    {
                        xtype:'button',
                        region:'north',
                        text:'Выйти'
//                        ,
//                        handler:account.logout
                    },
                    {
                        xtype:'panel',
                        region:'center',
                        layout:'fit',
                        html:'<span id="account_amount">load</span>'
                    }
                ]
            },
            {
//                xtype:'panel',
                region:'center'
//                ,
//                layout:'fit',
//                items:[
//                    srcAddForm
//                ]
            }
        ]
    }
});