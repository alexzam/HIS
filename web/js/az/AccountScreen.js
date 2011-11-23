Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.FilterPanel',
        'alexzam.his.view.account.TransactionGrid'
    ],

    config:{
        layout:'border',
        items:[
//            {xtype:'his.account.TopPanel'},
//            {xtype:'his.account.FilterPanel'},
            {xtype:'his.account.TransactionGrid'}
        ]
    }
});
