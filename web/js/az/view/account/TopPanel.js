Ext.define('alexzam.his.view.account.TopPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.TopPanel',

    requires:[
        'Ext.layout.container.Border',
        'alexzam.his.view.account.AddTransactionForm'
    ],

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
                    text:'Выйти',
                    handler:this.onBtLogout
                },
                {
                    xtype:'panel',
                    region:'center',
                    layout:'fit',
                    html:'<span id="account_amount">load</span>'
                }
            ]
        }
    ],

    initComponent:function ()
    {
        var me = this;
        me.items.push(Ext.create('Ext.panel.Panel', {
            region:'center',
            layout:'fit',
            items:[
                Ext.create('alexzam.his.view.account.AddTransactionForm', {
                    userRadioOptions:me.userRadioOptions
                })
            ]
        }));

        me.callParent();
    },

    onBtLogout:function(){
        document.location = "login?mode=out";
    }
});