Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.RightPanel',
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
                userRadioOptions:me.userRadioOptions,
                region:'north',
                height:125
            },
            {
                xtype:'his.account.RightPanel',
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
