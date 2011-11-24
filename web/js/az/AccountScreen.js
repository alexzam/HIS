Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.FilterPanel',
        'alexzam.his.view.account.TransactionGrid'
    ],

    layout:'border',

    initComponent:function ()
    {
        var me = this;

        me.items = [
            {
                xtype:'his.account.TopPanel',
                rootUrl:me.rootUrl,
                region:'north',
                height:125
            },
            {
                xtype:'his.account.FilterPanel',
                rootUrl:me.rootUrl,
                region:'east',
                width:200
            },
            {
                xtype:'his.account.TransactionGrid',
                region:'center',
                rootUrl:me.rootUrl
            }
        ];

        me.callParent();
    }

});
